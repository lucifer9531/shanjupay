package com.google.merchant.mapstruct;

import com.google.base.BaseConvert;
import com.google.merchant.api.dto.AppDTO;
import com.google.merchant.domain.App;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppConvert extends BaseConvert<AppDTO, App> {

}
