package com.talentstream.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
 
@Configuration
public class RazorpayConfig {
 
    @Value("${razorpay.key}")
    private String razorPayKey;
 
    @Value("${razorpay.secret}")
    private String razorPaySecret;
 
    @Bean
    RazorpayClient razorpayClient() throws RazorpayException {
        return new RazorpayClient(razorPayKey, razorPaySecret);
    }
 
	public String getRazorPayKey() {
		return razorPayKey;
	}
 
	public void setRazorPayKey(String razorPayKey) {
		this.razorPayKey = razorPayKey;
	}
 
	public String getRazorPaySecret() {
		return razorPaySecret;
	}
 
	public void setRazorPaySecret(String razorPaySecret) {
		this.razorPaySecret = razorPaySecret;
	}
 
	public RazorpayConfig(String razorPayKey, String razorPaySecret) {
		super();
		this.razorPayKey = razorPayKey;
		this.razorPaySecret = razorPaySecret;
	}
 
	public RazorpayConfig() {
		super();
	}
}
 

