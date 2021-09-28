package com.google.merchant.mapstruct;

import com.google.base.BaseConvert;
import com.google.merchant.api.dto.MerchantDTO;
import com.google.merchant.domain.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MerchantConvert extends BaseConvert<MerchantDTO, Merchant> {

}
