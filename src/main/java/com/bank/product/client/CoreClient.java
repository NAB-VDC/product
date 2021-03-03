package com.bank.product.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bank.product.dto.SmsDTO;
import com.bank.product.dto.VoucherDTO;

@FeignClient("core")
public interface CoreClient {
	@GetMapping("/vouchers")
	public ResponseEntity<VoucherDTO> generate();
	
	@PostMapping("/sms/send")
	public ResponseEntity<Void> send(@RequestBody SmsDTO sms);
}
