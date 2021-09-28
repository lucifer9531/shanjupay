package com.google.user.mapstruct;

import com.google.base.BaseConvert;
import com.google.user.api.dto.tenant.CreateTenantRequestDTO;
import com.google.user.domain.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TenantRequestConvert extends BaseConvert<CreateTenantRequestDTO, Tenant> {

}
