package com.utfinancing.financehub.engine.finance.service;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.LprDataQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.LprDataDTO;
import com.utfinancing.financehub.engine.finance.model.vo.LprDataVO;
import com.utfinancing.financehub.engine.finance.entity.LprDataEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-17
 * @Description : LprData服务类接口
 * @Modified :
 */
public interface ILprDataService extends IService<LprDataEntity> {

    Long saveLprData(LprDataDTO dto);

    void saveRawData(JSONArray jsonArray);

    Long updateLprData(Long id, LprDataDTO dto);

    LprDataDTO getLprDataDTOById(Long id);

    IPage<LprDataVO> selectPage(LprDataQueryDTO queryDTO);

    List<LprDataVO> listOneYear();
}
