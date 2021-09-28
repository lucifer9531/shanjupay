package com.google.merchant.mapstruct;

import com.google.base.BaseConvert;
import com.google.merchant.api.dto.MerchantDTO;
import com.google.merchant.vo.MerchantDetailVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author sohyun
 * @date 2020/11/18 23:23
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MerchantDetailConvert extends BaseConvert<MerchantDTO, MerchantDetailVO> {
}
