// package com.talentstream.config;




// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.jodconverter.local.office.LocalOfficeManager;


// @Configuration
// public class LibreOfficeConfig {

//     private static LocalOfficeManager officeManager;
   
//     @Bean(destroyMethod = "stop") 
//     public LocalOfficeManager officeManager() {
//         if (officeManager == null) { 
//             officeManager = LocalOfficeManager.builder()
//                     .install()
//                     .portNumbers(8100)  
//                     .build();
//             try {
//                 officeManager.start();
//                 System.out.println( "LibreOffice started successfully.");
//             } catch (Exception e) {
//                 System.err.println("Error starting LibreOffice: " + e.getMessage());
//             }
//         }
//         return officeManager;
//     }
// }