package com.bank.product.mapping;

import java.util.function.Function;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;

import com.bank.product.domain.Voucher;
import com.bank.product.dto.VoucherDTO;

@Mapper(componentModel = "spring", uses = {}, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface DTOMapper {

	@Mappings({
		@Mapping(target = "uuid", source = "publicId"),
		@Mapping(target = "code", source = "code"),
		@Mapping(target = "datetime", source = "dateGenerate")
	})
	VoucherDTO toVoucherDTO(Voucher entity);
	
	
	@Mappings({
		@Mapping(source = "uuid", target = "publicId"),
		@Mapping(source = "code", target = "code"),
		@Mapping(source = "datetime", target = "dateGenerate")
	})
	Voucher toVoucher(VoucherDTO dto);
}
