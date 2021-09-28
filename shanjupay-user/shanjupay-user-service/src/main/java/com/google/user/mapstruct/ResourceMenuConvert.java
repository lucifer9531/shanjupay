package com.google.user.mapstruct;

import com.google.base.BaseConvert;
import com.google.user.api.dto.menu.MenuDTO;
import com.google.user.domain.ResourceMenu;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResourceMenuConvert extends BaseConvert<MenuDTO, ResourceMenu> {

}
