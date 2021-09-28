package com.google.merchant.api;

import com.google.domain.BusinessException;
import com.google.merchant.api.dto.MerchantDTO;
import com.google.merchant.api.dto.StaffDTO;
import com.google.merchant.api.dto.StoreDTO;

/**
 * @author sohyun
 * @date 2020/11/16 00:06
 **/
public interface MerchantService {
    /**
     * 根据ID查询详细信息
     * @param merchantId
     * @return
     */
    MerchantDTO queryMerchantById(Long merchantId);

    /**
     * 根据id查询商户的信息
     * @param tenantId
     * @return
     */
    MerchantDTO queryMerchantByTenantId(Long tenantId);

    /**
     * 商户注册
     * @param merchantDTO
     * @return
     */
    MerchantDTO createMerchant(MerchantDTO merchantDTO);

    /**
     * 资质申请
     * @param merchantId
     * @param merchantDTO
     */
    void applyMerchant(Long merchantId, MerchantDTO merchantDTO) throws BusinessException;

    /**
     * 新增门店
     * @param storeDTO
     * @return
     * @throws BusinessException
     */
    StoreDTO createStore(StoreDTO storeDTO) throws BusinessException;

    /**
     * 新增员工
     * @param staffDTO
     * @return
     * @throws BusinessException
     */
    StaffDTO createStaff(StaffDTO staffDTO) throws BusinessException;

    /**
     * 讲员工设置为门店的管理员
     * @param storeId
     * @param staffId
     * @throws BusinessException
     */
    void bindStaffToStore(Long storeId, Long staffId) throws BusinessException;
}
