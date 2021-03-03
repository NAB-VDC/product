package com.bank.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.product.domain.Voucher;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {

	List<Voucher> findByPhone(String phone);
}
