package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.CostChannelFeeQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CostChannelFeeDTO;
import com.utfinancing.financehub.engine.finance.model.vo.CostChannelFeeVO;
import com.utfinancing.financehub.engine.finance.entity.CostChannelFeeEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-12-13
 * @Description : CostChannelFee服务类接口
 * @Modified :
 */
public interface ICostChannelFeeService extends IService<CostChannelFeeEntity> {

    Long saveCostChannelFee(CostChannelFeeDTO dto);

    Long updateCostChannelFee(Long id, CostChannelFeeDTO dto);

    CostChannelFeeDTO getCostChannelFeeDTOById(Long id);

    IPage<CostChannelFeeVO> selectPage(CostChannelFeeQueryDTO queryDTO);

    Boolean importTemplate(MultipartFile file,String expenseType,String fileType);

    Boolean generateVoucher(List<Long> idList,String isSubmit);

    List<CostChannelFeeVO> listByCondition(CostChannelFeeQueryDTO queryDTO);

    Boolean deleteByIds(List<Long> idList);

    Boolean submit(List<Long> idList);

    Boolean withdraw(List<Long> idList);

    /**
     * 更新核销状态
     * @param commonApproveDTO
     * @return
     */
    Boolean updateProcessStatus(CommonApproveDTO commonApproveDTO);
}
