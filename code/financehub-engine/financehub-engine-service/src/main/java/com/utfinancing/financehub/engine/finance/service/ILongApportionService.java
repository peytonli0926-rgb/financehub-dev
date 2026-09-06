package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.LongApportionQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.LongApportionDTO;
import com.utfinancing.financehub.engine.finance.model.vo.LongApportionVO;
import com.utfinancing.financehub.engine.finance.entity.LongApportionEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-12
 * @Description : LongApportion服务类接口
 * @Modified :
 */
public interface ILongApportionService extends IService<LongApportionEntity> {

    Long saveLongApportion(LongApportionDTO dto);

    Long updateLongApportion(Long id, LongApportionDTO dto);

    LongApportionDTO getLongApportionDTOById(Long id);

    IPage<LongApportionVO> selectPage(LongApportionQueryDTO queryDTO);

    List<LongApportionVO> selectList(LongApportionQueryDTO queryDTO);

    /**
     * 根据长期应收款id删除分摊表
     * @param longRegisterIdList
     */
    void removeByLongRegisterIdList(List<Long> longRegisterIdList);
}
