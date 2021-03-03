package com.bank.product.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String uuid;
	private String code;
	private Date datetime;

}
