package com.utfinancing.financehub.engine.scene.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.scene.model.dto.AccountQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.AccountDTO;
import com.utfinancing.financehub.engine.scene.model.dto.AccountSaveDTO;
import com.utfinancing.financehub.engine.scene.model.vo.AccountVO;
import com.utfinancing.financehub.engine.scene.entity.AccountEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description : Account服务类接口
 * @Modified :
 */
public interface IAccountService extends IService<AccountEntity> {

    Long saveAccount(AccountSaveDTO dto);

    Long updateAccount(Long id, AccountSaveDTO dto);

    AccountDTO getAccountDTOById(Long id);

    IPage<AccountVO> selectPage(AccountQueryDTO queryDTO);
    List<AccountVO> selectList(AccountQueryDTO queryDTO);

    /**
     * 根据业务编码和金额类型查询科目
     * @param businessCode
     * @param fundType
     * @return
     */
    AccountDTO getAccountByFundType(String businessCode, String fundType);


    List<AccountVO> queryAllAccount();

    AccountDTO getOneAccountByCode(String accountCode);

    public Map<String, AccountEntity> selectAllAccountToRedis();

    public Map<String, AccountEntity> getAccountEntityMapFromRedis();

    public AccountDTO getAccountByFundTypeFromRedis(String businessCode, String fundType);

    public String getFundTypeString(List<String> accountCodeList);

}
