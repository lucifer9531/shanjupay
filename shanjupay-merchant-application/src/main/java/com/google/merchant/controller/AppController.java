package com.google.merchant.controller;

import com.google.merchant.api.AppService;
import com.google.merchant.api.dto.AppDTO;
import com.google.merchant.common.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sohyun
 * @date 2020/11/19 20:32
 */
@RestController
@Api(tags = "商户平台-应用管理")
public class AppController {

    @Reference
    AppService appService;

    @PostMapping("/my/apps")
    @ApiOperation("商户创建应用")
    public AppDTO createApp(@RequestBody AppDTO appDTO) {
        Long merchantId = SecurityUtil.getMerchantId();
        return appService.createApp(merchantId, appDTO);
    }

    @GetMapping("/my/apps")
    @ApiOperation("查询商户下的应用列表")
    public List<AppDTO> queryMyApps() {
        Long merchantId = SecurityUtil.getMerchantId();
        return appService.queryAppByMerchant(merchantId);
    }

    @GetMapping("/my/apps/{appId}")
    @ApiOperation("根据ID查询应用信息")
    public AppDTO getApp(@PathVariable("appId") String appId) {
        return appService.getAppById(appId);
    }
}
