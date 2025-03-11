package com.talentstream;

import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Configuration
public class AwsSecretsManagerUtil {

	public static String getSecret() {
		try {

			String secrets = System.getenv("AWS_CREDENTIALS");
			System.out.println("Secretes  " + secrets);

			//added null check condition
			if (secrets == null) {
				System.err.println("AWS_CREDENTIALS environment variable is not set.");
				return null;
			}

			JSONObject jsonObject = new JSONObject(secrets);

			String accessKey = jsonObject.getString("AWS_ACCESS_KEY_ID");
			String secretKey = jsonObject.getString("AWS_SECRET_ACCESS_KEY");

			String region1 = jsonObject.getString("AWS_REGION");
			Region region = Region.of(region1);

			if (accessKey == null || secretKey == null) {
				System.err.println("AWS credentials are not set in environment variables.");
				return null;
			}

			return secrets;

		} catch (Exception e) {
			System.err.println("An error occurred: " + e.getMessage());
			return null;
		}
	}

	// added method to get Database secret key with all required values like
	// username ,password
	public static String getDBSecret() {
		try {

			String secrete = getSecret();
			JSONObject jsonObject = new JSONObject(secrete);

			final String ACCESS_KEY = jsonObject.getString("AWS_ACCESS_KEY_ID");
			final String SECRET_KEY = jsonObject.getString("AWS_SECRET_ACCESS_KEY");

			final String REGION = jsonObject.getString("AWS_REGION");
			final String DATABASE_SECRET_NAME = jsonObject.getString("AWS_DATABASE_SECRET_NAME");

			// Initializing AWS Secrets Manager client 
			SecretsManagerClient secretsClient = SecretsManagerClient.builder().region(Region.of(REGION))
					.credentialsProvider(
							StaticCredentialsProvider.create(AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY)))
					.build();

			GetSecretValueRequest request = GetSecretValueRequest.builder().secretId(DATABASE_SECRET_NAME).build();

			GetSecretValueResponse response = secretsClient.getSecretValue(request);

			return response.secretString();

		} catch (Exception e) {
			System.err.println("An error occurred: " + e.getMessage());
			return null;
		}
	}

	
	
	public String getDbUsername() {
		String secret = getDBSecret();
		if (secret == null) {
			return null;
		}

		JSONObject jsonObject = new JSONObject(secret);
		System.out.println("Username is : " + jsonObject.getString("username"));
		return jsonObject.getString("username");
	}

	public String getDbPassword() {
		String secret = getDBSecret();
		if (secret == null) {
			return null;
		}

		JSONObject jsonObject = new JSONObject(secret);
		System.out.println("Password is : " + jsonObject.getString("password"));
		return jsonObject.getString("password");
	}

}
