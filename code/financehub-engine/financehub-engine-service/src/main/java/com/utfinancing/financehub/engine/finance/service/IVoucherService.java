package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.github.yulichang.base.MPJBaseService;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.VoucherDetailExportDTO;
import com.utfinancing.financehub.engine.finance.model.vo.VoucherExportVo;
import com.utfinancing.financehub.engine.finance.model.vo.VoucherVO;
import com.utfinancing.financehub.engine.finance.entity.VoucherEntity;
import com.utfinancing.financehub.engine.rule.model.dto.InterfaceDataDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneRuleDTO;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-01
 * @Description : Voucher服务类接口
 * @Modified :
 */
public interface IVoucherService extends MPJBaseService<VoucherEntity> {

    Long saveVoucher(VoucherDTO dto);

    Long updateVoucher(Long id, VoucherDTO dto);

    VoucherDTO getVoucherDTOById(Long id);

    IPage<VoucherVO> selectPage(VoucherQueryDTO queryDTO);

    /**
     * 保存凭证+凭证行
     * @param voucherDTO
     */
    VoucherDTO saveVoucherAndEntries(VoucherSaveDTO voucherDTO);

    /**
     * 根据规则执行结果，生成凭证数据
     * @param ruleDTO
     */
    VoucherDTO saveVoucherDetailByRuleResult(SceneRuleDTO ruleDTO, InterfaceDataDTO interfaceDataDTO);

    /**
     * 根据规则结果生成凭证保存对象
     * @param ruleDTO
     * @param interfaceDataDTO
     * @return
     */
    VoucherSaveDTO generateVoucherFromRule(SceneRuleDTO ruleDTO, InterfaceDataDTO interfaceDataDTO);

    /**
     * 根据ID查询凭证详情
     * @param id
     * @return
     */
    public VoucherDTO getVoucherDTO(Long id);

    /**
     * 获取最新一条凭证
     * @param businessCode 业务编码， 必须要传
     * @param orgId 公司id, 必传
     * @param clientCode 合同编码， 如果不传，查询整体
     * @param contractCode 客户编码， 如果不传，查询整体
     * @param lastDay 查询该日期前的数据,非必填
     * @return
     */
    public VoucherDTO getLastVoucherDTO(String businessCode, String orgId, String clientCode, String contractCode, Date lastDay);


    /**
     * 凭证查询
     * @return
     */
    IPage<VoucherDetailDTO> queryVoucherPage(VoucherQueryDTO queryDTO);

    /**
     * 批量删除凭证数据
     * @param idList
     * @return
     */
    Boolean deleteByIdList(List<Long> idList);

    /**
     * 批量更新凭证的审核状态
     */
    public void updateStatusByids(List<String> ids, String status, String recheckUserNo, String recheckUserName);

    /**
     * 凭证号分页接口
     * @param queryDTO
     * @return
     */
    IPage<VoucherVO> voucherNumberPage(VoucherQueryDTO queryDTO);

    /**
     * 导出凭证信息
     * @param queryDTO
     * @return
     */
    List<VoucherDetailExportDTO> selectAllVoucerDetails(VoucherQueryDTO queryDTO);

    /**
     *
     * @param batchIdList
     * @param batchType
     * @return
     */
    Boolean deleteByBatchIdList(List<Long> batchIdList,String batchType);


    /**
     * 提交凭证（不重新生成凭证）
     * @param voucherIdList
     */
    public void commitVoucherList(List<Long> voucherIdList);

    /**
     * 查询应付保险费相关凭证金额
     * @param voucherQueryDTO
     * @return
     */
    List<VoucherDetailDTO> getInsuranceAmount(VoucherQueryDTO voucherQueryDTO);

    /**
     * 根据批次id和类型获取凭证
     * @param batchIdList
     * @param batchType
     * @return
     */
    List<VoucherVO> getByBatchIdList(List<Long> batchIdList,String batchType);


    /**
     * 凭证查询
     * @return
     */
    IPage<VoucherDetailDTO> summaryByPage(VoucherQueryDTO queryDTO);

    String getClientCodeByClientName(String clientName);

    List<VoucherEntity> selectVoucherEntity(VoucherQueryDTO queryDTO);

    /**
     * 批量更新凭证的审核状态通过批次Id
     */
    void updateStatusByBatch(List<Long> batchIds, String batchType, String status, String userNo, String userName);

    void writeOffVoucher(List<Long> batchIdList,String batchType,String status, String userNo,String userName);

    long generateVoucherNum(String voucherType, LocalDateTime dateTime);

    Boolean writeOff(List<VoucherCopyDTO> copyDTOList);

    Boolean copy(List<VoucherCopyDTO> copyDTOList);

    void updateStatusBatchByIds(List<String> voucherIds, String voucherStatus, Integer periodCode);

    List<VoucherExportVo > summaryExport(VoucherQueryDTO queryDTO);

    /**
     * @description: 手工凭证撤回-仅供手工凭证撤回使用
     * @author: zhangli.chen
     * @date 2025/11/11 15:55
     * @param: batchIds
     * @param: batchType
     * @param: status
     * @param: userNo
     * @param: userName
     * @return void
     **/
    void withdrawOnlyForManualVoucher(List<Long> batchIds, String batchType, String status, String userNo, String userName);
}
