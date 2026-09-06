package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentProvisionQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentProvisionDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentProvisionUploadTaskQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentProvisionDetailReportVO;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentProvisionExcelTypeVO;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentProvisionUploadTaskVO;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentProvisionVO;
import com.utfinancing.financehub.engine.finance.entity.ImpairmentProvisionEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Author : wenbin
 * @Date : Create in 2024-03-25
 * @Description : ImpairmentProvision服务类接口
 * @Modified :
 */
public interface IImpairmentProvisionService extends IService<ImpairmentProvisionEntity> {

    Long saveImpairmentProvision(ImpairmentProvisionDTO dto);

    Long updateImpairmentProvision(Long id, ImpairmentProvisionDTO dto);

    ImpairmentProvisionDTO getImpairmentProvisionDTOById(Long id);

    IPage<ImpairmentProvisionVO> selectPage(ImpairmentProvisionQueryDTO queryDTO);

    /**
     * 查询所有数据
     *
     * @param queryDTO
     * @return
     */
    List<ImpairmentProvisionVO> selectList(ImpairmentProvisionQueryDTO queryDTO);


    /**
     * 生成凭证
     */
    String generateVoucherAsync(List<Long> ids, String code);
    /**
     * 生成凭证
     *
     * @param ids
     * @param code
     * @return
     */
    //Map<String, Integer> generateVoucher(List<Long> ids, String code);

    /**
     * 提交
     *
     * @param ids
     * @return
     */
    String submit(List<Long> ids);

    /**
     * 撤回
     *
     * @param ids
     * @return
     */
    Boolean withdraw(List<Long> ids);

    /**
     * 上传
     *
     * @param file
     * @return
     */
    String importFile(MultipartFile file, String excelType);

    /**
     * 获取上传excel列表
     *
     * @return
     */
    List<ImpairmentProvisionExcelTypeVO> getUploadExcelList();

    /**
     * 查看本月减值报告
     *
     * @return
     */
    List<ImpairmentProvisionDetailReportVO> getImpairmentReport();

    /**
     * 获取导出减值清单excel列表
     *
     * @return
     */
    List<ImpairmentProvisionExcelTypeVO> getExportExcelList();

    /**
     * 导出减值清单
     *
     * @param response
     * @param excelType
     */
    void exportImpairmentList(HttpServletResponse response, String excelType);

    /**
     * 审批修改单据状态
     *
     * @param approveDTO
     */
    void updateProcessStatus(CommonApproveDTO approveDTO);

    /**
     * 批量冲销
     * @param ids
     * @return
     */
    Boolean writeOff(List<Long> ids);

    /**
     * 查询上传任务
     * @param queryDTO
     * @return
     */
    IPage<ImpairmentProvisionUploadTaskVO> queryUpdateTask(ImpairmentProvisionUploadTaskQueryDTO queryDTO);


    /**
     * @description:减值计提-首页-传送明细凭证至金蝶
     **/
    String pushDetailVouchers(List<Long> ids);

    /**
     * @description:减值计提-首页-异步推送汇总凭证
     **/
    public void asyncPushSummaryVoucher(CommonApproveDTO approveDTO);




}
