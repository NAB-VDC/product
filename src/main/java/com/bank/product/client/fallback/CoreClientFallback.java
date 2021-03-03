package com.bank.product.client.fallback;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bank.product.client.CoreClient;
import com.bank.product.dto.SmsDTO;
import com.bank.product.dto.VoucherDTO;

@Component
public class CoreClientFallback implements CoreClient{

	@Override
	public ResponseEntity<VoucherDTO> generate() {
		return ResponseEntity.unprocessableEntity().build();
	}

	@Override
	public ResponseEntity<Void> send(SmsDTO sms) {
		return ResponseEntity.unprocessableEntity().build();
	}

}
