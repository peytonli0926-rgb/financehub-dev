package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.DictTypeEnum;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.BatchModifyTemplateEntity;
import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionSumEntity;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.NonConfirmCollectionAccountCheckingVO;
import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionAccountCheckingEntity;
import com.utfinancing.financehub.engine.finance.mapper.NonConfirmCollectionAccountCheckingMapper;
import com.utfinancing.financehub.engine.finance.service.IBatchModifyTemplateService;
import com.utfinancing.financehub.engine.finance.service.INonConfirmCollectionAccountCheckingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.service.INonConfirmCollectionSumService;
import com.utfinancing.financehub.engine.hthx.common.enums.FinanceEngineEnum;
import com.utfinancing.financehub.engine.hthx.utils.HthxDateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author : robjiang
 * @Date : Create in 2024-04-22
 * @Description :  NonConfirmCollectionAccountChecking服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class NonConfirmCollectionAccountCheckingServiceImpl
        extends ServiceImpl<NonConfirmCollectionAccountCheckingMapper, NonConfirmCollectionAccountCheckingEntity>
        implements INonConfirmCollectionAccountCheckingService {

    private final NonConfirmCollectionAccountCheckingMapper nonConfirmCollectionAccountCheckingMapper;

    private final INonConfirmCollectionSumService nonConfirmCollectionSumService;
    private final RemoteDictService remoteDictService;

    private final IBatchModifyTemplateService batchModifyTemplateService;

    @Override
    public Long saveNonConfirmCollectionAccountChecking(NonConfirmCollectionAccountCheckingDTO dto) {
        NonConfirmCollectionAccountCheckingEntity entity = BeanUtil.copyProperties(dto, NonConfirmCollectionAccountCheckingEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateNonConfirmCollectionAccountChecking(Long id, NonConfirmCollectionAccountCheckingDTO dto) {
        NonConfirmCollectionAccountCheckingEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public NonConfirmCollectionAccountCheckingDTO getNonConfirmCollectionAccountCheckingDTOById(Long id) {
        NonConfirmCollectionAccountCheckingEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, NonConfirmCollectionAccountCheckingDTO.class);
    }

    @Override
    public IPage<NonConfirmCollectionAccountCheckingVO> selectPage(NonConfirmCollectionAccountCheckingQueryDTO queryDTO) {
        LambdaQueryWrapper<NonConfirmCollectionAccountCheckingEntity> queryWrapper = Wrappers.<NonConfirmCollectionAccountCheckingEntity>lambdaQuery();
        //这里注入查询条件
        IPage<NonConfirmCollectionAccountCheckingEntity> entityIPage = nonConfirmCollectionAccountCheckingMapper.
                selectPage(new Page<NonConfirmCollectionAccountCheckingEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, NonConfirmCollectionAccountCheckingVO.class);
    }

    /**
     * @description:未确认收款-对账表-导出按钮-查询对账导出数据
     * @author: zhangli.chen
     **/
    public List<AccountCheckingExportDTO> queryExportData(QueryNonConfirmAccountCheckingInputDTO params) {
        // 对账月份如果为空，则默认查询当前月
        if (StringUtils.isEmpty(params.getCheckingMonth())) {
            params.setCheckingMonth(DateUtil.format(DateUtil.date(), "yyyy-MM"));
        }
        // 返回结果
        List<AccountCheckingExportDTO> dataList = new ArrayList<>();
        // 查询条件
        SelectNonConfirmAccountCheckingDTO queryParams = BeanUtil.copyProperties(params, SelectNonConfirmAccountCheckingDTO.class);
        // 根据对账月份设置查询起始时间
        queryParams.setStartDate(DateUtil.beginOfMonth(DateUtil.parse(params.getCheckingMonth(), "yyyy-MM")));
        queryParams.setEndDate(DateUtil.endOfMonth(DateUtil.parse(params.getCheckingMonth(), "yyyy-MM")));
        String lastCheckingMonth = DateUtil.format(DateUtil.offsetMonth(DateUtil.parse(params.getCheckingMonth(), "yyyy-MM"), -1), "yyyy-MM");
        queryParams.setLastCheckingMonth(lastCheckingMonth);
        log.info("====>>NonConfirmCollectionAccountCheckingServiceImpl==>>queryExportData==>>00==>>queryParams:{}",queryParams);
        // 如果对账月份不为空, 则优先查询对账月份下是否已有对账数据； 若有则直接查询已对账数据，若没有则查询实时对账记录
        Integer count = nonConfirmCollectionAccountCheckingMapper.selectLastAccountCheckingCount(queryParams);
        // 系统编码转换
//        List<SysDictData> sysDictDataList = remoteDictService.listDictData(DictTypeEnum.SYS_FORM_SOURCE.getCode()).getData();
//        final Map<String,String> systemCodeMap = sysDictDataList.stream().collect(HashMap::new,
//                (map,item)->map.put(item.getDictValue(),item.getDictLabel()),HashMap::putAll);
        final Map<String,String> systemCodeMap  = SystemEnum.getEnumToMap();
        if (StringUtils.isNotEmpty(params.getCheckingMonth())) {
            String[] dateRange = HthxDateUtils.calculateDateRange(params.getCheckingMonth(), FinanceEngineEnum.Numbers.THREE.getKey());
            if(dateRange!=null && dateRange.length>0){
                queryParams.setFromStartDate(dateRange[0]);
                queryParams.setFromEndDate(dateRange[1]);
            }
        }
        // 若有则直接查询已对账数据
        if (count != null && count.intValue() > 0) {
            log.info("====>>NonConfirmCollectionAccountCheckingServiceImpl==>>queryExportData==>>01==>>queryParams:{}",queryParams);
            List<AccountCheckingExportDTO> nonConfirmCollectionAccountCheckingEntityList =
                    nonConfirmCollectionAccountCheckingMapper.accountingCheckingHistroyDataExport(queryParams);
            dataList = BeanUtil.copyToList(nonConfirmCollectionAccountCheckingEntityList, AccountCheckingExportDTO.class);
        } else {
            // 判断查询的对账月份与当前日期是否为同一月（或者上一月），如果为同一月，则优先查询是否已经存在已确认对账记录，如果没有则进行实时对账查询
            boolean isSameMonth = StringUtils.equals(params.getCheckingMonth(), DateUtil.format(DateUtil.date(), "yyyy-MM"));
            Date lastMonthDate = DateUtil.offsetMonth(DateUtil.date(), -1);
            boolean isLastMonth = StringUtils.equals(params.getCheckingMonth(), DateUtil.format(lastMonthDate, "yyyy-MM"));
            log.info("====>>NonConfirmCollectionAccountCheckingServiceImpl==>>queryExportData==>>02==>>" +
                    "isSameMonth:{},isLastMonth:{},queryParams:{}",isSameMonth,isLastMonth,queryParams);
            if (isSameMonth || isLastMonth) {
                log.info("====>>NonConfirmCollectionAccountCheckingServiceImpl==>>queryExportData==>>04==>>queryParams:{}",queryParams);
                List<AccountCheckingExportDTO> nonConfirmCollectionAccountCheckingEntityList =
                        nonConfirmCollectionAccountCheckingMapper.accountingCheckingDataExport(queryParams);
                if (nonConfirmCollectionAccountCheckingEntityList != null
                        && !nonConfirmCollectionAccountCheckingEntityList.isEmpty()) {
                    dataList = BeanUtil.copyToList(
                            nonConfirmCollectionAccountCheckingEntityList, AccountCheckingExportDTO.class);
                }
            }
        }
        log.info("====>>NonConfirmCollectionAccountCheckingServiceImpl==>>queryExportData==>>03==>>systemCodeMap:{}",systemCodeMap);
        // 使用并行流处理（当 dataList 超过 1000 条时）
        dataList.parallelStream().forEach(e -> {
            String original = e.getSystemCode();
            if (original == null || original.isEmpty()) {
                e.setSystemCode(FinanceEngineEnum.Symbol.NULL.getValue());
                return;
            }
            e.setSystemCode(Arrays.stream(original.split(","))
                            .map(String::trim)
                            .filter(part -> !part.isEmpty())
                            .map(code -> systemCodeMap.getOrDefault(code, code))
                            .collect(Collectors.joining(",")));
        });
        return dataList;
    }

    private static String getSystemName(String systemCode,Map<String, String> codeToNameMap) {
        if (systemCode == null || systemCode.isEmpty()){
            return FinanceEngineEnum.Symbol.NULL.getValue();
        }
        // 按原始顺序拼接结果-无映射时使用原code
        return Arrays.stream(systemCode.split(","))
                .map(String::trim)
                .map(code -> codeToNameMap.getOrDefault(code, code))
                .collect(Collectors.joining(","));
    }

    /**
     * 分页查询未确认收款的对账表数据
     */
    public IPage<QueryNonConfirmAccountCheckingOutputDTO> queryNonConfirmAccountChecking(
            QueryNonConfirmAccountCheckingInputDTO params) {
        // 对账月份如果为空，则默认查当前月得对账
        if (StringUtils.isEmpty(params.getCheckingMonth())) {
            params.setCheckingMonth(DateUtil.format(DateUtil.date(), "yyyy-MM"));
        }

        // 返回结果
        List<QueryNonConfirmAccountCheckingOutputDTO> dataList = new ArrayList<>();
        // 查询条件
        SelectNonConfirmAccountCheckingDTO queryParams = BeanUtil.copyProperties(
                params, SelectNonConfirmAccountCheckingDTO.class);
        queryParams.setStartDate(DateUtil.beginOfMonth(DateUtil.date()));
        queryParams.setEndDate(DateUtil.endOfMonth(DateUtil.date()));
        String lastCheckingMonth = DateUtil.format(DateUtil.offsetMonth(DateUtil.date(), -1), "yyyy-MM");
        queryParams.setLastCheckingMonth(lastCheckingMonth);

        // 如果对账月份不为空, 则优先查询历史对账记录是否存在； 不存在得情况下，则看对账月份是否为当前月，是：则查询实时对账记录
        Integer count = nonConfirmCollectionAccountCheckingMapper.selectLastAccountCheckingCount(queryParams);

        // 如果直接取历史的对账记录为
        if (count != null && count.intValue() > 0) {
            List<NonConfirmCollectionAccountCheckingEntity> nonConfirmCollectionAccountCheckingEntityList =
                    nonConfirmCollectionAccountCheckingMapper.selectLastAccountChecking(queryParams);
            dataList = BeanUtil.copyToList(
                    nonConfirmCollectionAccountCheckingEntityList, QueryNonConfirmAccountCheckingOutputDTO.class);
        } else {
            // 判断查询的对账月份与当前日期是否为同一月（或者上一月），如果为同一月，则优先查询是否已经存在已确认对账记录，如果没有则进行实时对账查询
            // An empty active summary table cannot produce a real-time reconciliation result.
            long activeSummaryCount = nonConfirmCollectionSumService.count(
                    Wrappers.<NonConfirmCollectionSumEntity>lambdaQuery()
                            .eq(NonConfirmCollectionSumEntity::getDelFlag, "0"));
            if (activeSummaryCount == 0) {
                return new Page<>(params.getPageNum(), params.getPageSize(), 0);
            }
            boolean isSameMonth = StringUtils.equals(params.getCheckingMonth(), DateUtil.format(DateUtil.date(), "yyyy-MM"));
            boolean isLastMonth = StringUtils.equals(params.getCheckingMonth(), lastCheckingMonth);
            if (isSameMonth || isLastMonth) {
                String[] dateRange = HthxDateUtils.calculateDateRange(params.getCheckingMonth(), FinanceEngineEnum.Numbers.THREE.getKey());
                log.info("====>>NonConfirmCollectionAccountCheckingServiceImpl==>>queryNonConfirmAccountChecking==>>03==>>" +
                        "params.getCheckingMonth(),dateRange:{}",params.getCheckingMonth(),dateRange);
                if(dateRange!=null && dateRange.length>0){
                    queryParams.setFromStartDate(dateRange[0]);
                    queryParams.setFromEndDate(dateRange[1]);
                }
                log.info("====>>NonConfirmCollectionAccountCheckingServiceImpl==>>queryNonConfirmAccountChecking==>>04==>>queryParams:{}",queryParams);
                count = nonConfirmCollectionAccountCheckingMapper.selectNonConfirmAccountCheckingCount(queryParams);
                if (count != null && count.intValue() > 0) {
                    List<NonConfirmCollectionAccountCheckingEntity> nonConfirmCollectionAccountCheckingEntityList =
                            nonConfirmCollectionAccountCheckingMapper.selectNonConfirmAccountChecking(queryParams);
                    this.accountCheckingProcess(nonConfirmCollectionAccountCheckingEntityList);
                    if (nonConfirmCollectionAccountCheckingEntityList != null
                            && !nonConfirmCollectionAccountCheckingEntityList.isEmpty()) {
                        dataList = BeanUtil.copyToList(
                                nonConfirmCollectionAccountCheckingEntityList, QueryNonConfirmAccountCheckingOutputDTO.class);
                    }
                }
            }
        }
        // 使用并行流处理（当 dataList 超过 1000 条时）
        Map<String,String> systemCodeMap  = SystemEnum.getEnumToMap();
        log.info("====>>NonConfirmCollectionAccountCheckingServiceImpl==>>queryNonConfirmAccountChecking==>>05==>>systemCodeMap:{}",systemCodeMap);
        dataList.parallelStream().forEach(e -> {
            String original = e.getSystemCode();
            if (original == null || original.isEmpty()) {
                e.setSystemCode(FinanceEngineEnum.Symbol.NULL.getValue());
                return;
            }
            e.setSystemCode(Arrays.stream(original.split(","))
                    .map(String::trim)
                    .filter(part -> !part.isEmpty())
                    .map(code -> systemCodeMap.getOrDefault(code, code))
                    .collect(Collectors.joining(",")));
        });
        IPage<QueryNonConfirmAccountCheckingOutputDTO> result = new Page<>(params.getPageNum(),params.getPageSize());
        result.setTotal(count);
        result.setRecords(dataList);
        return result;
    }

    /**
     * 对账数据处理
     */
    private void accountCheckingProcess(List<NonConfirmCollectionAccountCheckingEntity> nonConfirmCollectionAccountCheckingEntityList) {
        nonConfirmCollectionAccountCheckingEntityList.stream().forEach(e-> {
            if (e.getDiffAmount() != null && e.getDiffAmount().compareTo(BigDecimal.ZERO) == 0) {
                e.setAccountCheckingComments("ok");
            }
        });
    }

    /**
     * @description:未确认收款-对账表-确认对账按钮-确认对账
     * @author: zhangli.chen
     **/
    public String confirmAccountingChecking(ConfirmAccountingCheckingDTO params) {
        if (StringUtils.isEmpty(params.getCheckingMonth())) {
            return "对账月份不能为空!";
        }
        // 如果是当前月份或者是上月，则可以重复进行确认对账
        boolean isSameMonth = StringUtils.equals(params.getCheckingMonth(), DateUtil.format(DateUtil.date(), "yyyy-MM"));
        Date lastMonthDate = DateUtil.offsetMonth(DateUtil.date(), -1);
        boolean isLastMonth = StringUtils.equals(params.getCheckingMonth(), DateUtil.format(lastMonthDate, "yyyy-MM"));
        log.info("====>>NonConfirmCollectionAccountCheckingServiceImpl==>>confirmAccountingChecking==>>00==>>CheckingMonth:{},isSameMonth:{},lastMonthDate:{},isLastMonth:{}"
                ,params.getCheckingMonth(),isSameMonth,lastMonthDate,isLastMonth);
        if (isSameMonth || isLastMonth) {
            SelectNonConfirmAccountCheckingDTO queryParams = new SelectNonConfirmAccountCheckingDTO();
            queryParams.setCheckingMonth(params.getCheckingMonth());
            Integer count = nonConfirmCollectionAccountCheckingMapper.selectLastAccountCheckingCount(queryParams);
            if (count.intValue() > 0) {
                LambdaQueryWrapper<NonConfirmCollectionAccountCheckingEntity> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(NonConfirmCollectionAccountCheckingEntity::getAccountCheckingMonth, params.getCheckingMonth());
                nonConfirmCollectionAccountCheckingMapper.delete(wrapper);
            }
            // 对账入库
            queryParams.setPageNum(1);
            queryParams.setPageSize(Integer.MAX_VALUE);
            queryParams.setStartDate(DateUtil.beginOfMonth(DateUtil.parse(params.getCheckingMonth(), "yyyy-MM")));
            queryParams.setEndDate(DateUtil.endOfMonth(DateUtil.parse(params.getCheckingMonth(), "yyyy-MM")));
            String lastCheckingMonth = DateUtil.format(DateUtil.offsetMonth(
                    DateUtil.parse(params.getCheckingMonth(), "yyyy-MM"), -1), "yyyy-MM");
            queryParams.setLastCheckingMonth(lastCheckingMonth);
            log.info("====>>NonConfirmCollectionAccountCheckingServiceImpl==>>confirmAccountingChecking==>>01==>>queryParams:{}",queryParams);
            String[] dateRange = HthxDateUtils.calculateDateRange(params.getCheckingMonth(), FinanceEngineEnum.Numbers.THREE.getKey());
            log.info("====>>NonConfirmCollectionAccountCheckingServiceImpl==>>confirmAccountingChecking==>>03==>>" +
                    "params.getCheckingMonth(),dateRange:{}",params.getCheckingMonth(),dateRange);
            if(dateRange!=null && dateRange.length>0){
                queryParams.setFromStartDate(dateRange[0]);
                queryParams.setFromEndDate(dateRange[1]);
            }
            log.info("====>>NonConfirmCollectionAccountCheckingServiceImpl==>>confirmAccountingChecking==>>04==>>queryParams:{}",queryParams);
            List<NonConfirmCollectionAccountCheckingEntity> nonConfirmCollectionAccountCheckingEntityList =
                    nonConfirmCollectionAccountCheckingMapper.selectNonConfirmAccountChecking(queryParams);
            if (nonConfirmCollectionAccountCheckingEntityList != null
                    && !nonConfirmCollectionAccountCheckingEntityList.isEmpty()) {
                this.saveBatch(nonConfirmCollectionAccountCheckingEntityList);
            }
            return "对账完成!";
        } else {
            return "非对账日期，不能进行确认对账!";
        }
    }


    /**
     * @description:未确认收款-对账表-批量修改上传
     * @author: zhangli.chen
     **/
    public String batchModifyUpload(List<BatchModifyTemplateDTO> list) {
        if (list == null || list.isEmpty()) {
            return "批量修改上传文件不能为空!";
        }
        // 校验数据
        String errs = batchModifyUploadValidation(list);
        if (StringUtils.isNotEmpty(errs)) {
            return errs;
        }
        batchModifyTemplateService.clearTableData();
        // 从数据字典获取系统列表
        /***modify by zhangli.chen for 将系统别名的获取逻辑从枚举调整为了从数据字典中来获取 on 20250729 ***/
//        List<SysDictData> sysDictDataList = remoteDictService.listDictData(DictTypeEnum.SYS_FORM_SOURCE.getCode()).getData();
        /***modify by zhangli.chen for 因数据字典中将运营平台系统切分太细，需要切换至枚举来获取运营平台别名值 on 20250925 ***/
        Map<String,String> systemCodeMap  = SystemEnum.getEnumToMap();
        List<SysDictData> sysDictDataList = new ArrayList<>();
        for(Map.Entry<String,String> entry : systemCodeMap.entrySet()){
            SysDictData dictData = new SysDictData();
            dictData.setDictValue(entry.getKey());
            dictData.setDictLabel(entry.getValue());
            sysDictDataList.add(dictData);
        }
        list.stream().forEach(e -> {
            Optional<String> systemCodeOptional =  sysDictDataList.stream().filter(data-> (StringUtils.isNotEmpty(e.getSystemCode())
                    && StringUtils.trim(e.getSystemCode()).equals(StringUtils.trim(data.getDictLabel())))).map(SysDictData::getDictValue).findFirst();
            if(systemCodeOptional.isPresent()){
                e.setSystemCode(systemCodeOptional.get());
            }
        });


        List<BatchModifyTemplateEntity> batchModifyTemplateEntityList = BeanUtil.copyToList(
                list, BatchModifyTemplateEntity.class);
        batchModifyTemplateService.saveBatch(batchModifyTemplateEntityList);
        nonConfirmCollectionAccountCheckingMapper.batchModifyUploadSql();
        return "批量修改完成!";
    }

    /**
     * 批量修改上传校验
     */
    public String batchModifyUploadValidation(List<BatchModifyTemplateDTO> list) {


        List<String> businessKeyList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            BatchModifyTemplateDTO dto = list.get(i);
            if (StringUtils.isEmpty(dto.getAccountCheckingMonth())) {
                return "第" + (i + 1) + "行数据：对账月份不能为空!";
            }

            if (StringUtils.isEmpty(dto.getEbankSerialNumber())) {
                return "第" + (i + 1) + "行数据：业务系统的网银编号-小网银不能为空!";
            }

            businessKeyList.add(dto.getAccountCheckingMonth().concat(dto.getEbankSerialNumber()));
        }

        List<NonConfirmCollectionAccountCheckingEntity> dbAccountCheckingList =
                nonConfirmCollectionAccountCheckingMapper.selectAccountCheckingByBusKey(businessKeyList);
        if (dbAccountCheckingList == null || dbAccountCheckingList.isEmpty()) {
            return "本月还没有确认对账";
        }

        return StringUtil.EMPTY;
    }

    /**
     * 回租结果上传
     */
    public String nonLeaseResultUpload(List<UploadNonLeaseResultTemplateDTO> list, String isWaring) {
        if (list == null || list.isEmpty()) {
            return "上传文件不能为空!";
        }

        // 数据合并
        Map<String, UploadNonLeaseResultTemplateDTO> dataMap = this.dataCombine(list);

        // 查询现有db中的对账数据
        List<String> businessKeyList = dataMap.keySet().stream().collect(Collectors.toList());
        List<NonConfirmCollectionAccountCheckingEntity> dbAccountCheckingList =
                nonConfirmCollectionAccountCheckingMapper.selectAccountCheckingByBusKey(businessKeyList);
        Map<String, NonConfirmCollectionAccountCheckingEntity> dbAccountCheckingMap = dbAccountCheckingList.
                stream().collect(Collectors.toMap(e->this.getBusinessKey(e), (e)->e, (e1, e2)->e1));

        // 查询未确认收款汇总数据
        List<String> ebankSerialNumberList = list.stream().map(UploadNonLeaseResultTemplateDTO::getEbankSerialNumber).
                distinct().collect(Collectors.toList());
        Map<String, NonConfirmCollectionSumEntity> nonConfirmCollectionSumEntityMap = nonConfirmCollectionSumService.
                queryNonConfirmCollectionSumMap(ebankSerialNumberList.toArray(new String[ebankSerialNumberList.size()]));

        // 数据校验
        String errs = this.nonLeaseResultUploadValidation(dataMap, isWaring, dbAccountCheckingMap,
                nonConfirmCollectionSumEntityMap);
        if (StringUtils.isNotEmpty(errs)) {
            return errs;
        }

        // 保存入库
        LambdaUpdateWrapper<NonConfirmCollectionAccountCheckingEntity> wrapper = new LambdaUpdateWrapper();
        for (String key : dataMap.keySet()) {
            UploadNonLeaseResultTemplateDTO dto = dataMap.get(key);
            NonConfirmCollectionAccountCheckingEntity entity = dbAccountCheckingMap.get(key);
            if (entity != null) {
                wrapper = new LambdaUpdateWrapper();
                wrapper.eq(NonConfirmCollectionAccountCheckingEntity::getId, entity.getId());
                wrapper.set(NonConfirmCollectionAccountCheckingEntity::getNonLeaseNonClaimAmount, dto.getNonLeaseNonClaimAmount());
                wrapper.set(NonConfirmCollectionAccountCheckingEntity::getNonLeaseNonClaimReasons, dto.getNonLeaseNonClaimReasons());
                if (dto.getNonLeaseNonClaimAmount() != null
                        && dto.getNonLeaseNonClaimAmount().compareTo(BigDecimal.ZERO) > 0
                        && !"TA报表挂账".equals(entity.getFinancialPrimaryClassic())
                        && !"UID挂账".equals(entity.getFinancialPrimaryClassic())
                        && !"溢存款".equals(entity.getFinancialPrimaryClassic())) {
                    wrapper.set(NonConfirmCollectionAccountCheckingEntity::getFinancialPrimaryClassic, "非租款");
                }
                this.update(wrapper);
            } else {
                entity = BeanUtil.copyProperties(dto, NonConfirmCollectionAccountCheckingEntity.class);

                NonConfirmCollectionSumEntity nonConfirmCollectionSum = nonConfirmCollectionSumEntityMap.
                        get(dto.getEbankSerialNumber());
                entity.setCollectionAccountsBankCode(nonConfirmCollectionSum.getCollectionAccountsBankCode());
                entity.setCollectionAccountsBank(nonConfirmCollectionSum.getCollectionAccountsBank());
                if (dto.getNonLeaseNonClaimAmount() != null
                        && dto.getNonLeaseNonClaimAmount().compareTo(BigDecimal.ZERO) > 0) {
                    entity.setFinancialPrimaryClassic("非租款");
                }
                this.save(entity);
            }
        }
        return StringUtil.EMPTY;
    }

    /**
     * 取得业务主键
     */
    private String getBusinessKey(NonConfirmCollectionAccountCheckingEntity entity) {
        return entity.getAccountCheckingMonth().concat(entity.getEbankSerialNumber());
    }

    /**
     * 上传数据，如果存在同一业务主键得数据，则进行合并操作
     */
    private Map<String, UploadNonLeaseResultTemplateDTO> dataCombine(List<UploadNonLeaseResultTemplateDTO> list) {

        // 同一批次数据进行合并
        Map<String, UploadNonLeaseResultTemplateDTO> dataMap = new HashMap<>();
        for (UploadNonLeaseResultTemplateDTO dto : list) {
            String businessKey = dto.getAccountCheckingMonth().concat(dto.getEbankSerialNumber());
            UploadNonLeaseResultTemplateDTO data = dataMap.get(businessKey);
            if (data == null) {
                data = BeanUtil.copyProperties(dto, UploadNonLeaseResultTemplateDTO.class);
                if (data.getNonLeaseNonClaimAmount() == null) {
                    data.setNonLeaseNonClaimAmount(BigDecimal.ZERO);
                }
                if (StringUtils.isEmpty(data.getNonLeaseNonClaimReasons())) {
                    data.setNonLeaseNonClaimReasons(StringUtil.EMPTY);
                }
            } else {
                if (dto.getNonLeaseNonClaimAmount() == null) {
                    dto.setNonLeaseNonClaimAmount(BigDecimal.ZERO);
                }
                data.setNonLeaseNonClaimAmount(data.getNonLeaseNonClaimAmount().add(dto.getNonLeaseNonClaimAmount()));
                if (StringUtils.isNotEmpty(dto.getNonLeaseNonClaimReasons())) {
                    data.setNonLeaseNonClaimReasons(data.getNonLeaseNonClaimReasons().
                            concat(",").concat(dto.getNonLeaseNonClaimReasons()));
                }
            }
            dataMap.put(businessKey, data);
        }
        return dataMap;
    }

    /**
     * 回租结果上传-数据校验
     */
    public String nonLeaseResultUploadValidation(Map<String, UploadNonLeaseResultTemplateDTO> dataMap, String isWaring,
                                                 Map<String, NonConfirmCollectionAccountCheckingEntity> dbAccountCheckingMap,
                                                 Map<String, NonConfirmCollectionSumEntity> nonConfirmCollectionSumEntityMap) {
        log.info("====>>NonConfirmCollectionAccountCheckingServiceImpl.nonLeaseResultUploadValidation==00==>>isWaring:{}",isWaring);
        for (String key : dataMap.keySet()) {
            UploadNonLeaseResultTemplateDTO dto = dataMap.get(key);
            if (StringUtils.isEmpty(dto.getAccountCheckingMonth())) {
                return "对账月份不能为空!";
            }
            if (StringUtils.isEmpty(dto.getEbankSerialNumber())) {
                return "业务系统批扣流水号不能为空!";
            } else {
                NonConfirmCollectionSumEntity entity = nonConfirmCollectionSumEntityMap.get(dto.getEbankSerialNumber());
                if (entity == null) {
                    return String.format("业务系统批扣流水号:%s不存在!", dto.getEbankSerialNumber());
                }

                // 业务系统批扣流水号是否对应多个到账主体，若查到多个主体，则出现错误提示：“xxx（业务系统批扣流水号）对应A主体、
                // B主体…（对应的多个主体），请补充到账主体信息”，无法上传，补充到账主体后才可成功上传
                if (StringUtils.isEmpty(dto.getCollectionAccountsBank()) &&
                    entity.getCollectionAccountsBank().split(",").length > 1) {
                    return String.format("业务系统批扣流水号:%s存在多个到账主体:%s,请补充到账主体信息!",
                            dto.getEbankSerialNumber(), entity.getCollectionAccountsBank());
                }
            }

            if (dbAccountCheckingMap.containsKey(key)) {
                NonConfirmCollectionAccountCheckingEntity entity = dbAccountCheckingMap.get(key);
                if (YesOrNoEnum.NO.getCode().equals(isWaring)
                        && dto.getNonLeaseNonClaimAmount().compareTo(entity.getCurMonthBalance()) > 0) {
                    log.info("====>>NonConfirmCollectionAccountCheckingServiceImpl.nonLeaseResultUploadValidation" +
                            "==01==>>dto.getNonLeaseNonClaimAmount():{},entity.getCurMonthBalance():{}",
                            dto.getNonLeaseNonClaimAmount(),entity.getCurMonthBalance());
                    return "Warining";
                }
                if (dto.getNonLeaseNonClaimAmount().compareTo(entity.getCurMonthBalance()) > 0) {
                    log.info("====>>NonConfirmCollectionAccountCheckingServiceImpl.nonLeaseResultUploadValidation" +
                                    "==02==>>dto.getNonLeaseNonClaimAmount():{},entity.getCurMonthBalance():{}",
                            dto.getNonLeaseNonClaimAmount(),entity.getCurMonthBalance());
                    return String.format("业务系统批扣流水号:%s的非租未认领金额大于本月余额!",
                            dto.getEbankSerialNumber());
                }
            } else {
                return "本月还没有确认对账,不能进行非租未认领金额与本月余额进行比较!";
            }
        }
        return StringUtil.EMPTY;
    }


}

