package com.google.merchant.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.domain.BusinessException;
import com.google.domain.CommonErrorCode;
import com.google.merchant.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sohyun
 * @date 2020/11/16 22:11
 */

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    private final RestTemplate restTemplate;
    @Value("${sms.url}")
    private String url;
    @Value(("${sms.effectiveTime}"))
    private String effectiveTime;

    @Override
    public String getSmSCode(String phone) throws BusinessException {
        String sms_url = url + "/sailing/generate?name=sms&effectiveTime=" + effectiveTime;

        // 请求体
        Map<String, Object> body = new HashMap<>();
        body.put("mobile", phone);
        // 请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        // 请求信息
        HttpEntity httpEntity = new HttpEntity(body, httpHeaders);
        ResponseEntity<Map> exchange;
        Map bodyMap;
        try {
            exchange = restTemplate.exchange(sms_url, HttpMethod.POST, httpEntity, Map.class);
            bodyMap = exchange.getBody();
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorCode.E_100107);
        }
        if (bodyMap == null || bodyMap.get("result") == null) {
            throw new BusinessException(CommonErrorCode.E_100107);
        }

        Map result = (Map) bodyMap.get("result");
        return result.get("key").toString();
    }

    @Override
    public void checkVerifiyCode(String verifiyKey, String verifiyCode) throws BusinessException {
        // 检验验证码的url
        String verify_url = url + "/sailing/verify?name=sms&verificationCode=" + verifiyCode + "&verificationKey=" + verifiyKey;

        Map bodyMap;
        try {
            // 使用restTemplate请求验证码服务
            ResponseEntity<Map> exchange = restTemplate.exchange(verify_url, HttpMethod.POST, HttpEntity.EMPTY, Map.class);
            bodyMap = exchange.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorCode.E_100102);

        }
        if (bodyMap == null || bodyMap.get("result") == null || !(Boolean) bodyMap.get("result")) {
            throw new BusinessException(CommonErrorCode.E_100102);
        }
    }
}
