package com.google.user.mapstruct;

import com.google.base.BaseConvert;
import com.google.user.api.dto.authorization.RoleDTO;
import com.google.user.domain.AuthorizationRole;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorizationRoleConvert extends BaseConvert<RoleDTO, AuthorizationRole> {

}
