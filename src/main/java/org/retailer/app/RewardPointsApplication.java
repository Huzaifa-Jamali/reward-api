package org.retailer.app;

import java.util.Collections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class RewardPointsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RewardPointsApplication.class, args);
	}

	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.paths(PathSelectors.any())
				.apis(RequestHandlerSelectors.basePackage("org.retailer"))
				.build()
				.apiInfo(apiDetails());
	}
	
	private ApiInfo apiDetails() {
		return new ApiInfo(
				"Reward Points API",
				"Sample API to calculate reward points",
				"1.0",
				"Free to use",
				new springfox.documentation.service.Contact("Huzaifa Jamali", "http://", "huzaifa.jamali27@gmail.com"),
				"API License",
				"http://",
				Collections.emptyList()
				);
	}

}
