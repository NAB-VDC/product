package com.bank.product.client;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class FeignErrorDecoder implements ErrorDecoder {
	private final ErrorDecoder defaultErrorDecoder = new Default();

	@Override
	public Exception decode(String methodKey, Response response) {
		HttpStatus httpStatus = HttpStatus.valueOf(response.status());
		switch (httpStatus) {
		case UNPROCESSABLE_ENTITY:
			return new UnprocessableEntityException(methodKey);
		default:
			return defaultErrorDecoder.decode(methodKey, response);
		}
	}
}
