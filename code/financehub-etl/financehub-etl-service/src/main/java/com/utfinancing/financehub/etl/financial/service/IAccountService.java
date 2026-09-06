package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.model.dto.AccountQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.AccountDTO;
import com.utfinancing.financehub.etl.financial.model.vo.AccountVO;
import com.utfinancing.financehub.etl.financial.entity.AccountEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-16
 * @Description : Account服务类接口
 * @Modified :
 */
public interface IAccountService extends IService<AccountEntity> {

    Long saveAccount(AccountDTO dto);

    Long updateAccount(Long id, AccountDTO dto);

    AccountDTO getAccountDTOById(Long id);

    IPage<AccountVO> selectPage(AccountQueryDTO queryDTO);

}
