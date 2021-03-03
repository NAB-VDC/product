package com.bank.product.service;

import java.util.List;

import com.bank.product.dto.VoucherDTO;
import com.bank.product.messaging.event.PurchaseVoucherRetryEvent;

public interface VoucherService {

	public VoucherDTO generate(String phone, boolean unHappyCase);
	public void generate4Retry(PurchaseVoucherRetryEvent event);
	
	public List<VoucherDTO> getByPhone(String phone);
}
