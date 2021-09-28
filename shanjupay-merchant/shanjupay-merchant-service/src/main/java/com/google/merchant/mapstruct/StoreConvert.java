package com.google.merchant.mapstruct;

import com.google.base.BaseConvert;
import com.google.merchant.api.dto.StoreDTO;
import com.google.merchant.domain.Store;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author sohyun
 * @date 2020/11/24 19:57
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StoreConvert extends BaseConvert<StoreDTO, Store> {
}
