package com.talentstream.util;

import java.nio.charset.StandardCharsets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
 
public class RazorPayUtills {
    
	public static boolean verifySignature(String payload, String expectedSignature, String secret) throws Exception {
	    String actualSignature = calculateRFC2104HMAC(payload, secret);
	    System.out.println("Expected Signature: "+expectedSignature);
	    return actualSignature.equals(expectedSignature);
	  }	
 
     private static String calculateRFC2104HMAC(String data, String secret) throws Exception {
	    SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
	    Mac mac = Mac.getInstance("HmacSHA256");
	    mac.init(signingKey);
	    byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
	    System.out.println("Actual Signature is: "+new String(Hex.encodeHex(rawHmac)));
	    return new String(Hex.encodeHex(rawHmac));
	  }
}