package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.ContractBalanceVO;
import com.utfinancing.financehub.engine.finance.model.vo.OutTableAbsVO;
import com.utfinancing.financehub.engine.finance.entity.OutTableAbsEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.model.vo.OutTableContractDetailVO;
import com.utfinancing.financehub.engine.finance.model.vo.OutTableRentPlanVO;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-11
 * @Description : OutTableAbs服务类接口
 * @Modified :
 */
public interface IOutTableAbsService extends IService<OutTableAbsEntity> {

    Long saveOutTableAbs(OutTableAbsDTO dto);

    Long updateOutTableAbs(Long id, OutTableAbsDTO dto);

    OutTableAbsDTO getOutTableAbsDTOById(Long id);

    IPage<OutTableAbsVO> selectPage(OutTableAbsQueryDTO queryDTO);

    Boolean importTemplate(MultipartFile file) throws IOException;

    IPage<OutTableContractDetailVO> detailPage(OutTableContractDetailQueryDTO queryDTO);

    Boolean deleteByIds(List<Long> idList);

    /**
     * 批量提交
     * @param idList
     * @return
     */
    Boolean submit(List<Long> idList);

    /**
     * 批量撤回
     * @param idList
     * @return
     */
    Boolean withdraw(List<Long> idList);

    /**
     * 批量生成凭证
     * @param idList
     * @param isSubmit
     * @return
     */
    Boolean generateVoucher(List<Long> idList,String isSubmit);

    Boolean updateProcessStatus(CommonApproveDTO approveDTO);

    List<OutTableAbsVO> selectTableAbsList(List<Long> idList);

    List<OutTableContractDetailVO> selectTableContractDetailList(List<Long> idList);

    /**
     * 根据合同号查已经复核和传送金蝶的数据
     * @param queryDTO
     * @return
     */
    List<OutTableContractDetailVO> selectByCondition(OutTableContractDetailQueryDTO queryDTO);

    IPage<OutTableRentPlanVO> rentPlanPage(OutTableAbsQueryDTO queryDTO);

    List<OutTableRentPlanVO> rentPlanList(OutTableAbsQueryDTO queryDTO);

    /**
     * 校验最新余额数据分页接口
     * @param queryDTO
     * @return
     */
    IPage<ContractBalanceVO> selectCheckPage(CheckPageQueryDTO queryDTO);

}
