package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.entity.ParityTransferEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-04-02
 * @Description : ParityTransfer服务类接口
 * @Modified :
 */
public interface IParityTransferService extends IService<ParityTransferEntity> {

    Long saveParityTransfer(ParityTransferDTO dto);

    Long updateParityTransfer(Long id, ParityTransferDTO dto);

    ParityTransferDTO getParityTransferDTOById(Long id);

    IPage<ParityTransferVO> selectPage(ParityTransferQueryDTO queryDTO);

    List<String> batchList();

    List<ParityTransferExportVO> selectPartityTransferList(List<Long> idList);

    List<ParityTransferDetailExportVO> selectPartityTransferDetailList(List<Long> idList);

    Boolean importTemplate(MultipartFile file) throws IOException;

    Boolean importPaymentInfo(MultipartFile file) throws IOException;

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


    Boolean generateVoucher(List<Long> idList,String isSubmit);

    Boolean updateProcessStatus(CommonApproveDTO approveDTO);

    /**
     * 校验最新余额数据分页接口
     *
     * @param queryDTO
     * @return
     */
    IPage<ParityTransferCheckVO> selectCheckPage(CheckPageQueryDTO queryDTO);

    IPage<ParityTransferDetailVO> detailPage(ParityTransferDetailQueryDTO queryDTO);

}
