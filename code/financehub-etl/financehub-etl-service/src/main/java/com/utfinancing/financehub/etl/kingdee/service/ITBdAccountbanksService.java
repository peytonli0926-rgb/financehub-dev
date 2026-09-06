package com.utfinancing.financehub.etl.kingdee.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.model.dto.BankAccountDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TBdAccountbanksQueryDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TBdAccountbanksDTO;
import com.utfinancing.financehub.etl.kingdee.model.vo.TBdAccountbanksVO;
import com.utfinancing.financehub.etl.kingdee.entity.TBdAccountbanksEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : TBdAccountbanks服务类接口
 * @Modified :
 */
@DS("slave_kingdee")
public interface ITBdAccountbanksService extends IService<TBdAccountbanksEntity> {

    List<BankAccountDTO> selectAllAccountBank();

}
