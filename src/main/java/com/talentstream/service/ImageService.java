package com.talentstream.service;

import java.io.IOException;
import java.net.URL;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.talentstream.AwsSecretsManagerUtil;
import com.talentstream.exception.CustomException;

@Service
public class ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);
    private String bucketName;
    private static final String FOLDERNAME = "Images/";

    private AmazonS3 initializeS3Client() {
        String secret = AwsSecretsManagerUtil.getSecret();
        JSONObject jsonObject = new JSONObject(secret);

        final String ACCESS_KEY = jsonObject.getString("AWS_ACCESS_KEY_ID");
        final String SECRET_KEY = jsonObject.getString("AWS_SECRET_ACCESS_KEY");
        final String REGION = jsonObject.getString("AWS_REGION");
        bucketName = jsonObject.getString("AWS_S3_BUCKET_NAME");

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.fromName(REGION))
                .build();
    }

    public String uploadImageToAWS(MultipartFile imageFile) {
    	
        if (imageFile.getSize() > 1 * 1024 * 1024) {
            throw new CustomException("File size should be less than 1MB.", HttpStatus.BAD_REQUEST);
        }

        if (!"image/png".equals(imageFile.getContentType())) {
            throw new CustomException("Only PNG file types are allowed.", HttpStatus.BAD_REQUEST);
        }

        String fileName = imageFile.getOriginalFilename();
        String objectKey = FOLDERNAME + fileName + ".png";

        try {
        	
            AmazonS3 s3Client = initializeS3Client();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(imageFile.getContentType());
            objectMetadata.setContentLength(imageFile.getSize());

            s3Client.putObject(new PutObjectRequest(bucketName, objectKey, imageFile.getInputStream(), objectMetadata));

            logger.info("Image uploaded successfully: {}", fileName);
            return objectKey;

        } catch (AmazonServiceException e) {
            logger.error("AWS  error while uploading image: {}", e.getMessage());
            throw new RuntimeException("AWS S3 error: " + e.getMessage(), e);
        } catch (IOException e) {
            logger.error("error while uploading image: {}", e.getMessage());
            throw new RuntimeException("I/O error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while uploading image: {}", e.getMessage());
            throw new CustomException("Internal Server Error: Unable to upload image.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String getImageURLFromAWs(String fileName) {
        try {
        	
            AmazonS3 s3Client = initializeS3Client();
            String objectKey = FOLDERNAME + fileName + ".png";

            if (!s3Client.doesObjectExist(bucketName, objectKey)) {
                logger.warn("Image not found: {}", fileName);
                throw new CustomException("Image not found: " + fileName + ".png", HttpStatus.NOT_FOUND);
            }

            URL url = s3Client.getUrl(bucketName, objectKey);
            logger.info("Image retrieved successfully: {}", fileName);
            return url.toString();

        } catch (CustomException e) {
            logger.error("Error while retrieving image: {}", e.getMessage());
            throw new CustomException(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error while retrieving image: {}", e.getMessage());
            throw new CustomException("Internal Server Error: Unable to retrieve image.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
