package com.talentstream.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.talentstream.AwsSecretsManagerUtil;
import java.util.Map;

@Service
public class ZohoAuthService {

    @Autowired
    private AwsSecretsManagerUtil secretsManagerUtil;

    private static final String TOKEN_URL = "https://accounts.zoho.com/oauth/v2/token";

  
    private String clientId="1000.N6ZQRDE1N00M0PWW9ECIESKXMENNEE";
    private String clientSecret="c0c143e0bec3b6b282d7922c8d02924c6d0f6d1e4e";
    private String refreshToken="1000.5b99532e2fba37aa0e3a456ba9b04793.43cb87365146be16c8d42438a694bb5c";
    private String accessToken;
    private long expiryTime; 
    

    /**
     * ✅ Fetch Zoho OAuth credentials from AWS Secrets Manager and initialize variables.
     */
//    private void loadCredentials() {
//        JSONObject credentials = new JSONObject(secretsManagerUtil.getSecret());
//        clientId = credentials.getString("ZOHO_CLIENT_ID");
//        clientSecret = credentials.getString("ZOHO_CLIENT_SECRET");
//        refreshToken = credentials.getString("ZOHO_REFRESH_TOKEN");
//    }

    /**
     * ✅ Retrieve a valid access token (refresh if expired)
     */
    public String getAccessToken() {
        if (accessToken == null || isAccessTokenExpired()) {
            refreshAccessToken();
        }
        return accessToken;
    }

    /**
     * ✅ Check if the access token has expired
     */
    private boolean isAccessTokenExpired() {
        return System.currentTimeMillis() >= expiryTime;
    }

    /**
     * ✅ Refresh access token using Zoho OAuth
     */
    public void refreshAccessToken() {
        // Ensure credentials are loaded
//        if (clientId == null || clientSecret == null || refreshToken == null) {
//            loadCredentials();
//        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Build request parameters
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("refresh_token", refreshToken);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("grant_type", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(TOKEN_URL, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                accessToken = (String) response.getBody().get("access_token");
                expiryTime = System.currentTimeMillis() + (response.getBody().get("expires_in") != null 
                                ? ((Integer) response.getBody().get("expires_in")) * 1000 
                                : 3600 * 1000); // Default expiry 1 hour

                System.out.println("✅ New Access Token Retrieved: " + accessToken);
            } else {
                System.err.println("❌ Failed to refresh token: " + response.getBody());
            }
        } catch (Exception e) {
            System.err.println("❌ Error refreshing access token: " + e.getMessage());
        }
    }
}
