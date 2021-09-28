package com.google.transaction.convert;

import com.google.base.BaseConvert;
import com.google.transaction.api.dto.PlatformChannelDTO;
import com.google.transaction.domain.PlatformChannel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author sohyun
 * @date 2020/11/19 22:53
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlatformChannelConvert extends BaseConvert<PlatformChannelDTO, PlatformChannel> {
}
