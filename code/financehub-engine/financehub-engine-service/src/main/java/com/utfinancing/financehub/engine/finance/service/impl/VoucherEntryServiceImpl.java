package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.BatchTypeEnum;
import com.utfinancing.financehub.engine.enums.ProcessStatusEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.AssetAbsRedeemEntity;
import com.utfinancing.financehub.engine.finance.entity.OutTableAbsEntity;
import com.utfinancing.financehub.engine.finance.entity.VoucherEntity;
import com.utfinancing.financehub.engine.finance.mapper.AssetAbsRedeemMapper;
import com.utfinancing.financehub.engine.finance.mapper.OutTableAbsMapper;
import com.utfinancing.financehub.engine.finance.mapper.VoucherMapper;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherEntryQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherEntryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.VoucherEntryVO;
import com.utfinancing.financehub.engine.finance.entity.VoucherEntryEntity;
import com.utfinancing.financehub.engine.finance.mapper.VoucherEntryMapper;
import com.utfinancing.financehub.engine.finance.service.IVoucherEntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-09-01
 * @Description :  VoucherEntry服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class VoucherEntryServiceImpl extends ServiceImpl<VoucherEntryMapper, VoucherEntryEntity> implements IVoucherEntryService {

    private final VoucherEntryMapper voucherEntryMapper;
    private final VoucherMapper voucherMapper;
    private final OutTableAbsMapper outTableAbsMapper;
    private final AssetAbsRedeemMapper assetAbsRedeemMapper;
    @Override
    public Long saveVoucherEntry(VoucherEntryDTO dto) {
        VoucherEntryEntity entity = BeanUtil.copyProperties(dto, VoucherEntryEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateVoucherEntry(Long id, VoucherEntryDTO dto) {
        VoucherEntryEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public VoucherEntryDTO getVoucherEntryDTOById(Long id) {
        VoucherEntryEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, VoucherEntryDTO.class);
    }

    @Override
    public IPage<VoucherEntryVO> selectPage(VoucherEntryQueryDTO queryDTO) {
        LambdaQueryWrapper<VoucherEntryEntity> queryWrapper = Wrappers.<VoucherEntryEntity>lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(queryDTO.getContractName()), VoucherEntryEntity::getContractName, queryDTO.getContractName());
        queryWrapper.like(StrUtil.isNotBlank(queryDTO.getClientName()), VoucherEntryEntity::getClientName, queryDTO.getClientName());
        queryWrapper.orderByDesc(VoucherEntryEntity::getCreateTime);
        IPage<VoucherEntryEntity> entityIPage = voucherEntryMapper.selectPage(new Page<VoucherEntryEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, VoucherEntryVO.class);
    }

    @Override
    public List<VoucherEntryVO> selectCourtCostByCondition(VoucherEntryQueryDTO queryDTO) {
        return voucherEntryMapper.selectCourtCostByCondition(queryDTO);
    }

    /**
     * 凭证分录取得
     */
    public List<VoucherEntryEntity> selectByVoucherId(Long voucherId) {
        LambdaQueryWrapper<VoucherEntryEntity> wrapper = new LambdaQueryWrapper();
        wrapper.eq(VoucherEntryEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        wrapper.eq(VoucherEntryEntity::getVoucherId, voucherId);
        return voucherEntryMapper.selectList(wrapper);
    }

    @Override
    public Long modifyVoucherEntry(Long id, VoucherEntryDTO dto) {
        VoucherEntryEntity entity = this.getById(id);
        if (YesOrNoEnum.NO.getCode().equals(entity.getEditFlag()) || !isEditFlag(entity.getVoucherId())) {
            throw new ServiceException("该条记录不可以修改");
        };
        entity.setAccountCode(dto.getAccountCode());
        entity.setAccountName(dto.getAccountName());
        entity.setBankAccount(dto.getBankAccount());
        entity.updateById();
        return id;
    }

    public Boolean isEditFlag(Long voucherId){
        //获取凭证信息
        VoucherEntity voucherEntity = voucherMapper.selectById(voucherId);
        //根据batchId，batchType判断调用那个mapper
        //校验数据状态如果状态为已提交，已复核，已发送金蝶则不可以修改
        if (BatchTypeEnum.CBABS.getCode().equals(voucherEntity.getBatchType())) {
            OutTableAbsEntity outTableAbsEntity = outTableAbsMapper.selectById(voucherEntity.getBatchId());
            if (null == outTableAbsEntity
                    || ProcessStatusEnum.ENTERED.getCode().equals(outTableAbsEntity.getProcessStatus())
                    || ProcessStatusEnum.REVIEWED.getCode().equals(outTableAbsEntity.getProcessStatus())
                    || ProcessStatusEnum.TO_KINGDEE.getCode().equals(outTableAbsEntity.getProcessStatus())
            ) {
              return Boolean.FALSE;
            }
        } else if (BatchTypeEnum.ABSSH.getCode().equals(voucherEntity.getBatchType())) {
            AssetAbsRedeemEntity assetAbsRedeemEntity = assetAbsRedeemMapper.selectById(voucherEntity.getBatchId());
            if (null == assetAbsRedeemEntity
                    || ProcessStatusEnum.ENTERED.getCode().equals(assetAbsRedeemEntity.getProcessStatus())
                    || ProcessStatusEnum.REVIEWED.getCode().equals(assetAbsRedeemEntity.getProcessStatus())
                    || ProcessStatusEnum.TO_KINGDEE.getCode().equals(assetAbsRedeemEntity.getProcessStatus())
            ) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

}

