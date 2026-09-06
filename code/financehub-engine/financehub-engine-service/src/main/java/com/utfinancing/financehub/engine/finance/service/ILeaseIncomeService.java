package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.entity.LeaseIncomeDetailsEntity;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.LeaseIncomeDetailsVO;
import com.utfinancing.financehub.engine.finance.model.vo.LeaseIncomeVO;
import com.utfinancing.financehub.engine.finance.entity.LeaseIncomeEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanVO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeDetailsVO;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-10
 * @Description : LeaseIncome服务类接口
 * @Modified :
 */
public interface ILeaseIncomeService extends IService<LeaseIncomeEntity> {

    Long saveLeaseIncome(LeaseIncomeDTO dto);

    Long updateLeaseIncome(Long id, LeaseIncomeDTO dto);

    LeaseIncomeDTO getLeaseIncomeDTOById(Long id);

    IPage<LeaseIncomeVO> selectPage(LeaseIncomeQueryDTO queryDTO);

    IPage<LeaseIncomeDetailsVO> selectDetailPage(LeaseIncomeDetailsQueryDTO queryDTO);

    List<LeaseIncomeDetailsVO> selectDetailList(LeaseIncomeDetailsQueryDTO leaseIncomeDetailsQueryDTO);

    List<RepaymentPlanVO> selectDetailPlanList(LeaseIncomeDetailsQueryDTO queryDTO);

    void deleteByIds(List<Long> ids);

    void submit(List<Long> ids);

    void withdraw(List<Long> ids);

    void pass(CommonApproveDTO approveDTO);

    void fail(CommonApproveDTO approveDTO);

    /**
     * 同步未实现收益
     */
    Boolean outstandingAmountSync(LeaseIncomeQueryDTO queryDTO);


    @Async
    void generateAsync(LeaseIncomeQueryDTO queryDTO);

    void generateLeaseIncome(LeaseIncomeQueryDTO queryDTO);

    R<String> importData(List<LeaseIncomeImport> list);

    void voucher(List<Long> ids, String isSubmit);

    public void multiThreadGenVoucher(List<Long> ids, String isSubmit);

    public Boolean createThreadForGenVoucher(int startIndex, int endIndex, String isSubmit, Long leaseIncomeId);
}
