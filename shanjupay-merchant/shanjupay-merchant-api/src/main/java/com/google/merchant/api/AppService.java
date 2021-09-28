package com.google.merchant.api;

import com.google.domain.BusinessException;
import com.google.merchant.api.dto.AppDTO;

import java.util.List;

public interface AppService {

    /**
     * 创建应用
     * @param merchantId
     * @param appDTO
     * @return
     */
    AppDTO createApp(Long merchantId, AppDTO appDTO) throws BusinessException;

    /**
     * 根据商户id查询应用列表
     * @param merchantId
     * @return
     * @throws BusinessException
     */
    List<AppDTO> queryAppByMerchant(Long merchantId) throws BusinessException;

    /**
     * 根据appId查询应用
     * @param appId
     * @return
     * @throws BusinessException
     */
    AppDTO getAppById(String appId) throws BusinessException;
}
