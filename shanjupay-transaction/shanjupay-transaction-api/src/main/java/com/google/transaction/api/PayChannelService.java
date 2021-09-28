package com.google.transaction.api;

import com.google.domain.BusinessException;
import com.google.transaction.api.dto.PayChannelDTO;
import com.google.transaction.api.dto.PayChannelParamDTO;
import com.google.transaction.api.dto.PlatformChannelDTO;

import java.util.List;

/**
 * @author sohyun
 * @date 2020/11/19 22:48
 */
public interface PayChannelService {

    /**
     * 查询平台服务类型
     * @return
     * @throws BusinessException
     */
    List<PlatformChannelDTO> queryPlatformChannel() throws BusinessException;

    /**
     * 为应用绑定一个服务类型
     * @param appId
     * @param platformChannelCode
     * @throws BusinessException
     */
    void bindPlatformChannelForApp(String appId, String platformChannelCode) throws BusinessException;

    /**
     * 查询应用绑定状态
     * @param appId
     * @param platformChannel
     * @return
     * @throws BusinessException
     */
    int queryAppBindPlatformChannel(String appId,String platformChannel) throws BusinessException;

    /**
     * 根据服务类型查询支付渠道
     * @param platformChannelCode
     * @return
     * @throws BusinessException
     */
    List<PayChannelDTO> queryPayChannelByPlatformChannel(String platformChannelCode) throws BusinessException;

    /**
     * 保存支付渠道参数
     * @param payChannelParamDTO
     * @throws BusinessException
     */
    void savePayChannelParam(PayChannelParamDTO payChannelParamDTO) throws BusinessException;

    /**
     * 获取指定应用指定服务类型下所包含的原始支付渠道参数列表
     * @param appId
     * @param platformChannel
     * @return
     * @throws BusinessException
     */
    List<PayChannelParamDTO> queryPayChannelParamByAppAndPlatform(String appId, String platformChannel) throws BusinessException;

    /**
     * 获取指定应用指定服务类型下所包含的某个原始支付参数
     * @param appId
     * @param platformChannel
     * @param payChannel
     * @return
     * @throws BusinessException
     */
    PayChannelParamDTO queryParamByAppPlatformAndPayChannel(String appId, String platformChannel, String payChannel) throws BusinessException;
}
