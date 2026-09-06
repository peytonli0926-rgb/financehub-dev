package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.engine.enums.SceneEnum;
import com.utfinancing.financehub.engine.finance.entity.PayableInsuranceInterfaceTotalEntity;
import com.utfinancing.financehub.engine.finance.mapper.PayableInsuranceInterfaceTotalMapper;
import com.utfinancing.financehub.engine.finance.model.dto.PayableInsuranceInterfaceTotalDTO;
import com.utfinancing.financehub.engine.finance.model.dto.PayableInsuranceInterfaceTotalQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.PayableInsuranceInterfaceTotalSaveDTO;
import com.utfinancing.financehub.engine.finance.model.vo.PayableInsuranceInterfaceTotalVO;
import com.utfinancing.financehub.engine.finance.service.IPayableInsuranceInterfaceTotalService;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-30
 * @Description :  PayableInsuranceInterfaceTotal服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class PayableInsuranceInterfaceTotalServiceImpl extends ServiceImpl<PayableInsuranceInterfaceTotalMapper, PayableInsuranceInterfaceTotalEntity> implements IPayableInsuranceInterfaceTotalService {

    private final PayableInsuranceInterfaceTotalMapper payableInsuranceInterfaceTotalMapper;

    @Override
    public Long savePayableInsuranceInterfaceTotal(PayableInsuranceInterfaceTotalSaveDTO dto) {
        PayableInsuranceInterfaceTotalEntity entity = BeanUtil.copyProperties(dto, PayableInsuranceInterfaceTotalEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updatePayableInsuranceInterfaceTotal(Long id, PayableInsuranceInterfaceTotalDTO dto) {
        PayableInsuranceInterfaceTotalEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public PayableInsuranceInterfaceTotalDTO getPayableInsuranceInterfaceTotalDTOById(Long id) {
        PayableInsuranceInterfaceTotalEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, PayableInsuranceInterfaceTotalDTO.class);
    }

    @Override
    public IPage<PayableInsuranceInterfaceTotalVO> selectPage(PayableInsuranceInterfaceTotalQueryDTO queryDTO) {
        LambdaQueryWrapper<PayableInsuranceInterfaceTotalEntity> queryWrapper = Wrappers.<PayableInsuranceInterfaceTotalEntity>lambdaQuery();
        //这里注入查询条件
        IPage<PayableInsuranceInterfaceTotalEntity> entityIPage = payableInsuranceInterfaceTotalMapper.selectPage(new Page<PayableInsuranceInterfaceTotalEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, PayableInsuranceInterfaceTotalVO.class);
    }

    @Override
    public void saveFromInterfaceData(Map<String, Object> dataMap) {
        String sceneCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_SCENE_CODE);
        String contractCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_CONTRACT_CODE);
        String sceneCodeOriginal = MapUtil.getStr(dataMap, RuleConstant.FIELD_SCENE_CODE_ORIGINAL);
        //投保场景 累计应付保险费
        if (SceneEnum.BXTB.getCode().equals(sceneCode) || (SceneEnum.ZLFK.getCode().equals(sceneCode) && SceneEnum.BXFFK.getDesc().equals(sceneCodeOriginal))) {
            PayableInsuranceInterfaceTotalSaveDTO saveDTO = BeanUtil.copyProperties(dataMap, PayableInsuranceInterfaceTotalSaveDTO.class);
            PayableInsuranceInterfaceTotalDTO oldDto = this.getByContractCode(contractCode);
            BigDecimal newActualPayableInsuaranceAmount = saveDTO.getActualPayableInsuaranceAmount();
            if (oldDto == null) {
                //如果不存在，新增一条记录
                BigDecimal payableInsuranceBalance = NumberUtil.sub(newActualPayableInsuaranceAmount, saveDTO.getPayableInsuranceAmount());
                saveDTO.setPayableInsuranceBalance(payableInsuranceBalance);
                this.savePayableInsuranceInterfaceTotal(saveDTO);
                return;
            }
            //如果存在，更新累计金额
            oldDto.setActualPayableInsuaranceAmount(newActualPayableInsuaranceAmount);
            oldDto.setPayableInsuranceAmount(NumberUtil.add(oldDto.getPayableInsuranceAmount(), saveDTO.getPayableInsuranceAmount()));
            oldDto.setPayableInsuranceBalance(NumberUtil.sub(oldDto.getActualPayableInsuaranceAmount(), oldDto.getPayableInsuranceAmount()));
            this.updateEntityById(oldDto);
        }
    }

    private void updateEntityById(PayableInsuranceInterfaceTotalDTO oldDto) {
        PayableInsuranceInterfaceTotalEntity entity = BeanUtil.copyProperties(oldDto, PayableInsuranceInterfaceTotalEntity.class);
        entity.updateById();
    }

    private PayableInsuranceInterfaceTotalDTO getByContractCode(String contractCode) {
        PayableInsuranceInterfaceTotalEntity entity = this.getOne(Wrappers.<PayableInsuranceInterfaceTotalEntity>lambdaQuery()
                .eq(PayableInsuranceInterfaceTotalEntity::getContractCode, contractCode)
        );
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, PayableInsuranceInterfaceTotalDTO.class);
    }

    @Override
    public List<PayableInsuranceInterfaceTotalDTO> listByContractCode(List<String> contractCodes) {
        if (CollectionUtils.isEmpty(contractCodes)) {
            return new ArrayList<>();
        }
        List<PayableInsuranceInterfaceTotalEntity> list = this.list(Wrappers.<PayableInsuranceInterfaceTotalEntity>lambdaQuery()
                .in(PayableInsuranceInterfaceTotalEntity::getContractCode, contractCodes)
        );
        return BeanUtil.copyToList(list, PayableInsuranceInterfaceTotalDTO.class);
    }

}

