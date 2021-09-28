package com.google.user.mapstruct;

import com.google.base.BaseConvert;
import com.google.user.api.dto.tenant.BundleDTO;
import com.google.user.domain.Bundle;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BundleConvert extends BaseConvert<BundleDTO, Bundle> {

}
