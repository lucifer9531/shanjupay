package com.google.merchant.mapstruct;

import com.google.base.BaseConvert;
import com.google.merchant.api.dto.MerchantDTO;
import com.google.merchant.vo.MerchantRegisterVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 将商户注册vo和dto进行转换
 * Created by Administrator.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MerchantRegisterConvert extends BaseConvert<MerchantDTO, MerchantRegisterVO> {

}
