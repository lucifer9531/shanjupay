package com.google.merchant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.domain.BusinessException;
import com.google.domain.CommonErrorCode;
import com.google.merchant.api.MerchantService;
import com.google.merchant.api.dto.MerchantDTO;
import com.google.merchant.api.dto.StaffDTO;
import com.google.merchant.api.dto.StoreDTO;
import com.google.merchant.domain.Merchant;
import com.google.merchant.domain.Staff;
import com.google.merchant.domain.Store;
import com.google.merchant.domain.StoreStaff;
import com.google.merchant.mapper.MerchantMapper;
import com.google.merchant.mapper.StaffMapper;
import com.google.merchant.mapper.StoreMapper;
import com.google.merchant.mapper.StoreStaffMapper;
import com.google.merchant.mapstruct.MerchantConvert;
import com.google.merchant.mapstruct.StaffConvert;
import com.google.merchant.mapstruct.StoreConvert;
import com.google.user.api.TenantService;
import com.google.user.api.dto.tenant.CreateTenantRequestDTO;
import com.google.user.api.dto.tenant.TenantDTO;
import com.google.util.PhoneUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author sohyun
 * @date 2020/11/16 00:07
 */
@org.apache.dubbo.config.annotation.Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final MerchantMapper merchantMapper;
    private final StoreMapper storeMapper;
    private final StaffMapper staffMapper;
    private final StoreStaffMapper storeStaffMapper;
    private final MerchantConvert merchantConvert;
    private final StoreConvert storeConvert;
    private final StaffConvert staffConvert;

    @Reference
    TenantService tenantService;

    @Override
    public MerchantDTO queryMerchantById(Long merchantId) {
        return merchantConvert.toDto(merchantMapper.selectById(merchantId));
    }

    @Override
    public MerchantDTO queryMerchantByTenantId(Long tenantId) {
        Merchant merchant = merchantMapper.selectOne(new LambdaQueryWrapper<Merchant>()
                .eq(Merchant::getTenantId, tenantId));
        return merchantConvert.toDto(merchant);
    }

    /**
     * 注册商户服务接口，接收账号、密码、手机号，为了可扩展性使用merchantDto接收数据
     * 调用SaaS接口：新增租户、用户、绑定租户和用户的关系，初始化权限
     * @param merchantDTO 商户注册信息
     * @return 注册成功的商户信息
     */
    @Override
    public MerchantDTO createMerchant(MerchantDTO merchantDTO) {
        //校验参数的合法性
        if (merchantDTO == null) {
            throw new BusinessException(CommonErrorCode.E_100108);
        }
        if (StringUtils.isBlank(merchantDTO.getMobile())) {
            throw new BusinessException(CommonErrorCode.E_100112);
        }
        if (StringUtils.isBlank(merchantDTO.getPassword())) {
            throw new BusinessException(CommonErrorCode.E_100111);
        }
        //手机号格式校验
        if (!PhoneUtil.isMatches(merchantDTO.getMobile())) {
            throw new BusinessException(CommonErrorCode.E_100109);
        }
        //校验手机号的唯一性
        //根据手机号查询商户表，如果存在记录则说明手机号已存在
        Integer count = merchantMapper.selectCount(new LambdaQueryWrapper<Merchant>()
                .eq(Merchant::getMobile, merchantDTO.getMobile()));
        if (count > 0) {
            throw new BusinessException(CommonErrorCode.E_100113);
        }
        CreateTenantRequestDTO createTenantRequestDTO = new CreateTenantRequestDTO();
        createTenantRequestDTO.setMobile(merchantDTO.getMobile());
        createTenantRequestDTO.setUsername(merchantDTO.getUsername());
        createTenantRequestDTO.setPassword(merchantDTO.getPassword());
        createTenantRequestDTO.setTenantTypeCode("shanju-merchant");
        createTenantRequestDTO.setBundleCode("shanju-merchant");
        createTenantRequestDTO.setName(merchantDTO.getUsername());

        //如果租户在SaaS已经存在，SaaS直接 返回此租户的信息，否则进行添加
        TenantDTO tenantAndAccount = tenantService.createTenantAndAccount(createTenantRequestDTO);

        //获取租户的id
        if (tenantAndAccount == null || tenantAndAccount.getId() == null) {
            throw new BusinessException(CommonErrorCode.E_200012);
        }
        //租户的id
        Long tenantId = tenantAndAccount.getId();

        //租户id在商户表唯一
        //根据租户id从商户表查询，如果存在记录则不允许添加商户
        Integer count1 = merchantMapper.selectCount(new LambdaQueryWrapper<Merchant>()
                .eq(Merchant::getTenantId, tenantId));
        if (count1 > 0) {
            throw new BusinessException(CommonErrorCode.E_200017);
        }

        Merchant merchant = merchantConvert.toEntity(merchantDTO);
        //设置所对应的租户的Id
        merchant.setTenantId(tenantId);
        // 审核状态为0 未进行资质申请
        merchant.setAuditStatus("0");
        merchantMapper.insert(merchant);

        //新增门店
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setStoreName("根门店");
        storeDTO.setMerchantId(merchant.getId());
        StoreDTO store = createStore(storeDTO);

        //新增员工
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setMobile(merchantDTO.getMobile());
        staffDTO.setUsername(merchantDTO.getUsername());
        staffDTO.setStoreId(store.getId());
        staffDTO.setMerchantId(merchant.getId());

        StaffDTO staff = createStaff(staffDTO);

        //为门店设置管理员
        bindStaffToStore(store.getId(),staff.getId());
        return merchantConvert.toDto(merchant);
    }

    @Override
    @Transactional
    public void applyMerchant(Long merchantId, MerchantDTO merchantDTO) throws BusinessException {
        if (merchantId == null || merchantDTO == null) {
            throw new BusinessException(CommonErrorCode.E_300009);
        }
        // 校验商户ID的合法性
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new BusinessException(CommonErrorCode.E_200002);
        }
        Merchant entity = merchantConvert.toEntity(merchantDTO);
        entity.setId(merchant.getId());
        entity.setMobile(merchant.getMobile());
        entity.setAuditStatus("1"); // 已经申请 待审核
        entity.setTenantId(merchant.getTenantId());

        merchantMapper.updateById(entity);
    }

    @Override
    @Transactional
    public StoreDTO createStore(StoreDTO storeDTO) throws BusinessException {

        Store entity = storeConvert.toEntity(storeDTO);
        storeMapper.insert(entity);
        return storeConvert.toDto(entity);
    }

    @Override
    public StaffDTO createStaff(StaffDTO staffDTO) throws BusinessException {
        //参数合法性校验
        if (staffDTO == null || StringUtils.isBlank(staffDTO.getMobile())
                || StringUtils.isBlank(staffDTO.getUsername())
                || staffDTO.getStoreId() == null) {
            throw new BusinessException(CommonErrorCode.E_300009);
        }
        //在同一个商户下员工的账号唯一
        Boolean existStaffByUserName = isExistStaffByUserName(staffDTO.getUsername(), staffDTO.getMerchantId());
        if (existStaffByUserName) {
            throw new BusinessException(CommonErrorCode.E_100114);
        }
        //在同一个商户下员工的手机号唯一
        Boolean existStaffByMobile = isExistStaffByMobile(staffDTO.getMobile(), staffDTO.getMerchantId());
        if (existStaffByMobile) {
            throw new BusinessException(CommonErrorCode.E_100113);
        }
        Staff staff = staffConvert.toEntity(staffDTO);
        staffMapper.insert(staff);
        return staffConvert.toDto(staff);
    }

    @Override
    public void bindStaffToStore(Long storeId, Long staffId) throws BusinessException {
        StoreStaff storeStaff = new StoreStaff();
        storeStaff.setStaffId(staffId);
        storeStaff.setStoreId(storeId);
        storeStaffMapper.insert(storeStaff);
    }

    /**
     * 员工手机号在同一个商户下是唯一校验
     *
     * @param mobile
     * @param merchantId
     * @return
     */
    private Boolean isExistStaffByMobile(String mobile, Long merchantId) {
        Integer count = staffMapper.selectCount(new LambdaQueryWrapper<Staff>()
                .eq(Staff::getMobile, mobile)
                .eq(Staff::getMerchantId, merchantId));
        return count > 0;
    }

    /**
     * 员工账号在同一个商户下是唯一校验
     *
     * @param username
     * @param merchantId
     * @return
     */
    private Boolean isExistStaffByUserName(String username, Long merchantId) {
        Integer count = staffMapper.selectCount(new LambdaQueryWrapper<Staff>()
                .eq(Staff::getUsername, username)
                .eq(Staff::getMerchantId, merchantId));
        return count > 0;
    }
}
