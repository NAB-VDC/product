package com.bank.product.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.product.dto.VoucherDTO;
import com.bank.product.dto.response.ResponseAbstract;
import com.bank.product.service.VoucherService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/public/sims/vouchers")
@AllArgsConstructor
public class SimRest {
	
	private final VoucherService voucherService;

	@PostMapping("/purchase/{phone}")
	public ResponseEntity<ResponseAbstract<VoucherDTO>> purchare(@PathVariable("phone") String phone, @RequestParam(name = "unhappy", required = false) boolean unhappyCase){
		try {
			return ResponseEntity.ok(ResponseAbstract.<VoucherDTO>builder()
					.data(voucherService.generate(phone, unhappyCase))
					.build());
		} catch (Exception e) {
			return ResponseEntity.ok(ResponseAbstract.<VoucherDTO>builder()
					.message("Your Purchase is successful. We will send sms to you later.")
					.build());
		}
	}

	@GetMapping("{phone}")
	@PreAuthorize("#oauth2.hasScope('product')")
	public ResponseEntity<ResponseAbstract<VoucherDTO>> getAll(@PathVariable("phone") String phone){
		return ResponseEntity.ok(ResponseAbstract.<VoucherDTO>builder()
				.datas(voucherService.getByPhone(phone))
				.build());
	}
}
