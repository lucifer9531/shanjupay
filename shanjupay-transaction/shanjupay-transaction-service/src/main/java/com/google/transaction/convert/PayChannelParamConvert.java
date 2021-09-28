package com.google.transaction.convert;

import com.google.base.BaseConvert;
import com.google.transaction.api.dto.PayChannelParamDTO;
import com.google.transaction.domain.PayChannelParam;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author sohyun
 * @date 2020/11/20 21:01
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PayChannelParamConvert extends BaseConvert<PayChannelParamDTO,PayChannelParam> {
}
