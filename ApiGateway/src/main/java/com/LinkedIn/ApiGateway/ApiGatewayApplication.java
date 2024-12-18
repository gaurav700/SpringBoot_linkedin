package com.LinkedIn.ApiGateway;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "API Gateway", version = "1.0", description = "Documentation API Gateway v1.0"))
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder) {
		return builder
				.routes()
				.route(r -> r.path("/connections/ConnectionService/v3/api-docs").and().method(HttpMethod.GET).uri("lb://CONNECTIONSERVICE"))
				.route(r -> r.path("/posts/PostService/v3/api-docs").and().method(HttpMethod.GET).uri("lb://POSTSERVICE"))
				.route(r -> r.path("/users/UserService/v3/api-docs").and().method(HttpMethod.GET).uri("lb://USERSERVICE"))
				.build();
	}

}
