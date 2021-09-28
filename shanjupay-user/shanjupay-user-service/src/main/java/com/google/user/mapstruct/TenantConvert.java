package com.google.user.mapstruct;

import com.google.base.BaseConvert;
import com.google.user.api.dto.tenant.TenantDTO;
import com.google.user.domain.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TenantConvert extends BaseConvert<TenantDTO, Tenant> {

}
