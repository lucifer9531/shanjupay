package com.google.user.mapstruct;

import com.google.base.BaseConvert;
import com.google.user.api.dto.resource.ApplicationDTO;
import com.google.user.domain.ResourceApplication;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResourceApplicationConvert extends BaseConvert<ApplicationDTO, ResourceApplication> {

}
