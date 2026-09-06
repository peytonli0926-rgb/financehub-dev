package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.finance.entity.OrgFundClaimJobRecordEntity;
import com.utfinancing.financehub.engine.finance.mapper.OrgFundClaimJobRecordMapper;
import com.utfinancing.financehub.engine.finance.model.dto.OrgClaimEbankNoQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OrgClaimEbankNoDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OrgClaimEbankNoSyncVO;
import com.utfinancing.financehub.engine.finance.model.vo.OrgClaimEbankNoVO;
import com.utfinancing.financehub.engine.finance.entity.OrgClaimEbankNoEntity;
import com.utfinancing.financehub.engine.finance.mapper.OrgClaimEbankNoMapper;
import com.utfinancing.financehub.engine.finance.service.IOfflineContractStructureService;
import com.utfinancing.financehub.engine.finance.service.IOrgClaimEbankNoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.service.IOrgFundClaimJobRecordService;
import com.utfinancing.financehub.engine.hthx.common.enums.FinanceEngineEnum;
import com.utfinancing.financehub.engine.rule.entity.InterfaceDataEntity;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author : robjiang
 * @Date : Create in 2025-06-06
 * @Description :  OrgClaimEbankNo服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class OrgClaimEbankNoServiceImpl extends ServiceImpl<OrgClaimEbankNoMapper, OrgClaimEbankNoEntity> implements IOrgClaimEbankNoService {

    private final OrgClaimEbankNoMapper orgClaimEbankNoMapper;

    @Autowired
    OrgFundClaimJobRecordMapper orgFundClaimJobRecordMapper;

    @Value("${external.interface.fund-org-claim-ebank}")
    private String requestUrl;


    @Override
    public Long saveOrgClaimEbankNo(OrgClaimEbankNoDTO dto) {
        OrgClaimEbankNoEntity entity = BeanUtil.copyProperties(dto, OrgClaimEbankNoEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateOrgClaimEbankNo(Long id, OrgClaimEbankNoDTO dto) {
        OrgClaimEbankNoEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public OrgClaimEbankNoDTO getOrgClaimEbankNoDTOById(Long id) {
        OrgClaimEbankNoEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, OrgClaimEbankNoDTO.class);
    }

    @Override
    public IPage<OrgClaimEbankNoVO> selectPage(OrgClaimEbankNoQueryDTO queryDTO) {
        LambdaQueryWrapper<OrgClaimEbankNoEntity> queryWrapper = Wrappers.<OrgClaimEbankNoEntity>lambdaQuery();
        //这里注入查询条件
        IPage<OrgClaimEbankNoEntity> entityIPage = orgClaimEbankNoMapper.selectPage(new Page<OrgClaimEbankNoEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, OrgClaimEbankNoVO.class);
    }

    /**
     * 同步机构认领的网银编号
     */
    public Map<String, Object> orgClaimEbankNoSync(Date startDate) {
        Date endDate = DateUtils.addDays(startDate, 1);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(FinanceEngineEnum.FundSystemQueryParameters.START_DATE.getKey(), DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, startDate));
        paramMap.put(FinanceEngineEnum.FundSystemQueryParameters.END_DATE.getKey(), DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, endDate));
        log.info("====>>orgClaimEbankNoSync==>>00==>>startDate:{},endDate:{},paramMap:{}",startDate,endDate,paramMap);
        String result = HttpUtil.createPost(requestUrl)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(JSON.toJSONString(paramMap))
                .execute()
                .body();
        JSONObject jsonObject = JSONObject.parseObject(result);
        log.info("====>>orgClaimEbankNoSync==>>01==>>请求资金系统完整报文==>>jsonObject:{}",jsonObject);
        String code = jsonObject.getString("code");
        if (!"200".equals(code)) {
            log.error(String.format("同步机构认领的网银编号(%s)-该接口返回报错信息:%s",
                    DateUtils.parseDateToStr("yyyy-MM-dd" , startDate), jsonObject.getString("msg")));
            throw new ServiceException("同步机构认领的网银编号-请求资金系统报错！");
        }
        JSONArray data = jsonObject.getJSONArray("data");
        if (data == null || data.isEmpty()) {
            log.info("====>>orgClaimEbankNoSync==>>02==>>同步机构认领的网银编号接口(查询日期{})-查询返回数据为空!",
                    DateUtils.parseDateToStr("yyyy-MM-dd", startDate));
            return paramMap;
        }
        log.info("====>>orgClaimEbankNoSync==>>03==>>同步机构认领的网银编号该接口返回数据数量：{}",data.size());
        LocalDateTime now = CommonDateUtils.parseDateToLocalDateTime(startDate);
        // 1. 收集所有银行编号
        Set<String> ebankNos = new HashSet<>(data.size());
        for (int i = 0; i < data.size(); i++) {
            ebankNos.add(data.getJSONObject(i).getString("wangybh"));
        }

        // 2. 分批查询现有记录（解决 IN 参数过多问题）
        Map<String, OrgClaimEbankNoEntity> existMap = new HashMap<>();
        // 使用 Lists.partition 分割大集合
        List<List<String>> partitionedEbankNos = Lists.partition(new ArrayList<>(ebankNos), FinanceEngineEnum.Numbers.THOUSAND.getKey());
        for (List<String> batch : partitionedEbankNos) {
            if (!batch.isEmpty()) {
                List<OrgClaimEbankNoEntity> batchEntities = this.list(
                        new LambdaQueryWrapper<OrgClaimEbankNoEntity>()
                                .in(OrgClaimEbankNoEntity::getEbankNo, batch)
                );
                batchEntities.forEach(entity -> existMap.put(entity.getEbankNo(), entity));
            }
        }

        // 3. 准备批量操作列表
        List<OrgClaimEbankNoEntity> batchInsert = new ArrayList<>();
        List<OrgClaimEbankNoEntity> batchUpdate = new ArrayList<>();
        // 4. 处理每条数据
        for (int i = 0; i < data.size(); i++) {
            JSONObject object = data.getJSONObject(i);
            OrgClaimEbankNoSyncVO javaObject = object.toJavaObject(OrgClaimEbankNoSyncVO.class);
            String ebankNo = javaObject.getWangybh();

            // 转换系统编码
            String systemCode = convertSystemCode(javaObject.getRljg());

            // 查找现有记录
            OrgClaimEbankNoEntity entity = existMap.get(ebankNo);

            if (entity == null) {
                OrgClaimEbankNoEntity newEntity = new OrgClaimEbankNoEntity();
                newEntity.setEbankNo(ebankNo);
                newEntity.setSystemCode(systemCode);
                newEntity.setCreateTime(LocalDateTime.now());
                newEntity.setUpdateTime(now);
                batchInsert.add(newEntity);
            } else {
                entity.setSystemCode(systemCode);
                entity.setUpdateTime(now);
                batchUpdate.add(entity);
            }
        }

        // 5. 批量执行数据库操作
        if (!batchInsert.isEmpty()) {
            log.info("====>>orgClaimEbankNoSync==>>04==>>待新增数据记录：{}",batchInsert);
            this.saveBatch(batchInsert, FinanceEngineEnum.Numbers.THOUSAND.getKey());
        }

        if (!batchUpdate.isEmpty()) {
            log.info("====>>orgClaimEbankNoSync==>>05==>>待更新数据记录：{}",batchUpdate);
            this.updateBatchById(batchUpdate, FinanceEngineEnum.Numbers.THOUSAND.getKey());
        }
        return paramMap;
    }

    /**
     * @description: 系统编码转换方法
     **/
    private String convertSystemCode(String rljg) {
        if (StringUtils.isBlank(rljg)) return StringUtils.EMPTY;
        switch (rljg) {
            case "小微": return SystemEnum.XWXT.getCode();
            case "恒信": return SystemEnum.TYPT.getCode();
            case "乘用车":
            case "cyc": return SystemEnum.CYCXT.getCode();
            case "商用车": return SystemEnum.SYCXT.getCode();
            case "恒运宝":
            case "恒运": return SystemEnum.HYB.getCode();
            case "现代物流":
            case "物流": return SystemEnum.HY_XDWL.getCode();
            case "运营平台": return SystemEnum.YYPT.getCode();
            case "供应链保理": return SystemEnum.GYLBL.getCode();
            case "贵安": return SystemEnum.GAXT.getCode();
            case "乘用车,商用车": return "CYCXT,SYCXT";
            default: return rljg;
        }
    }

}

