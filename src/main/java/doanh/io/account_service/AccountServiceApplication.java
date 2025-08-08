package doanh.io.account_service;

import doanh.io.account_service.config.EnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AccountServiceApplication {

	public static void main(String[] args) {

		EnvLoader.init();
		SpringApplication.run(AccountServiceApplication.class, args);
	}

}
