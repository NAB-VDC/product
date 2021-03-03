package com.bank.product.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.bank.product.messaging.event.PurchaseVoucherRetryEvent;
import com.bank.product.service.VoucherService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class MessageListener {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final VoucherService voucherService;

	@StreamListener(ProductOutputChannels.RETRY_SIM_VOUCHER)
	public void retryPurchasePrepaid(@Payload PurchaseVoucherRetryEvent event) throws Exception {
		logger.debug("Received payload: {}", event);
		voucherService.generate4Retry(event);
	}
}
