package org.rainbow.kinesheet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableCaching
@EnableMethodSecurity
public class KinesheetRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(KinesheetRestApplication.class, args);
	}

}
