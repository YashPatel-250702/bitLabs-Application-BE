package com.talentstream.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.talentstream.exception.CustomException;
import com.talentstream.exception.UnsupportedFileTypeException;
import com.talentstream.service.ImageService;

@RestController
@RequestMapping("/image")
public class ImageController {
	
	private static final Logger logger = LoggerFactory.getLogger(ImageController.class);
	
	@Autowired
	private ImageService imageService;
	
	@PostMapping("/upload/{fileName}")
	public String fileUpload(@PathVariable String  fileName, @RequestParam MultipartFile imageFile) {
		try {
			String filename = imageService.uploadImageToAWS(imageFile,fileName );
			logger.info("Image uploaded successfully: {}", filename);
			return filename + " Image uploaded successfully";
		} catch (CustomException ce) {
			logger.error("Error occurred while uploading image : {}", ce.getMessage());
			return ce.getMessage();
		} catch (UnsupportedFileTypeException e) {
			logger.error("Unsupported file type during image upload");
			return "Only JPG and PNG file types are allowed.";
		} catch (MaxUploadSizeExceededException e) {
			logger.error("Max upload size exceeded during image upload");
			return "File size should be less than 1MB.";
		}
	}

	@GetMapping("/getImage/{fileName}")
	public ResponseEntity<byte[]> getCompanyLogo(@PathVariable String fileName) {
		try {
			byte[] imageBytes = imageService.getImageFromAWs(fileName);
			logger.info("Image downloaded successfully for fileName: {}", fileName);
			return ResponseEntity.ok()
					.contentType(MediaType.IMAGE_JPEG)
					.body(imageBytes);

		} catch (CustomException ce) {
			// Handle exception appropriately, e.g., return a 404 Not Found response
			logger.error("Error occurred while downloading image  for fileName: {}", fileName);
			return ResponseEntity.status(ce.getStatus()).body(null);
		}
	}
	

}
