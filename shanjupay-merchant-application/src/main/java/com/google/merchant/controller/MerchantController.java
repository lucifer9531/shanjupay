package com.google.merchant.controller;

import com.google.merchant.api.MerchantService;
import com.google.merchant.api.dto.MerchantDTO;
import com.google.merchant.common.util.SecurityUtil;
import com.google.merchant.mapstruct.MerchantDetailConvert;
import com.google.merchant.mapstruct.MerchantRegisterConvert;
import com.google.merchant.service.FileService;
import com.google.merchant.service.SmsService;
import com.google.merchant.vo.MerchantDetailVO;
import com.google.merchant.vo.MerchantRegisterVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author sohyun
 * @date 2020/11/16 00:13
 */
@RestController
@Api(tags = "商户平台：商户管理")
@RequiredArgsConstructor
public class MerchantController {

    @Reference
    MerchantService merchantService;

    private final SmsService smsService;
    private final FileService fileService;
    private final MerchantRegisterConvert merchantRegisterConvert;
    private final MerchantDetailConvert merchantDetailConvert;

    @GetMapping("/merchants/{id}")
    @ApiOperation("根据Id查询商户信息")
    public MerchantDTO queryMerchantById(@PathVariable("id") Long id) {
        MerchantDTO merchantDTO = merchantService.queryMerchantById(id);
        return merchantDTO;
    }

    @GetMapping("/my/merchants")
    @ApiOperation("获取登录用户的商品信息")
    public MerchantDTO getMyMerchantInfo() {
        Long merchantId = SecurityUtil.getMerchantId();
        return merchantService.queryMerchantById(merchantId);
    }

    @ApiOperation("获取手机验证码")
    @GetMapping("/sms")
    public String getSMSCode(@RequestParam("phone") String phone) {
        return smsService.getSmSCode(phone);
    }

    @ApiOperation("商户注册")
    @PostMapping("/merchants/register")
    public MerchantRegisterVO createMerant(@RequestBody MerchantRegisterVO merchantRegisterVO) {
        smsService.checkVerifiyCode(merchantRegisterVO.getVerifiykey(), merchantRegisterVO.getVerifiyCode());
        //注册商户
        MerchantDTO merchantDTO = merchantRegisterConvert.toDto(merchantRegisterVO);
        merchantService.createMerchant(merchantDTO);
        return merchantRegisterVO;
    }

    @ApiOperation("上传证件照")
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile multipartFile) {
        return fileService.upload(multipartFile);
    }

    @PostMapping("/my/merchants/save")
    @ApiOperation("资质申请")
    public void saveMerchant(@RequestBody MerchantDetailVO merchantDetailVO) {
        // 解析token
        Long merchantId = SecurityUtil.getMerchantId();
        merchantService.applyMerchant(merchantId, merchantDetailConvert.toDto(merchantDetailVO));
    }
}
