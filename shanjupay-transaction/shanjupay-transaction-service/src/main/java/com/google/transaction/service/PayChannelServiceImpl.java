package com.google.transaction.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.client.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.google.cache.Cache;
import com.google.domain.BusinessException;
import com.google.domain.CommonErrorCode;
import com.google.transaction.api.PayChannelService;
import com.google.transaction.api.dto.PayChannelDTO;
import com.google.transaction.api.dto.PayChannelParamDTO;
import com.google.transaction.api.dto.PlatformChannelDTO;
import com.google.transaction.convert.PayChannelParamConvert;
import com.google.transaction.convert.PlatformChannelConvert;
import com.google.transaction.domain.AppPlatformChannel;
import com.google.transaction.domain.PayChannelParam;
import com.google.transaction.mapper.AppPlatformChannelMapper;
import com.google.transaction.mapper.PayChannelParamMapper;
import com.google.transaction.mapper.PlatformChannelMapper;
import com.google.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author sohyun
 * @date 2020/11/19 22:51
 */
@Service
@RequiredArgsConstructor
public class PayChannelServiceImpl implements PayChannelService {

    private final Cache cache;
    private final PlatformChannelMapper platformChannelMapper;
    private final AppPlatformChannelMapper appPlatformChannelMapper;
    private final PayChannelParamMapper payChannelParamMapper;
    private final PlatformChannelConvert platformChannelConvert;
    private final PayChannelParamConvert payChannelParamConvert;

    @Override
    public List<PlatformChannelDTO> queryPlatformChannel() throws BusinessException {
        return platformChannelConvert.toDto(platformChannelMapper.selectList(null));
    }

    @Override
    @Transactional
    public void bindPlatformChannelForApp(String appId, String platformChannelCode) throws BusinessException {
        AppPlatformChannel appPlatformChannel = appPlatformChannelMapper.selectOne(new LambdaQueryWrapper<AppPlatformChannel>()
                .eq(AppPlatformChannel::getId, appId)
                .eq(AppPlatformChannel::getPlatformChannel, platformChannelCode));
        if (appPlatformChannel == null) {
            AppPlatformChannel entity = new AppPlatformChannel();
            entity.setAppId(appId);
            entity.setPlatformChannel(platformChannelCode);
            appPlatformChannelMapper.insert(entity);
        }
    }

    @Override
    public int queryAppBindPlatformChannel(String appId, String platformChannel) throws BusinessException {
        AppPlatformChannel appPlatformChannel = appPlatformChannelMapper.selectOne(new LambdaQueryWrapper<AppPlatformChannel>()
                .eq(AppPlatformChannel::getId, appId)
                .eq(AppPlatformChannel::getPlatformChannel, platformChannel));
        if (appPlatformChannel != null) {
            return 1;
        }
        return 0;
    }

    @Override
    public List<PayChannelDTO> queryPayChannelByPlatformChannel(String platformChannelCode) throws BusinessException {
        return platformChannelMapper.selectPayChannelByPlatformChannel(platformChannelCode);
    }

    @Override
    public void savePayChannelParam(PayChannelParamDTO payChannelParamDTO) throws BusinessException {
        if (payChannelParamDTO == null || StringUtils.isBlank(payChannelParamDTO.getAppId()) ||
                StringUtils.isBlank(payChannelParamDTO.getPlatformChannelCode()) ||
                StringUtils.isBlank(payChannelParamDTO.getPayChannel())) {
            throw new BusinessException(CommonErrorCode.E_300009);
        }
        // 根据appid和服务类型查询应用与服务类型绑定id
        Long appPlatformChannelId = selectIdByAppPlatformChannel(payChannelParamDTO.getAppId(), payChannelParamDTO.getPlatformChannelCode());
        if (appPlatformChannelId == null) {
            // 应用未绑定该服务类型不可进行支付渠道参数配置
            throw new BusinessException(CommonErrorCode.E_300010);
        }
        // 根据应用与服务类型绑定id和支付渠道查询参数信息
        PayChannelParam payChannelParam = payChannelParamMapper.selectOne(
                new LambdaQueryWrapper<PayChannelParam>()
                        .eq(PayChannelParam::getAppPlatformChannelId, appPlatformChannelId)
                        .eq(PayChannelParam::getPayChannel, payChannelParamDTO.getPayChannel()));
        // 更新已有配置
        if (payChannelParam != null) {
            payChannelParam.setChannelName(payChannelParamDTO.getChannelName());
            payChannelParam.setParam(payChannelParamDTO.getParam());
            payChannelParamMapper.updateById(payChannelParam);
        } else {
            // 添加新配置
            PayChannelParam entity = payChannelParamConvert.toEntity(payChannelParamDTO);
            entity.setId(null);
            // 应用与服务类型绑定id
            entity.setAppPlatformChannelId(appPlatformChannelId);
            payChannelParamMapper.insert(entity);
        }
        updateCache(payChannelParamDTO.getAppId(), payChannelParamDTO.getPlatformChannelCode());
    }

    private void updateCache(String appId, String platformChannel) {
        // 1.key的构建 如：SJ_PAY_PARAM:b910da455bc84514b324656e1088320b:shanju_c2b
        String redisKey = RedisUtil.keyBuilder(appId, platformChannel);
        // 2. 查询redis,检查key是否存在
        Boolean exists = cache.exists(redisKey);
        if (exists) {
            cache.del(redisKey);
        }
        // 3.从数据库查询应用的服务类型对应的实际支付参数，并重新存入缓存
        Long appPlatformChannelId = selectIdByAppPlatformChannel(appId, platformChannel);
        if(appPlatformChannelId != null) {
            List<PayChannelParam> payChannelParams = payChannelParamMapper.selectList(new LambdaQueryWrapper<PayChannelParam>()
                    .eq(PayChannelParam::getAppPlatformChannelId, appPlatformChannelId));
            List<PayChannelParamDTO> payChannelParamDTOS = payChannelParamConvert.toDto(payChannelParams);
            // 将payChannelParamDTOS转成json串存入redis
            cache.set(redisKey, JSON.toJSON(payChannelParamDTOS).toString());
        }
    }

    @Override
    public List<PayChannelParamDTO> queryPayChannelParamByAppAndPlatform(String appId, String platformChannel) throws BusinessException {
        // 从缓存查询
        String redisKey = RedisUtil.keyBuilder(appId, platformChannel);
        Boolean exists = cache.exists(redisKey);
        if(exists) {
            String PayChannelParamDTO_String = cache.get(redisKey);
            List<PayChannelParamDTO> payChannelParamDTOS = JSON.parseArray(PayChannelParamDTO_String, PayChannelParamDTO.class);
            return payChannelParamDTOS;
        }
        Long appPlatformChannelId = selectIdByAppPlatformChannel(appId, platformChannel);
        if (appPlatformChannelId == null) {
            return null;
        }
        List<PayChannelParam> payChannelParams = payChannelParamMapper.selectList(
                new LambdaQueryWrapper<PayChannelParam>()
                        .eq(PayChannelParam::getAppPlatformChannelId, appPlatformChannelId));
        List<PayChannelParamDTO> payChannelParamDTOS = payChannelParamConvert.toDto(payChannelParams);
        updateCache(appId, platformChannel);
        return payChannelParamDTOS;
    }

    @Override
    public PayChannelParamDTO queryParamByAppPlatformAndPayChannel(String appId, String platformChannel, String payChannel) throws BusinessException {
        List<PayChannelParamDTO> payChannelParamDTOS = queryPayChannelParamByAppAndPlatform(appId, platformChannel);
        for (PayChannelParamDTO payChannelParamDTO : payChannelParamDTOS) {
            if (payChannelParamDTO.getPayChannel().equals(payChannel)) {
                return payChannelParamDTO;
            }
        }
        return null;
    }

    /**
     * 根据appid和服务类型查询应用与服务类型绑定id
     *
     * @param appId
     * @param platformChannelCode
     * @return
     */
    private Long selectIdByAppPlatformChannel(String appId, String platformChannelCode) {
        AppPlatformChannel appPlatformChannel = appPlatformChannelMapper.selectOne(new
                LambdaQueryWrapper<AppPlatformChannel>()
                .eq(AppPlatformChannel::getAppId, appId)
                .eq(AppPlatformChannel::getPlatformChannel, platformChannelCode));
        if (appPlatformChannel != null) {
            return appPlatformChannel.getId();
        }
        return null;
    }
}
