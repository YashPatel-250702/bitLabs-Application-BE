package com.talentstream.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.talentstream.dto.PaymentDetailsDto;
import com.talentstream.entity.JobRecruiter;
import com.talentstream.entity.RazorPayOrder;
import com.talentstream.exception.CustomException;
import com.talentstream.repository.JobRecruiterRepository;
import com.talentstream.repository.RazorPayRepositry;
import com.talentstream.util.RazorPayUtills;

@Service
public class RazorPayService {

	@Autowired
	private RazorPayRepositry payRepositry;

	@Autowired
	private RazorpayClient razorpayClient;

	@Value("${razorpay.secret}")
	private String razorPaySecret;

	@Autowired
	private JobRecruiterRepository jobRecruiterRepository;

	public Order createOrder(Double amount, String currency, Long recruiterId) throws Exception {

		JobRecruiter recruiter = jobRecruiterRepository.findById(recruiterId).orElseThrow(
				() -> new CustomException("JobRecruiter with ID " + recruiterId + " not found.", HttpStatus.NOT_FOUND));

		Optional<RazorPayOrder> activeOrder = payRepositry.findByJobRecruiter_RecruiterIdAndIsActive(recruiterId, true);

		if (activeOrder.isPresent()) {
			throw new CustomException("Recruiter already has an active order", HttpStatus.BAD_REQUEST);
		}

		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount", amount * 100);
		orderRequest.put("currency", currency);
		orderRequest.put("payment_capture", 1);

		Order order = razorpayClient.orders.create(orderRequest);

		if (order == null)
			return null;

		RazorPayOrder razorPayOrder = new RazorPayOrder();
		razorPayOrder.setOrderId(order.get("id"));
		razorPayOrder.setOrderAmount(amount);
		razorPayOrder.setJobRecruiter(recruiter);
		razorPayOrder.setCurrency(currency);
		razorPayOrder.setIsActive(false);
		razorPayOrder.setOrderStatus("created");
		razorPayOrder.setOrderDate(LocalDateTime.now());
		razorPayOrder.setCreatedAt(LocalDateTime.now());
		payRepositry.save(razorPayOrder);

		return order;
	}

	public boolean verifyPayment(String paymentId, String orderId, String razorpaySignature, Long recruiterId)
			throws Exception {

		JobRecruiter recruiter = jobRecruiterRepository.findById(recruiterId).orElseThrow(
				() -> new CustomException("JobRecruiter with ID " + recruiterId + " not found.", HttpStatus.NOT_FOUND));

		RazorPayOrder order = payRepositry
				.findByOrderIdAndJobRecruiter_RecruiterIdAndOrderStatus(orderId, recruiterId, "created")
				.orElseThrow(() -> new CustomException("No created Order not found", HttpStatus.NOT_FOUND));

		String payload = orderId + '|' + paymentId;
		boolean isVerified = RazorPayUtills.verifySignature(payload, razorpaySignature, razorPaySecret);
		if (!isVerified) {
			throw new CustomException("Payment verification failed", HttpStatus.UNAUTHORIZED);
		}

		String paymentStatus = razorpayClient.payments.fetch(paymentId).get("status");
		order.setOrderStatus(paymentStatus);
		order.setIsActive("captured".equals(paymentStatus));
		order.setUpdatedAt(LocalDateTime.now());
		payRepositry.save(order);

		return "captured".equals(paymentStatus);
	}

	public PaymentDetailsDto getActivePayments(Long recruiterId) {
		RazorPayOrder order = payRepositry.findByJobRecruiter_RecruiterIdAndIsActive(recruiterId, true)
				.orElseThrow(() -> new CustomException("No Active order found to recruiter id: " + recruiterId,
						HttpStatus.NOT_FOUND));
		return convertToDTO(order);
	}

	private PaymentDetailsDto convertToDTO(RazorPayOrder order) {
		PaymentDetailsDto dto = new PaymentDetailsDto();
		dto.setOrderId(order.getOrderId());
		dto.setIsActive(order.getIsActive());
		dto.setAmount(order.getOrderAmount());
		dto.setOrderDate(order.getOrderDate());
		dto.setOrderStatus(order.getOrderStatus());
		dto.setRecruiterId(order.getJobRecruiter().getRecruiterId());
		return dto;

	}
}
