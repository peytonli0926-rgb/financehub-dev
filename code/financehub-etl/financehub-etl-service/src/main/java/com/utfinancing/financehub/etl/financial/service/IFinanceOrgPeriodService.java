package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.etl.financial.entity.AccountEntity;
import com.utfinancing.financehub.etl.financial.model.dto.AccountDTO;
import com.utfinancing.financehub.etl.financial.model.dto.AccountQueryDTO;
import com.utfinancing.financehub.etl.financial.model.vo.AccountVO;
import com.utfinancing.financehub.etl.kingdee.model.dto.OrgPeriodDTO;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-16
 * @Description : Account服务类接口
 * @Modified :
 */
public interface IFinanceOrgPeriodService {

    List<OrgPeriodDTO> selectCurrentPeriodCodeByOrgIds(List<String> compareOrgIds);
}
