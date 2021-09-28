package com.google.user.mapstruct;

import com.google.base.BaseConvert;
import com.google.user.api.dto.authorization.PrivilegeDTO;
import com.google.user.domain.AuthorizationPrivilege;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorizationPrivilegeConvert extends BaseConvert<PrivilegeDTO, AuthorizationPrivilege> {

}
