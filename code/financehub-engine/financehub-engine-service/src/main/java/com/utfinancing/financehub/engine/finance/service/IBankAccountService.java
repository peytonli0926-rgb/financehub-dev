package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.BankAccountQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.BankAccountDTO;
import com.utfinancing.financehub.engine.finance.model.vo.BankAccountVO;
import com.utfinancing.financehub.engine.finance.entity.BankAccountEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-12-06
 * @Description : BankAccount服务类接口
 * @Modified :
 */
public interface IBankAccountService extends IService<BankAccountEntity> {

    Long saveBankAccount(BankAccountDTO dto);

    Long updateBankAccount(Long id, BankAccountDTO dto);

    BankAccountDTO getBankAccountDTOById(Long id);

    IPage<BankAccountVO> selectPage(BankAccountQueryDTO queryDTO);

    /**
     * 根据银行账号查询签约主体
     */
    public  List<BankAccountEntity>  selectBankAccountEntity(String bankAccountNumber);

    /**
     * 根据银行账号编码查询
     * @param bankAccountCode
     * @return
     */
    List<BankAccountEntity> selectByBankAccountCode(String bankAccountCode);
}
