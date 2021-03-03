package com.bank.product.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ProductInputChannels {

	@Input
    SubscribableChannel retrySimVoucher();
}
