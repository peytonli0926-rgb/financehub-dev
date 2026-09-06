package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.CloseAccountQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CloseAccountDTO;
import com.utfinancing.financehub.engine.finance.model.vo.CloseAccountVO;
import com.utfinancing.financehub.engine.finance.entity.CloseAccountEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-01-16
 * @Description : CloseAccount服务类接口
 * @Modified :
 */
public interface ICloseAccountService extends IService<CloseAccountEntity> {

    Long saveCloseAccount(CloseAccountDTO dto);

    Long updateCloseAccount(Long id, CloseAccountDTO dto);

    CloseAccountDTO getCloseAccountDTOById(Long id);

    IPage<CloseAccountVO> selectPage(CloseAccountQueryDTO queryDTO);

    Integer queryCurrentPeriodCode(String systemCode);

    CloseAccountEntity queryCloseDate(String systemCode);
}
