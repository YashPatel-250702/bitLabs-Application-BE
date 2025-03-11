package com.talentstream.entity;

import javax.validation.constraints.NotBlank;

public class VerifyPaymentRequest {
	
	@NotBlank(message = "Please Provide Payment Id To Verify Payment Details")
	private String payment_id;
	
	@NotBlank(message = "Please Provide Order Id To Verify Payment Details")
	private String order_id;
	
	@NotBlank(message = "Please Provide Signature To Verify Payment Details")
	private String signature;
	
	
	public VerifyPaymentRequest(String payment_id, String order_id, String signature) {
		super();
		this.payment_id = payment_id;
		this.order_id = order_id;
		this.signature = signature;
	}
	public String getPayment_id() {
		return payment_id;
	}
	public void setPayment_id(String payment_id) {
		this.payment_id = payment_id;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public VerifyPaymentRequest() {
		super();
	}
	
	
 
}
 
 