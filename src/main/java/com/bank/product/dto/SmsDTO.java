package com.bank.product.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String phone;
	private String message;
}
