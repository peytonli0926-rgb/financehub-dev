package com.utfinancing.financehub.engine.rule.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.common.core.constant.HttpStatus;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.engine.enums.SceneEnum;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.CloseAccountEntity;
import com.utfinancing.financehub.engine.finance.entity.FundBusinessSystemEbankMappingEntity;
import com.utfinancing.financehub.engine.finance.entity.FundBusinessSystemEbankWyAmountEntity;
import com.utfinancing.financehub.engine.finance.entity.FundEbankTransactionDataEntity;
import com.utfinancing.financehub.engine.finance.model.dto.SelectFundEbankTransactionDataByConDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SelectNonConfirmCollectionDataDTO;
import com.utfinancing.financehub.engine.finance.service.ICloseAccountService;
import com.utfinancing.financehub.engine.finance.service.IFundBusinessSystemEbankMappingService;
import com.utfinancing.financehub.engine.finance.service.IFundBusinessSystemEbankWyAmountService;
import com.utfinancing.financehub.engine.finance.service.IFundEbankTransactionDataService;
import com.utfinancing.financehub.engine.rule.model.dto.InterfaceDataDTO;
import com.utfinancing.financehub.engine.rule.service.IPeriodCodeService;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import com.utfinancing.financehub.engine.utils.PeriodCodeUtil;
import com.utfinancing.financehub.etl.api.RemoteKingdeeEasService;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.statement.truncate.Truncate;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 23/01/2024
 */
@AllArgsConstructor
@Service
public class PeriodCodeServiceImpl implements IPeriodCodeService {

    private final ICloseAccountService closeAccountService;
    private final RemoteKingdeeEasService remoteKingdeeEasService;

    @Resource
    @Lazy
    private IFundBusinessSystemEbankMappingService fundBusinessSystemEbankMappingService;

    @Resource
    @Lazy
    private IFundEbankTransactionDataService fundEbankTransactionDataService;

    @Resource
    @Lazy
    private IFundBusinessSystemEbankWyAmountService fundBusinessSystemEbankWyAmountService;

    @Override
    public LocalDate generateVoucherDate(InterfaceDataDTO dto) {
        String systemCode = dto.getSystemCode();
        String orgId = dto.getOrgId();
        LocalDateTime businessDate = dto.getBusinessDate();
        LocalDateTime interFaceCreateTime = dto.getInterfaceCreateTime();
        if (interFaceCreateTime == null) {
            interFaceCreateTime = LocalDateTime.now();
        }

        String sceneCode = dto.getSceneCode();
        String isInterfaceData = dto.getIsInterfaceData();
        // 起租接口不要求上游传核算日期，凭证日期统一取业务发生日期。
        if (ObjectUtil.equals(sceneCode, SceneEnum.HTQZ.getCode())) {
            return null == businessDate ? LocalDate.now() : businessDate.toLocalDate();
        }
        if (StringUtils.isNotEmpty(isInterfaceData) && YesOrNoEnum.YES.getCode().equals(isInterfaceData)) {
            return null == interFaceCreateTime ? LocalDate.now() : interFaceCreateTime.toLocalDate();
        }
        if (SystemEnum.MFXT.getCode().equals(systemCode)) {
            //魔方系统，取当前系统时间
            return null == interFaceCreateTime ? LocalDate.now() : interFaceCreateTime.toLocalDate();
        }
        if (SystemEnum.KPXT.getCode().equals(systemCode) || SystemEnum.ZJXT.getCode().equals(systemCode)) {
            //资金系统，按统一平台的记账日期处理
            return null == businessDate ? LocalDate.now() : businessDate.toLocalDate();
        } else if (SystemEnum.HYB.getCode().equals(systemCode) || SystemEnum.GAXT.getCode().equals(systemCode)
                || SystemEnum.HY_XDWL.getCode().equals(systemCode)) {
            return null == businessDate ? LocalDate.now() : businessDate.toLocalDate();
        }

        LocalDate voucherDate = LocalDate.now();
        if (SystemEnum.CWZT.getCode().equals(systemCode)) {
            // 中台，以下场景，直接用传入的业务日期
            if (ObjectUtil.equals(sceneCode, SceneEnum.DZZCJZ.getCode()) || ObjectUtil.equals(sceneCode, SceneEnum.DZZCSR.getCode())
                    || ObjectUtil.equals(sceneCode, SceneEnum.HTQZ.getCode()) || ObjectUtil.equals(sceneCode, SceneEnum.FWFJT.getCode())
            ) {
                voucherDate = businessDate.toLocalDate();
            } else {
                // 取会计期间时，海通恒信金融集团（香港）换为 海通恒信国际融资租赁股份有限公司
                if (ObjectUtil.equals(orgId, "00000")) {
                    orgId = "01-C0001";
                }
                // 中台逻辑 取金蝶的当前账期
                voucherDate = getGeneralLedgerVoucherDate(orgId, businessDate);
            }
        } else if (SystemEnum.YWZT.getCode().equals(systemCode)  || SystemEnum.HYYW.getCode().equals(systemCode)) {
            voucherDate = businessDate.toLocalDate();
        } else {
            //其余系统根据各自的关账时间来决定
            voucherDate = getSystemVoucherDate(systemCode, dto.getEbankSerialNumber(), interFaceCreateTime, dto.getSettlementWay());
        }
        return voucherDate;
    }

    private Date getDateByYMD(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // 注意月份从0开始，即0对应1月
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * 业务系统凭证日期取值逻辑
     */
    private LocalDate getSystemVoucherDate(String systemCode, String ebankSerialNumber,
                                           LocalDateTime interFaceCreateTime, String settlementWay) {
        LocalDate voucherDate = null;

        Date createTime = CommonDateUtils.parseLocalDateTimeToDate(interFaceCreateTime);
        //获取当前的关账时间
        CloseAccountEntity accountEntity = closeAccountService.queryCloseDate(systemCode);
        if (null == accountEntity) {
            throw new ServiceException(String.format("系统编码:%s的关账期间不存在", systemCode));
        }
        //操作日期就是关账日期
        Date closeDate = CommonDateUtils.parseDateStringToDate(accountEntity.getOperateDate());
        // 上一个账期的日期(1号)
        Date lastAccountPeriodDate = getDateByYMD(accountEntity.getYear(), accountEntity.getMonth(), 1);
        // 当前账期日期1号
        Date curAccountPeriodDate = DateUtil.offsetMonth(lastAccountPeriodDate, 1);
        // 当前账期的最后一天
        Date curAccountPeriodLastDate = DateUtil.endOfMonth(curAccountPeriodDate);

        // 判断是否为网银到账事件
        if ("卡扣".equals(settlementWay)) {
            // 取得网银到账日期
            Date ebankIncomeDate = this.getEbankIncomeDate(ebankSerialNumber);
            Date compareDate = createTime.compareTo(ebankIncomeDate) >= 0 ? createTime : ebankIncomeDate;
            if (compareDate.compareTo(curAccountPeriodLastDate) <= 0) {
                voucherDate = compareDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } else {
                voucherDate = curAccountPeriodLastDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
        } else {
            // 创建时间 <= 当前账期的最后一天
            if (createTime.compareTo(curAccountPeriodLastDate) <= 0) {
                voucherDate = createTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } else {
                voucherDate = curAccountPeriodLastDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
        }

        return voucherDate;
    }

    /**
     * 根据批扣号码，查询网银到账日期
     */
    private Date getEbankIncomeDate(String ebankSerialNumber) {
        Date ebankIncomeDate = null;
        // 取得资金系统与业务系统的网银编号映射关系
        FundBusinessSystemEbankMappingEntity fundBusinessSystemEbankMappingEntity =
                fundBusinessSystemEbankMappingService.selectFundBusinessSystemEbankMappingBySerialNumber(ebankSerialNumber);
        // 如果映射关系不存在，则无法进行凭证日期生产
        if (fundBusinessSystemEbankMappingEntity == null) {
            throw new ServiceException("资金系统与业务系统的网银编号映射关系不存在!批扣流水号：" + ebankSerialNumber);
        }

        //
        // 查询资金系统的网银到账信息
        SelectFundEbankTransactionDataByConDTO dto = new SelectFundEbankTransactionDataByConDTO();
        dto.setEbankNumberList(Arrays.asList(fundBusinessSystemEbankMappingEntity.getEbankNumber().split(",")));
        List<FundEbankTransactionDataEntity> fundEbankTransactionDataEntities =
                fundEbankTransactionDataService.selectFundEbankTransactionDataByCon(dto);
        // 如果资金系统的网银到账信息不存在，则查询另一个表
        if (fundEbankTransactionDataEntities == null || fundEbankTransactionDataEntities.isEmpty()) {
            SelectNonConfirmCollectionDataDTO params = new SelectNonConfirmCollectionDataDTO();
            params.setEbankNumber(fundBusinessSystemEbankMappingEntity.getEbankNumber());
            List<FundBusinessSystemEbankWyAmountEntity> fundBusinessSystemEbankWyAmountEntities =
                    fundBusinessSystemEbankWyAmountService.selectFundEbankTransactionDataByCon(params);
            if (fundBusinessSystemEbankWyAmountEntities == null || fundBusinessSystemEbankWyAmountEntities.isEmpty()) {
                throw new ServiceException("不能取得网银到账日期! 批扣流水号：" + ebankSerialNumber);
            } else {
                ebankIncomeDate = fundBusinessSystemEbankWyAmountEntities.get(0).getBusinessDate();
            }
        } else {
            ebankIncomeDate = DateUtils.parseDate(fundEbankTransactionDataEntities.get(0).getBusinessDate());
        }
        return ebankIncomeDate;
    }

    /**
     * 金蝶总账凭证日期取值逻辑
     * @return
     */
    private LocalDate getGeneralLedgerVoucherDate(String orgId, LocalDateTime businessDate) {
        LocalDate voucherDate = null;
        if (ObjectUtil.isEmpty(businessDate) || StringUtils.isEmpty(orgId)) {
            businessDate = LocalDateTime.now();
        }
        if (StringUtils.isEmpty(orgId)) {
            return businessDate.toLocalDate();
        }
        //获取当前会计期间
        Integer currentPeriodCode = this.queryKingdeePeriodCode(orgId);
        //提取当前业务日期的会计期间
        LocalDate bizDate = businessDate.toLocalDate();

        // 1.业务日期>会计期间 ，取会计期间最后一天
        // 2.业务日期 在会计期间内，取业务日期，
        // 3.业务日期< 会计期间，取会计期间第一天

        Integer businessPeriodCode = NumberUtil.parseInt(LocalDateTimeUtil.format(bizDate, "yyyyMM"));
        if (businessPeriodCode > currentPeriodCode) {
            // 1.业务日期>会计期间 ，取会计期间最后一天记账日期等于业务日期
            voucherDate = PeriodCodeUtil.parseLastDayOfMonth(currentPeriodCode);
        } else if (ObjectUtil.equals(businessPeriodCode, currentPeriodCode)) {
            //2.业务日期 在会计期间内，取业务日期
            voucherDate = bizDate;
        } else {
            // 3.业务日期< 会计期间，取会计期间第一天
            voucherDate = PeriodCodeUtil.parseFirstDayOfMonth(currentPeriodCode);
        }
        return voucherDate;
    }

    /**
     * 查询金蝶当前期间
     * @param orgId
     * @return
     */
    private Integer queryKingdeePeriodCode(String orgId) {
        R<Integer> periodResult = remoteKingdeeEasService.getCurrentPeriodCode(orgId);
        if (periodResult.getCode() != HttpStatus.SUCCESS || periodResult.getData() == null) {
            // update by pgao 和需求沟通如果查询不到金蝶数据就取系统当前期间
            return NumberUtil.parseInt(LocalDateTimeUtil.format(LocalDate.now(), "yyyyMM"));
        }
        return periodResult.getData();
    }

}
