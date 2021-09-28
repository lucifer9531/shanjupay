package com.google.merchant.service;

import com.google.domain.BusinessException;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author sohyun
 * @date 2020/11/18 21:16
 */
public interface FileService {
    String upload(MultipartFile multipartFile) throws BusinessException;
}
