package com.google.merchant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.domain.BusinessException;
import com.google.domain.CommonErrorCode;
import com.google.merchant.api.AppService;
import com.google.merchant.api.dto.AppDTO;
import com.google.merchant.domain.App;
import com.google.merchant.domain.Merchant;
import com.google.merchant.mapper.AppMapper;
import com.google.merchant.mapper.MerchantMapper;
import com.google.merchant.mapstruct.AppConvert;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author sohyun
 * @date 2020/11/19 20:18
 */
@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {

    private final AppMapper appMapper;
    private final MerchantMapper merchantMapper;
    private final AppConvert appConvert;
    @Override
    public AppDTO createApp(Long merchantId, AppDTO appDTO) throws BusinessException {

        if (merchantId == null || appDTO == null || StringUtils.isBlank(appDTO.getAppName())) {
            throw new BusinessException(CommonErrorCode.E_300009);
        }
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new BusinessException(CommonErrorCode.E_200002);
        }
        String auditStatus = merchant.getAuditStatus();
        if (!"2".equals(auditStatus)) {
            throw new BusinessException(CommonErrorCode.E_200003);
        }
        String appName = appDTO.getAppName();
        Boolean existAppName = isExistAppName(appName);
        if (existAppName) {
            throw new BusinessException(CommonErrorCode.E_200004);
        }
        String appId = UUID.randomUUID().toString();
        App entity = appConvert.toEntity(appDTO);
        entity.setAppId(appId);
        entity.setMerchantId(merchantId);

        appMapper.insert(entity);
        return appConvert.toDto(entity);
    }

    @Override
    public List<AppDTO> queryAppByMerchant(Long merchantId) throws BusinessException {
        List<App> apps = appMapper.selectList(new LambdaQueryWrapper<App>()
                .eq(App::getMerchantId, merchantId));
        return appConvert.toDto(apps);
    }

    @Override
    public AppDTO getAppById(String appId) throws BusinessException {
        App app = appMapper.selectOne(new LambdaQueryWrapper<App>()
                .eq(App::getAppId, appId));
        return appConvert.toDto(app);
    }

    private Boolean isExistAppName(String appName) {
        Integer count = appMapper.selectCount(new LambdaQueryWrapper<App>()
                .eq(App::getAppName, appName));
        return count > 0;
    }
}
