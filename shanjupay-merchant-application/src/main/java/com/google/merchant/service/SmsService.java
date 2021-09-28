package com.google.merchant.service;

import com.google.domain.BusinessException;

public interface SmsService {

    /**
     * 发送手机验证码
     * @param phone
     * @return
     */
    String getSmSCode(String phone) throws BusinessException;

    void checkVerifiyCode(String verifiyKey, String verifiyCode) throws BusinessException;
}
