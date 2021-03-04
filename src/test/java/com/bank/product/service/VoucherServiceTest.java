package com.bank.product.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.bank.product.client.CoreClient;
import com.bank.product.dto.VoucherDTO;
import com.bank.product.dto.response.VoucherFailedRepository;
import com.bank.product.messaging.event.PurchaseVoucherRetryEvent;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VoucherServiceTest {

	private static final String PHONE = "0909888999";
	
	@Autowired VoucherService voucherService;
	@Autowired VoucherFailedRepository voucherFailedRepository;
	
	@MockBean
	CoreClient coreClient;
	
	@Before
	public void prepair() {
	}
	
	@Test
	public void test001() {
		Mockito.when(coreClient.generate()).thenReturn(new ResponseEntity<VoucherDTO>(VoucherDTO.builder()
				.code("123123")
				.datetime(new Date())
				.build(), HttpStatus.OK));
		VoucherDTO voucher = voucherService.generate(PHONE, false);
		assertNotNull(voucher);
	}
	
	@Test(expected = Exception.class)
	public void test002() {
		Mockito.when(coreClient.generate()).thenReturn(new ResponseEntity<VoucherDTO>(VoucherDTO.builder()
				.code("123123")
				.datetime(new Date())
				.build(), HttpStatus.OK));
		voucherService.generate(PHONE, true);
	}
	
	@Test(expected = Exception.class)
	public void test003() {
		Mockito.when(coreClient.generate()).thenReturn(coreClient.generate()).thenReturn(new ResponseEntity<VoucherDTO>(VoucherDTO.builder()
				.code("123123")
				.datetime(new Date())
				.build(), HttpStatus.UNAUTHORIZED));
		voucherService.generate(PHONE, true);
	}
	
	@Test(expected = Exception.class)
	public void test004() {
		Mockito.when(coreClient.generate()).thenReturn(coreClient.generate()).thenReturn(new ResponseEntity<VoucherDTO>(VoucherDTO.builder()
				.code("123123")
				.datetime(new Date())
				.build(), HttpStatus.UNAUTHORIZED));
		voucherService.generate(PHONE, false);
	}
	
	@Test
	public void test005() {
		Mockito.when(coreClient.generate()).thenReturn(new ResponseEntity<VoucherDTO>(VoucherDTO.builder()
				.code("123123")
				.datetime(new Date())
				.build(), HttpStatus.OK));
		voucherService.generate4Retry(PurchaseVoucherRetryEvent.builder()
				.phone(PHONE)
				.retryNumber(1)
				.build());
		
		Mockito.when(coreClient.generate()).thenReturn(new ResponseEntity<VoucherDTO>(VoucherDTO.builder()
				.code("123123")
				.datetime(new Date())
				.build(), HttpStatus.UNAUTHORIZED));
		voucherService.generate4Retry(PurchaseVoucherRetryEvent.builder()
				.phone(PHONE)
				.retryNumber(1)
				.build());
		voucherService.generate4Retry(PurchaseVoucherRetryEvent.builder()
				.phone(PHONE)
				.retryNumber(4)
				.build());
		assertTrue(voucherFailedRepository.count() > 0);
	}
	
	@Test
	public void test006() {
		Mockito.when(coreClient.generate()).thenReturn(new ResponseEntity<VoucherDTO>(VoucherDTO.builder()
				.code("123123")
				.datetime(new Date())
				.build(), HttpStatus.OK));
		voucherService.generate(PHONE, false);
		voucherService.generate(PHONE, false);
		assertFalse(voucherService.getByPhone(PHONE).isEmpty());
	}
}
