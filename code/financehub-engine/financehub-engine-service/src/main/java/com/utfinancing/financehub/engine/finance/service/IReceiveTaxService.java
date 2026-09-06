package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.ReceiveTaxQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ReceiveTaxDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ReceiveTaxVO;
import com.utfinancing.financehub.engine.finance.entity.ReceiveTaxEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-20
 * @Description : ReceiveTax服务类接口
 * @Modified :
 */
public interface IReceiveTaxService extends IService<ReceiveTaxEntity> {

    Long saveReceiveTax(ReceiveTaxDTO dto);

    Long updateReceiveTax(Long id, ReceiveTaxDTO dto);

    ReceiveTaxDTO getReceiveTaxDTOById(Long id);

    IPage<ReceiveTaxVO> selectPage(ReceiveTaxQueryDTO queryDTO);

    /**
     * 查询所有数据
     * @param queryDTO
     * @return
     */
    List<ReceiveTaxVO> selectList(ReceiveTaxQueryDTO queryDTO);
}
