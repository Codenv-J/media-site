package com.coden;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


@ServletComponentScan(basePackages = "com.coden.filter")
@SpringBootApplication
public class MediaDocApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediaDocApplication.class, args);
    }

}