package com.google.merchant.mapstruct;

import com.google.base.BaseConvert;
import com.google.merchant.api.dto.StaffDTO;
import com.google.merchant.domain.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author sohyun
 * @date 2020/11/24 19:58
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StaffConvert extends BaseConvert<StaffDTO, Staff> {
}
