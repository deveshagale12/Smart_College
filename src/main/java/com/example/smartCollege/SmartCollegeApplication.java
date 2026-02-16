
package com.smartCollege;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@SpringBootApplication
@EnableScheduling
public class SmartCollegeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartCollege2Application.class, args);
	}

}

