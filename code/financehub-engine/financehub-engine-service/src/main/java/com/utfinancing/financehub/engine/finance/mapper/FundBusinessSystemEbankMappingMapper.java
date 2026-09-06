package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.FundBusinessSystemEbankMappingEntity;
import com.utfinancing.financehub.engine.finance.model.dto.SelectNonConfirmCollectionDataDTO;
import com.utfinancing.financehub.engine.finance.model.vo.SystemBankMappingReconciliationDetailVO;
import com.utfinancing.financehub.engine.finance.model.vo.SystemBankMappingReconciliationNoVO;
import com.utfinancing.financehub.engine.finance.model.vo.SystemBankMappingReconciliationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 资金系统、业务系统网银编号映射表 Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2023-12-21
 */
public interface FundBusinessSystemEbankMappingMapper extends BaseMapper<FundBusinessSystemEbankMappingEntity> {

    /**
     * 查询未确认收款的资金系统数据
     */
    public List<SelectNonConfirmCollectionDataDTO> selectNonConfirmCollectionData();

    Page<SystemBankMappingReconciliationVO> systemBankMappingReconciliationFilter(Page<SystemBankMappingReconciliationVO> page, @Param("periodCode") int periodCode, @Param("abnormal") Integer abnormal);

    SystemBankMappingReconciliationVO systemBankMappingReconciliationByMatchNumber(@Param("periodCode") int periodCode, @Param("matchNumber") String abnormal);

    Page<SystemBankMappingReconciliationDetailVO> systemBankMappingReconciliationDetailByMatchNumber(Page<SystemBankMappingReconciliationDetailVO> page,@Param("matchNumber") String matchNumber);

    Page<SystemBankMappingReconciliationNoVO> systemBankMappingReconciliationNoVO(Page<SystemBankMappingReconciliationNoVO> page);

}
