package com.google.user.mapstruct;

import com.google.base.BaseConvert;
import com.google.user.api.dto.tenant.AccountDTO;
import com.google.user.domain.Account;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountConvert extends BaseConvert<AccountDTO, Account> {
}
