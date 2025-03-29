package com.talentstream.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.Order;
import com.razorpay.RazorpayException;
import com.talentstream.dto.PaymentDetailsDto;
import com.talentstream.dto.RazorPayDto;
import com.talentstream.entity.CreateOrderRequest;
import com.talentstream.entity.VerifyPaymentRequest;
import com.talentstream.exception.CustomException;
import com.talentstream.service.RazorPayService;

@RestController
@RequestMapping("/razorPay")
public class RazorPayController {
	private static final Logger logger = LoggerFactory.getLogger(RazorPayController.class);

	@Autowired
	private RazorPayService razorPayService;

	@PostMapping("/createOrder")
	public ResponseEntity<Object> createOrder(@Valid @RequestBody CreateOrderRequest createOrderDto,
			BindingResult bindingResult) {
		try {
			logger.info("Creating order for recruiter ID: {}", createOrderDto.getRecruiter_id());

			if (bindingResult.hasErrors()) {
				Map<String, String> errors = new LinkedHashMap<>();
				bindingResult.getFieldErrors().forEach(fieldError -> {
					errors.put(fieldError.getField(), fieldError.getDefaultMessage());
				});
				return ResponseEntity.badRequest().body(errors);
			}

			Order order = razorPayService.createOrder(createOrderDto.getAmount(), "INR",
					createOrderDto.getRecruiter_id());
			if (order == null) {
				throw new CustomException("Unable to create order", HttpStatus.SERVICE_UNAVAILABLE);
			}

			RazorPayDto razorPayDto = new RazorPayDto(order.get("id"), createOrderDto.getRecruiter_id());
			logger.info("Order created successfully: {}", razorPayDto);
			return ResponseEntity.ok(razorPayDto);
		} catch (CustomException e) {
			logger.error("Custom error: {}", e.getMessage());
			return ResponseEntity.status(e.getStatus()).body(e.getMessage());
		} catch (Exception e) {
			logger.error("Error creating payment order: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Internal Error Creating Payment Order: " + e.getMessage());
		}
	}

	@PostMapping("/verifyPayment/{recruiterId}")
	public ResponseEntity<Object> verifyPayment(@Valid @RequestBody VerifyPaymentRequest paymentDetails,
			BindingResult bindingResult, @PathVariable Long recruiterId) {
		try {
			logger.info("Verifying payment for recruiter ID: {}", recruiterId);

			if (bindingResult.hasErrors()) {
				Map<String, String> errors = new LinkedHashMap<>();
				bindingResult.getFieldErrors().forEach(fieldError -> {
					errors.put(fieldError.getField(), fieldError.getDefaultMessage());
				});
				return ResponseEntity.badRequest().body(errors);
			}

			boolean isVerified = razorPayService.verifyPayment(paymentDetails.getPayment_id(),
					paymentDetails.getOrder_id(), paymentDetails.getSignature(), recruiterId);
			if (!isVerified) {
				throw new CustomException("Payment Verification Failed. Check Order ID, Payment ID, and Signature.",
						HttpStatus.BAD_REQUEST);
			}
			return ResponseEntity.ok("Payment Verified Successfully");
		} catch (CustomException e) {
			logger.error("Custom error: {}", e.getMessage());
			return ResponseEntity.status(e.getStatus()).body(e.getMessage());
		} catch (RazorpayException e) {
			logger.error("Error verifying payment: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Internal Error While Verifying Payment Order");
		} catch (Exception e) {
			logger.error("Unexpected error verifying payment: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error: " + e.getMessage());
		}
	}

	@GetMapping("/getPaymentDetail/{recruiterId}")
	public ResponseEntity<Object> getPaymentDetailById(@PathVariable Long recruiterId) {
		try {
			logger.info("Fetching payment details for recruiter ID: {}", recruiterId);

			PaymentDetailsDto details = razorPayService.getActivePayments(recruiterId);
			return ResponseEntity.ok().body(details);
		} catch (CustomException e) {
			logger.error("Custom error: {}", e.getMessage());
			return ResponseEntity.status(e.getStatus()).body(e.getMessage());
		} catch (Exception e) {
			logger.error("Unexpected error fetching payment details: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error: " + e.getMessage());
		}
	}
}
