package com.google.merchant.controller;

import com.google.merchant.common.util.SecurityUtil;
import com.google.transaction.api.PayChannelService;
import com.google.transaction.api.dto.PayChannelDTO;
import com.google.transaction.api.dto.PayChannelParamDTO;
import com.google.transaction.api.dto.PlatformChannelDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sohyun
 * @date 2020/11/19 22:58
 */
@RestController
@Api(tags = "商户平台-渠道和支付参数")
public class PlatformParamController {

    @Reference
    PayChannelService payChannelService;

    @GetMapping("/my/platform-channels")
    @ApiOperation("获取平台服务类型")
    public List<PlatformChannelDTO> queryPlatformChannel() {
        return payChannelService.queryPlatformChannel();
    }

    @PostMapping("/my/apps/{appId}/platform-channels")
    @ApiOperation("为应用绑定服务类型")
    public void bindPlatformChannelForApp(@PathVariable("appId") String appId, @RequestParam("platformChannelCode") String platformChannelCode) {
        payChannelService.bindPlatformChannelForApp(appId, platformChannelCode);
    }

    @ApiOperation("查询应用是否绑定了某个服务类型")
    @GetMapping("/my/merchants/apps/platformchannels")
    public int queryAppBindPlatformChannel(@RequestParam("appId") String appId, @RequestParam("platformChannel") String platformChannel) {
        return payChannelService.queryAppBindPlatformChannel(appId, platformChannel);
    }

    @ApiOperation("根据服务类型查询支付渠道")
    @GetMapping(value = "/my/pay‐channels/platform‐channel/{platformChannelCode}")
    public List<PayChannelDTO> queryPayChannelByPlatformChannel(String platformChannelCode) {
        return payChannelService.queryPayChannelByPlatformChannel(platformChannelCode);
    }

    @ApiOperation("商户配置支付渠道参数")
    @RequestMapping(value = "/my/pay‐channel‐params", method = {RequestMethod.POST, RequestMethod.PUT})
    public void createPayChannelParam(@RequestBody PayChannelParamDTO payChannelParam) {
        Long merchantId = SecurityUtil.getMerchantId();
        payChannelParam.setMerchantId(merchantId);
        payChannelService.savePayChannelParam(payChannelParam);
    }

    @ApiOperation("获取指定应用指定服务类型下所包含的原始支付渠道参数列表")
    @GetMapping("/my/pay‐channel‐params/apps/{appId}/platform‐channels/{platformChannel}")
    public List<PayChannelParamDTO> queryPayChannelParam(@PathVariable String appId, @PathVariable String platformChannel) {
        return payChannelService.queryPayChannelParamByAppAndPlatform(appId, platformChannel);
    }

    @ApiOperation("获取指定应用指定服务类型下所包含的某个原始支付参数")
    @GetMapping("/my/pay‐channel‐params/apps/{appId}/platform‐channels/{platformChannel}/pay‐channels/{payChannel}")
    public PayChannelParamDTO queryPayChannelParam(
            @PathVariable String appId,
            @PathVariable String platformChannel,
            @PathVariable String payChannel) {
        return payChannelService.queryParamByAppPlatformAndPayChannel(appId, platformChannel, payChannel);
    }
}
