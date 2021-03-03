package com.bank.product.dto.response;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.product.domain.VoucherFailed;

public interface VoucherFailedRepository extends JpaRepository<VoucherFailed, Long>{

}
