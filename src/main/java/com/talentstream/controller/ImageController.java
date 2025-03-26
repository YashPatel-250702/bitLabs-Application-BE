package com.talentstream.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.talentstream.exception.CustomException;
import com.talentstream.service.ImageService;

@RestController
@RequestMapping("/image")
public class ImageController {

	private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

	@Autowired
	private ImageService imageService;

	@PostMapping("/upload")
	public ResponseEntity<String> uploadImage(@RequestParam MultipartFile imageFile) {
		try {

			String filename = imageService.uploadImageToAWS(imageFile);
			return ResponseEntity.ok(filename + " Image uploaded successfully");

		} catch (Exception e) {

			return ResponseEntity.internalServerError().body("Internal Server Error: Unable to upload image.");
		}
	}

	@GetMapping("/getImage/{fileName}")
	public ResponseEntity<String> getImageUrl(@PathVariable String fileName) {
		try {
			String url = imageService.getImageURLFromAWs(fileName);

			return ResponseEntity.ok(url);
		} catch (CustomException e) {
			return ResponseEntity.status(e.getStatus()).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Internal Server Error: Unable to retrieve image.");
		}
	}
}
