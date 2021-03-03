package com.bank.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;

import com.bank.product.client.FeignErrorDecoder;
import com.bank.product.messaging.ProductInputChannels;
import com.bank.product.messaging.ProductOutputChannels;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableBinding({ProductOutputChannels.class, ProductInputChannels.class})
public class ProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApplication.class, args);
	}
	
	@Bean
    public FeignErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }
}
