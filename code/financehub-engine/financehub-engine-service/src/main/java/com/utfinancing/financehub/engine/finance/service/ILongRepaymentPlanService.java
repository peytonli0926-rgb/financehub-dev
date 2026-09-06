package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.LongRepaymentPlanQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.LongRepaymentPlanDTO;
import com.utfinancing.financehub.engine.finance.model.vo.LongRepaymentPlanVO;
import com.utfinancing.financehub.engine.finance.entity.LongRepaymentPlanEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-12
 * @Description : LongRepaymentPlan服务类接口
 * @Modified :
 */
public interface ILongRepaymentPlanService extends IService<LongRepaymentPlanEntity> {

    Long saveLongRepaymentPlan(LongRepaymentPlanDTO dto);

    Long updateLongRepaymentPlan(Long id, LongRepaymentPlanDTO dto);

    LongRepaymentPlanDTO getLongRepaymentPlanDTOById(Long id);

    IPage<LongRepaymentPlanVO> selectPage(LongRepaymentPlanQueryDTO queryDTO);

    List<LongRepaymentPlanVO> selectList(LongRepaymentPlanQueryDTO queryDTO);

    /**
     * 根据长期应收款id删除租金计划
     * @param longRegisterIdList
     */
    void removeByLongRegisterIdList(List<Long> longRegisterIdList);

    /**
     * 根据长期应收款id查询租金计划
     * @param longRegisterIdList
     * @return
     */
    List<LongRepaymentPlanEntity> getByLongRegisterIdList(List<Long> longRegisterIdList);
}
