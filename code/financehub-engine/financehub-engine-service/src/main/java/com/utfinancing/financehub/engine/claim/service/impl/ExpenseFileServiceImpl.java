package com.utfinancing.financehub.engine.claim.service.impl;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.claim.entity.AssetsExpenseEntity;
import com.utfinancing.financehub.engine.claim.entity.ClaimOrderDetailEntity;
import com.utfinancing.financehub.engine.claim.model.dto.AssetsExpenseExcel;
import com.utfinancing.financehub.engine.claim.service.IAssetsExpenseService;
import com.utfinancing.financehub.engine.claim.service.IClaimOrderDetailService;
import com.utfinancing.financehub.engine.claim.service.IExpenseFileService;
import com.utfinancing.financehub.engine.constants.Constants;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.file.common.UnifiedException;
import com.utfinancing.financehub.engine.file.service.FilezService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ExpenseFileServiceImpl implements IExpenseFileService {


    @Value("${file.storage.basicpath.windows:null}")
    private String basicFilePathForWindows;

    @Value("${file.storage.basicpath.linux:null}")
    private String basicFilePathForLinux;

    @Resource
    private FilezService filezService;

    @Resource
    private IAssetsExpenseService assetsExpenseService;

    @Resource
    private IClaimOrderDetailService claimOrderDetailService;

    @Resource
    private ResourceLoader resourceLoader ;

    public R<String> expenseTypeDataSyncTest() throws Exception {
        Long neid = 1782712524660019211L;
        Integer nsid = 1;
        String recordId = String.valueOf(IdWorker.getId());
        InputStream inputStream = filezService.downloadFile(neid, nsid, recordId);

        // 下载文件
        File downloadFile = this.createFile(recordId.concat(".xlsx"));
        FileUtils.copyInputStreamToFile(inputStream, downloadFile);
        log.info(inputStream.toString());
        return R.ok("下载成功!");
    }

    /**
     * 费用类型数据同步
     */
    public R<String> expenseTypeDataSync() throws Exception {
        log.info("财务费用类型数据同步开始...");
        List<ClaimOrderDetailEntity> claimOrderDetailEntityList = claimOrderDetailService.selectByIsDownloadFile();
        if (claimOrderDetailEntityList == null || claimOrderDetailEntityList.isEmpty()) {
            log.info("财务费用类型数据同步完成!");
            return R.ok("财务费用类型数据同步完成!");
        }
        log.info("财务费用类型数据同步数据量：" + claimOrderDetailEntityList.size());

        for (int i = 0; i < claimOrderDetailEntityList.size(); i++) {
            ClaimOrderDetailEntity claimOrderDetailEntity = claimOrderDetailEntityList.get(i);

            String recordId = String.valueOf(claimOrderDetailEntity.getId());
            InputStream inputStream = filezService.downloadFile(claimOrderDetailEntity.getAttachNeid(),
                    claimOrderDetailEntity.getAttachNsid(), recordId);

            // 下载文件
            File downloadFile = this.createFile(recordId.concat(".xlsx"));
            FileUtils.copyInputStreamToFile(inputStream, downloadFile);

            // 将下载的文件再读入写入DB
            InputStream newInputStream = FileUtils.openInputStream(downloadFile);
            ExcelUtil<AssetsExpenseExcel> util = new ExcelUtil<>(AssetsExpenseExcel.class);
            List<AssetsExpenseExcel> list = util.importExcel(newInputStream);

            if (list == null || list.isEmpty()) {
                continue;
            }

            List<AssetsExpenseEntity> entityList = new ArrayList<>();
            for (AssetsExpenseExcel assetsExpenseExcel : list) {
                AssetsExpenseEntity entity = new AssetsExpenseEntity();
                BeanUtils.copyProperties(assetsExpenseExcel, entity);
                entity.setId(IdWorker.getId());
                entity.setClaimOrderDetailId(claimOrderDetailEntity.getId());
                entity.setErrsInfo(this.dataValid(entity));
                entityList.add(entity);
            }
            assetsExpenseService.saveBatch(entityList);
            claimOrderDetailService.lambdaUpdate().
                    set(ClaimOrderDetailEntity::getIsDownloadFile, YesOrNoEnum.YES.getCode()).
                    set(ClaimOrderDetailEntity::getFilePath, downloadFile.getAbsolutePath()).
                    eq(ClaimOrderDetailEntity::getId, claimOrderDetailEntity.getId()).update();
        }
        return R.ok("财务费用类型数据同步完成!");
    }

    /**
     * 数据校验
     */
    private String dataValid(AssetsExpenseEntity entity) {
        StringBuffer errsInfo = new StringBuffer();
        if (StringUtils.isEmpty(entity.getExpenseType())) {
            errsInfo = errsInfo.append("费用类型不能为空! | ");
        }
        if (StringUtils.isEmpty(entity.getSupplier())) {
            errsInfo = errsInfo.append("供应商不能为空! | ");
        }
        if (StringUtils.isEmpty(entity.getContractCode())) {
            errsInfo = errsInfo.append("合同号不能为空! | ");
        }
        if (StringUtils.isEmpty(entity.getContractOrgId())) {
            errsInfo = errsInfo.append("合同主体不能为空! | ");
        }

        // 运输费用校验
        errsInfo = this.haulAmountValid(entity, errsInfo);
        // 保管费用校验
        errsInfo = this.storageAmountValid(entity, errsInfo);
        // 诉讼执行费
        errsInfo = this.briefAndExecutionFeeValid(entity, errsInfo);
        // 诉讼律师服务费校验
        errsInfo = this.counselFeeValid(entity, errsInfo);
        // 评估费
        errsInfo = this.assessAmountValid(entity, errsInfo);
        // 委外催收
        errsInfo = this.entrustFeeValid(entity, errsInfo);
        return errsInfo.toString();
    }


    /**
     * 委外催收校验
     */
    private StringBuffer entrustFeeValid(AssetsExpenseEntity entity, StringBuffer errsInfo) {
        if (StringUtils.isNotEmpty(entity.getExpenseType()) && "委外催收".equals(entity.getExpenseType())) {
            if (StringUtils.isEmpty(entity.getExpensePeriod())) {
                errsInfo = errsInfo.append("费用所属期不能为空! | ");
            }
            if (StringUtils.isEmpty(entity.getAssetsTransferFlag())) {
                errsInfo = errsInfo.append("资产转让标识不能为空! | ");
            }
            if (entity.getReceivedAmount() == null || entity.getReceivedAmount().compareTo(BigDecimal.ZERO) == 0) {
                errsInfo = errsInfo.append("回款金额不能为空! | ");
            }
            if (entity.getEntrustSubjectMatterAmount() == null ||
                    entity.getEntrustSubjectMatterAmount().compareTo(BigDecimal.ZERO) == 0) {
                errsInfo = errsInfo.append("委案标的金额不能为空! | ");
            }
            if (StringUtils.isEmpty(entity.getServiceFeeRate())) {
                errsInfo = errsInfo.append("服务费费率不能为空! | ");
            }
            if (entity.getServiceFeeAmount() == null ||
                    entity.getServiceFeeAmount().compareTo(BigDecimal.ZERO) == 0) {
                errsInfo = errsInfo.append("服务费金额不能为空! | ");
            }
            if (entity.getOverdueDays() == null) {
                errsInfo = errsInfo.append("逾期天数不能为空! | ");
            }
            if (StringUtils.isEmpty(entity.getAccountAge())) {
                errsInfo = errsInfo.append("账龄不能为空! | ");
            }
            if (StringUtils.isEmpty(entity.getFeeRateVersion())) {
                errsInfo = errsInfo.append("费率版本不能为空! | ");
            }
        }
        return errsInfo;
    }

    /**
     * 评估费校验
     */
    private StringBuffer assessAmountValid(AssetsExpenseEntity entity, StringBuffer errsInfo) {
        if (StringUtils.isNotEmpty(entity.getExpenseType()) && "评估费".equals(entity.getExpenseType())) {
            if (entity.getAssessAmount() == null || entity.getAssessAmount().compareTo(BigDecimal.ZERO) == 0) {
                errsInfo = errsInfo.append("评估费金额不能为空! | ");
            }
        }
        return errsInfo;
    }

    /**
     * 诉讼律师服务费校验
     */
    private StringBuffer counselFeeValid(AssetsExpenseEntity entity, StringBuffer errsInfo) {
        if (StringUtils.isNotEmpty(entity.getExpenseType()) && "诉讼律师服务费".equals(entity.getExpenseType())) {
            if (StringUtils.isEmpty(entity.getExpensePeriod())) {
                errsInfo = errsInfo.append("费用所属期不能为空! | ");
            }
            if (entity.getBasicCounselFee() == null || entity.getBasicCounselFee().compareTo(BigDecimal.ZERO) == 0) {
                errsInfo = errsInfo.append("基础律师费不能为空! | ");
            }
            if (entity.getEntrustSubjectMatterAmount() == null ||
                    entity.getEntrustSubjectMatterAmount().compareTo(BigDecimal.ZERO) == 0) {
                errsInfo = errsInfo.append("委案标的金额不能为空! | ");
            }
        }
        return errsInfo;
    }

    /**
     * 诉讼执行费校验
     */
    private StringBuffer briefAndExecutionFeeValid(AssetsExpenseEntity entity, StringBuffer errsInfo) {
        if (StringUtils.isNotEmpty(entity.getExpenseType()) && "诉讼执行费".equals(entity.getExpenseType())) {
            if (StringUtils.isEmpty(entity.getExpensePeriod())) {
                errsInfo = errsInfo.append("费用所属期不能为空! | ");
            }
            if (entity.getBriefAndExecutionFee() == null || entity.getBriefAndExecutionFee().compareTo(BigDecimal.ZERO) == 0) {
                errsInfo = errsInfo.append("诉讼费/执行费金额不能为空! | ");
            }
            if (StringUtils.isEmpty(entity.getDebtToBatchNo())) {
                errsInfo = errsInfo.append("债转批次不能为空! | ");
            }
        }
        return errsInfo;
    }

    /**
     * 保管费用校验
     */
    private StringBuffer storageAmountValid(AssetsExpenseEntity entity, StringBuffer errsInfo) {
        if (StringUtils.isNotEmpty(entity.getExpenseType()) && "保管费".equals(entity.getExpenseType())) {
            if (entity.getStorageDays() == null) {
                errsInfo = errsInfo.append("保管天数不能为空! | ");
            }
            if (entity.getStorageSerivceFee() == null || entity.getStorageSerivceFee().compareTo(BigDecimal.ZERO) == 0) {
                errsInfo = errsInfo.append("仓储保管服务费不能为空! | ");
            }
        }
        return errsInfo;
    }

    /**
     * 运输费用校验
     */
    private StringBuffer haulAmountValid(AssetsExpenseEntity entity, StringBuffer errsInfo) {
        if (StringUtils.isNotEmpty(entity.getExpenseType()) && "运输费".equals(entity.getExpenseType())) {
            if (entity.getHaulDistance() == null || entity.getHaulDistance().compareTo(BigDecimal.ZERO) == 0) {
                errsInfo = errsInfo.append("运输距离不能为空! | ");
            }
            if (entity.getHaulAmount() == null || entity.getHaulAmount().compareTo(BigDecimal.ZERO) == 0) {
                errsInfo = errsInfo.append("运输费用不能为空! | ");
            }
            if (entity.getMaintenanceAmount() == null || entity.getMaintenanceAmount().compareTo(BigDecimal.ZERO) == 0) {
                errsInfo = errsInfo.append("整备费用不能为空! | ");
            }
            if (entity.getExpenseTotalAmount() == null || entity.getExpenseTotalAmount().compareTo(BigDecimal.ZERO) == 0) {
                errsInfo = errsInfo.append("费用合计不能为空! | ");
            }
        }
        return errsInfo;
    }

    /**
     * 取得文件存储路径
     */
    private String getFileStoragePath() {
        String operationSystemName = System.getProperties().getProperty("os.name");
        if (operationSystemName.toLowerCase().indexOf(Constants.OPERATION_SYSTEM_NAME_WINDOWS) > -1) {
            return basicFilePathForWindows.concat("ExpenseFileDownloadFloder\\");
        } else if (operationSystemName.toLowerCase().indexOf(Constants.OPERATION_SYSTEM_NAME_LINUX) > -1
                || operationSystemName.toLowerCase().indexOf(Constants.OPERATION_SYSTEM_NAME_UNIX) > -1) {
            return basicFilePathForLinux.concat("ExpenseFileDownloadFloder/");
        }
        return StringUtil.EMPTY;
    }

    /**
     * 创建文件
     */
    private File createFile(String fileName) throws IOException {
        String directoryPath = this.getFileStoragePath();
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File targetFile = new File(directory.getAbsolutePath().concat(File.separator).concat(fileName));
        if (targetFile.exists()) {
            targetFile.delete();
        }

        return targetFile;
    }
}
