package com.bank.product.messaging.event;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class PurchaseVoucherRetryEvent implements Serializable{

	private static final long serialVersionUID = 1L;
	private String phone;
	
	@Builder.Default
	private int retryNumber = 1;
}
