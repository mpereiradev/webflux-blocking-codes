package dev.matheuspereira.webflux.blocking.codes;

import io.swagger.v3.oas.models.annotations.OpenAPI30;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPI30
@SpringBootApplication
public class WebfluxBlockingCodesApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebfluxBlockingCodesApplication.class, args);
	}

}
