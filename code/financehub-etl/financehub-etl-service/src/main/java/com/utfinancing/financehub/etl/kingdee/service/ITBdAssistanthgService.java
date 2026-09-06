package com.utfinancing.financehub.etl.kingdee.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.kingdee.model.dto.TBdAssistanthgQueryDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TBdAssistanthgDTO;
import com.utfinancing.financehub.etl.kingdee.model.vo.TBdAssistanthgVO;
import com.utfinancing.financehub.etl.kingdee.entity.TBdAssistanthgEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : TBdAssistanthg服务类接口
 * @Modified :
 */
@DS("slave_kingdee")
public interface ITBdAssistanthgService extends IService<TBdAssistanthgEntity> {

}
