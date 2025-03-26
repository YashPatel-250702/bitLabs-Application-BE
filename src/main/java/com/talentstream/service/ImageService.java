package com.talentstream.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.talentstream.AwsSecretsManagerUtil;
import com.talentstream.exception.CustomException;

@Service
public class ImageService {

	private String bucketName;

	private AmazonS3 initializeS3Client() {

		String secrete = AwsSecretsManagerUtil.getSecret();
		JSONObject jsonObject = new JSONObject(secrete);

		final String ACCESS_KEY = jsonObject.getString("AWS_ACCESS_KEY_ID");
		final String SECRET_KEY = jsonObject.getString("AWS_SECRET_ACCESS_KEY");

		final String REGION = jsonObject.getString("AWS_REGION");
		bucketName = jsonObject.getString("AWS_S3_BUCKET_NAME");

		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
		return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
				.withRegion(Regions.fromName(REGION)).build();
	}

	public String uploadImageToAWS(MultipartFile imageFile, String fileName) {

		if (imageFile.getSize() > 1 * 1024 * 1024) {
			throw new CustomException("File size should be less than 1MB.", HttpStatus.BAD_REQUEST);
		}

		String contentType = imageFile.getContentType();

		if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
			throw new CustomException("Only JPG and PNG file types are allowed.", HttpStatus.BAD_REQUEST);
		}

		String objectKey = "Images/"+fileName + ".png";

		try {
			AmazonS3 s3Client = initializeS3Client();

			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType(imageFile.getContentType());
			objectMetadata.setContentLength(imageFile.getSize());

			s3Client.putObject(new PutObjectRequest(bucketName, objectKey, imageFile.getInputStream(), objectMetadata));

			return objectKey;

		} catch (AmazonServiceException ase) {
			throw new RuntimeException("Failed to upload image to S3", ase);
		} catch (IOException e) {
			throw new RuntimeException("Error processing image file", e);
		}

	}

	public String getImageFromAWs(String fileName) {

		try {

			String objectKey = "Images/"+fileName + ".png";

			AmazonS3 s3Client = initializeS3Client();

			String url = s3Client.getUrl(bucketName, objectKey).toString();
			System.out.println("Url: "+url);
			return url;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "Error while getting image";
		}

	}
}
