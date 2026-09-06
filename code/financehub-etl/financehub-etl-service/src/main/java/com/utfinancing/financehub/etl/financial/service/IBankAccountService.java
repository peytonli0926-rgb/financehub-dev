package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.model.dto.BankAccountQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.BankAccountDTO;
import com.utfinancing.financehub.etl.financial.model.vo.BankAccountVO;
import com.utfinancing.financehub.etl.financial.entity.BankAccountEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : BankAccount服务类接口
 * @Modified :
 */
public interface IBankAccountService extends IService<BankAccountEntity> {

    Long saveBankAccount(BankAccountDTO dto);

    Long updateBankAccount(Long id, BankAccountDTO dto);

    BankAccountDTO getBankAccountDTOById(Long id);

    IPage<BankAccountVO> selectPage(BankAccountQueryDTO queryDTO);

}
