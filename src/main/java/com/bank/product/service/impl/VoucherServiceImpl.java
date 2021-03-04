package com.bank.product.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bank.product.audit.MessageSenderEventLog;
import com.bank.product.client.CoreClient;
import com.bank.product.domain.Voucher;
import com.bank.product.domain.VoucherFailed;
import com.bank.product.dto.SmsDTO;
import com.bank.product.dto.VoucherDTO;
import com.bank.product.dto.response.VoucherFailedRepository;
import com.bank.product.mapping.DTOMapper;
import com.bank.product.messaging.ProductOutputChannels;
import com.bank.product.messaging.event.PurchaseVoucherRetryEvent;
import com.bank.product.repository.VoucherRepository;
import com.bank.product.service.VoucherService;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VoucherServiceImpl implements VoucherService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final VoucherRepository voucherRepository;
	private final VoucherFailedRepository voucherFailedRepository;

	private final DTOMapper objMapper;

	private final CoreClient coreClient;

	private final MessageSenderEventLog messageSenderEventLog;

	private final ProductOutputChannels productChannels;
	
	//TODO move to configure
	private final int maxRetry = 3;

	@Override
	public VoucherDTO generate(String phone, boolean unHappyCase) {
		try {
			return doGenerate(phone, unHappyCase);
		} catch (Exception e) {
			logger.error("Can not generate voucher.", e);
			messageSenderEventLog.send(productChannels.retrySimVoucher(),
					PurchaseVoucherRetryEvent.builder().phone(phone).build());
			throw e;
		}
	}

	@Override
	public void generate4Retry(PurchaseVoucherRetryEvent event) {
		VoucherDTO voucherDTO = null;
		try {
			voucherDTO = doGenerate(event.getPhone());
		} catch (Exception e) {
			if(event.getRetryNumber() < maxRetry) {
				messageSenderEventLog.send(productChannels.retrySimVoucher(),
						PurchaseVoucherRetryEvent.builder().phone(event.getPhone())
						.retryNumber(event.getRetryNumber()+1)
						.build());
			} else {
				voucherFailedRepository.save(VoucherFailed.builder()
						.phone(event.getPhone())
						.dateGenerate(new Date())
						.build());
			}
		}
		if(Objects.nonNull(voucherDTO)) {
			coreClient.send(SmsDTO.builder()
					.phone(event.getPhone())
					.message("Your voucher: " + voucherDTO.getCode())
					.build());
		}
	}

	private VoucherDTO doGenerate(String phone) {
		return doGenerate(phone, false);
	}
	
	private VoucherDTO doGenerate(String phone, boolean unHappyCase) {
		try {
			final ResponseEntity<VoucherDTO> voucher = coreClient.generate();
			if (HttpStatus.OK.equals(voucher.getStatusCode()) && unHappyCase == false) {
				final Voucher voucherEntity = objMapper.toVoucher(voucher.getBody());
				voucherEntity.setPhone(phone);
				voucherRepository.save(voucherEntity);
				return voucher.getBody();
			} else {
				throw new InternalErrorException("Internal error");
			}
		} catch (Exception e) {
			logger.error("Can not call Core service.", e);
			throw e;
		}
	}

	@Override
	public List<VoucherDTO> getByPhone(String phone) {
		return voucherRepository.findByPhone(phone).stream().map(objMapper::toVoucherDTO).collect(Collectors.toList());
	}

}
