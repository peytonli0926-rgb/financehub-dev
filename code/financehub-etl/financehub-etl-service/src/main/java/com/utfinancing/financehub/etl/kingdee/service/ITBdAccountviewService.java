package com.utfinancing.financehub.etl.kingdee.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.model.dto.KingdeeAccountDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TBdAccountviewQueryDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TBdAccountviewDTO;
import com.utfinancing.financehub.etl.kingdee.model.vo.TBdAccountviewVO;
import com.utfinancing.financehub.etl.kingdee.entity.TBdAccountviewEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : TBdAccountview服务类接口
 * @Modified :
 */
@DS("slave_kingdee")
public interface ITBdAccountviewService extends IService<TBdAccountviewEntity> {

    List<KingdeeAccountDTO> selectAllAccount();
}
