package com.bank.product.dto.response;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@JsonInclude(value = Include.NON_NULL)
public class ResponseAbstract<T> implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Builder.Default
	private String status = String.valueOf(HttpStatus.OK.value());
	private String message;
	private T data;
	private Collection<T> datas;
	
}
