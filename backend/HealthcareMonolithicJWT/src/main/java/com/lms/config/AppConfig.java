//package com.lms.config;
//
//import org.modelmapper.ModelMapper;
//import org.modelmapper.convention.MatchingStrategies;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class AppConfig {
//
//    @Bean
//    public ModelMapper modelMapper() {
//        ModelMapper mapper = new ModelMapper();
//        
//        // LOOSE ko hata kar STRICT kar diya hai taaki exact match ho
//        // Isse 'categoryName' sirf 'categoryName' mein hi jayega, 'categoryId' mein nahi!
//        mapper.getConfiguration()
//              .setMatchingStrategy(MatchingStrategies.STRICT)
//              .setFieldMatchingEnabled(true) // Private fields ko handle karne ke liye
//              .setAmbiguityIgnored(true);    // Confusion hone par ignore karne ke liye
//              
//        return mapper;
//    }
//}


package com.lms.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        // Sabse important line: Iske bina nested mapping (null issue) thik nahi hogi
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        return mapper;
    }
}