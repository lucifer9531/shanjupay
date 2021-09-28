package com.google.merchant.service.impl;

import com.google.domain.BusinessException;
import com.google.domain.CommonErrorCode;
import com.google.merchant.config.MinioProperties;
import com.google.merchant.service.FileService;
import io.minio.MinioClient;
import io.minio.policy.PolicyType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author sohyun
 * @date 2020/11/18 21:20
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MinioProperties minioProperties;
    @Override
    public String upload(MultipartFile multipartFile) throws BusinessException {
        String fileName = multipartFile.getOriginalFilename();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            MinioClient minioClient = new MinioClient(minioProperties.getEndpoint(), minioProperties.getAccessKey(), minioProperties.getSecretKey());
            boolean isExist = minioClient.bucketExists(minioProperties.getBucketName());
            if (!isExist) {
                minioClient.makeBucket(minioProperties.getBucketName());
                minioClient.setBucketPolicy(minioProperties.getBucketName(), "*.*", PolicyType.READ_ONLY);
            }
            String name = format.format(new Date()) + File.separator + fileName;
            minioClient.putObject(minioProperties.getBucketName(), name, multipartFile.getInputStream(), multipartFile.getContentType());
            return minioProperties.getEndpoint() + File.separator + minioProperties.getBucketName() + File.separator + name;
        } catch (Exception e) {
            throw new BusinessException(CommonErrorCode.E_100106);
        }
    }
}
