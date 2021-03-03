package com.bank.product.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ProductOutputChannels {

	public static final String RETRY_SIM_VOUCHER = "retrySimVoucher";

	@Output
	MessageChannel retrySimVoucher();
	
}
