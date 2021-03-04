package com.bank.product.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.bank.product.dto.VoucherDTO;
import com.bank.product.service.VoucherService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SimRestTest {

	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	VoucherService voucherService;

	@Test
	@SuppressWarnings("unchecked")
	public void test01() throws Exception {
		Mockito.when(voucherService.generate("123123123", false)).thenReturn(VoucherDTO.builder()
				.code("mock")
				.datetime(new Date())
				.build());
		mockMvc.perform(post("/public/sims/vouchers/purchase/123123123")).andExpect(status().isOk());
		Mockito.when(voucherService.generate("123123123", false)).thenThrow(Exception.class);
		mockMvc.perform(post("/public/sims/vouchers/purchase/123123123"))
		.andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Your Purchase is successful. We will send sms to you later."));
	}
	
	@Test
	public void test02() throws Exception {
		Mockito.when(voucherService.getByPhone("123123123")).thenReturn(Arrays.asList(VoucherDTO.builder()
				.code("mock")
				.datetime(new Date())
				.build()));
		mockMvc.perform(get("/public/sims/vouchers/123123123")).andExpect(status().isOk());
	}
}
