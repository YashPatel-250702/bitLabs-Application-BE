package com.talentstream.config;

import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() throws Exception {
        try {
        	String firebaseConfigPath = System.getenv("FIREBASE_CONFIG");
            if(firebaseConfigPath==null) {
            	throw new Exception("Path Not Found"); 
            }
        	FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath);

        	FirebaseOptions options = FirebaseOptions.builder()
        	        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        	        .build();

        	if (FirebaseApp.getApps().isEmpty()) {
        	    FirebaseApp.initializeApp(options);
        	    System.out.println("Firebase has been initialized!");
        	}
        } catch (IOException e) {
            System.err.println("Error "+e.getMessage());
        }
    }
}
