package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.DictTypeEnum;
import com.utfinancing.financehub.engine.finance.entity.RentRegisterEntity;
import com.utfinancing.financehub.engine.finance.mapper.RentRegisterMapper;
import com.utfinancing.financehub.engine.finance.model.dto.RentRegisterDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.RentRegisterDetailDTO;
import com.utfinancing.financehub.engine.finance.model.vo.RentRegisterDetailExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.RentRegisterDetailVO;
import com.utfinancing.financehub.engine.finance.entity.RentRegisterDetailEntity;
import com.utfinancing.financehub.engine.finance.mapper.RentRegisterDetailMapper;
import com.utfinancing.financehub.engine.finance.service.IRentRegisterDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-08
 * @Description :  RentRegisterDetail服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class RentRegisterDetailServiceImpl extends ServiceImpl<RentRegisterDetailMapper, RentRegisterDetailEntity> implements IRentRegisterDetailService {

    private final RentRegisterDetailMapper rentRegisterDetailMapper;
    private final RentRegisterMapper rentRegisterMapper;

    private final RemoteDictService remoteDictService;

    @Override
    public Long saveRentRegisterDetail(RentRegisterDetailDTO dto) {
        RentRegisterDetailEntity entity = BeanUtil.copyProperties(dto, RentRegisterDetailEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateRentRegisterDetail(Long id, RentRegisterDetailDTO dto) {
        RentRegisterDetailEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public RentRegisterDetailDTO getRentRegisterDetailDTOById(Long id) {
        RentRegisterDetailEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, RentRegisterDetailDTO.class);
    }

    @Override
    public IPage<RentRegisterDetailVO> selectPage(RentRegisterDetailQueryDTO queryDTO) {
        Page page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        IPage<RentRegisterDetailVO> entityIPage = rentRegisterDetailMapper.selectPageByMapper(page, queryDTO);
        return entityIPage;
    }

    @Override
    public List<RentRegisterDetailExcelVO> selectList(RentRegisterDetailQueryDTO queryDTO) {
        List<RentRegisterDetailVO> entityIPage = rentRegisterDetailMapper.selectPageByMapper(queryDTO);
        return ListBeanUtil.copyList(entityIPage, RentRegisterDetailExcelVO.class);
    }

    /**
     * 计算金额
     *
     * @param contractCodeList
     */
    @Override
    public void calculateAmount(List<String> contractCodeList) {
        // 获取税率
        BigDecimal taxRate;
        R<List<SysDictData>> taxRateR = remoteDictService.listDictData(DictTypeEnum.SYS_TAX_RATE_BONDED_ASSETS.getCode());
        if (ObjectUtil.isNotEmpty(taxRateR) && ObjectUtil.isNotEmpty(taxRateR.getData())) {
            // 取第一个税率
            taxRate = new BigDecimal(taxRateR.getData().get(0).getDictLabel());
        } else {
            taxRate = BigDecimal.ZERO;
        }
        for (String contractCode : contractCodeList) {
            List<RentRegisterDetailEntity> rentRegisterDetailEntityList = list(new LambdaQueryWrapper<RentRegisterDetailEntity>().eq(RentRegisterDetailEntity::getContractCode, contractCode));
            if (CollectionUtils.isEmpty(rentRegisterDetailEntityList)) {
                continue;
            }
            List<LocalDate> planDateList = rentRegisterDetailEntityList.stream().map(RentRegisterDetailEntity::getPlanDate).sorted().collect(Collectors.toList());
            LocalDate startDate = planDateList.get(0);
            LocalDate endDate = planDateList.get(planDateList.size() - 1);
            // 总天数
            long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
            // 总金额
            BigDecimal totalAmt = rentRegisterDetailEntityList.stream().filter(a -> ObjectUtil.isNotEmpty(a.getReceivableRent())).map(RentRegisterDetailEntity::getReceivableRent).reduce(BigDecimal.ZERO, BigDecimal::add);

            rentRegisterDetailEntityList.stream().forEach(a -> {
                BigDecimal thisMonthReceivableRent = BigDecimal.ZERO;
                // 当月应收租金=应收租金（上传）汇总*当月天数/所有天数
                if (ObjectUtil.equals(a.getPlanDate(), startDate)) {
                    // 计算从计划日期到该月最后一天的天数
                    long daysUntilEndOfMonth = ChronoUnit.DAYS.between(a.getPlanDate(), a.getPlanDate().with(TemporalAdjusters.lastDayOfMonth())) + 1;
                    thisMonthReceivableRent = totalAmt.multiply(BigDecimal.valueOf(daysUntilEndOfMonth))
                            .divide(BigDecimal.valueOf(totalDays), 2, BigDecimal.ROUND_HALF_UP);
                } else if (ObjectUtil.equals(a.getPlanDate(), endDate)) {
                    // 计算从当月第一天到计划日期的天数
                    long daysFromStartOfMonth = ChronoUnit.DAYS.between(a.getPlanDate().with(TemporalAdjusters.firstDayOfMonth()), a.getPlanDate()) + 1;
                    thisMonthReceivableRent = totalAmt.multiply(BigDecimal.valueOf(daysFromStartOfMonth))
                            .divide(BigDecimal.valueOf(totalDays), 2, BigDecimal.ROUND_HALF_UP);
                } else {
                    // 当月所有天数
                    int totalDaysInMonth = a.getPlanDate().lengthOfMonth();
                    thisMonthReceivableRent = totalAmt.multiply(BigDecimal.valueOf(totalDaysInMonth))
                            .divide(BigDecimal.valueOf(totalDays), 2, BigDecimal.ROUND_HALF_UP);
                }
                a.setThisMonthReceivableRent(thisMonthReceivableRent);
                // 当月计提税金 =当月应收租金/（1+税率）*税率
                BigDecimal thisMonthTax = thisMonthReceivableRent.multiply(taxRate).divide(BigDecimal.ONE.add(taxRate), 2, BigDecimal.ROUND_HALF_UP);
                a.setThisMonthTax(thisMonthTax);
                // 当月租金收入=当月应收租金-当月计提税金
                a.setThisMonthRentIncome(thisMonthReceivableRent.subtract(thisMonthTax));

            });

            // 更新租金明细
            this.updateBatchById(rentRegisterDetailEntityList);

            //更新头上的金额
            List<RentRegisterEntity> rentRegisterEntityList = rentRegisterMapper.selectList(new LambdaQueryWrapper<RentRegisterEntity>().eq(RentRegisterEntity::getContractCode, contractCode));
            Date leaseDateStart = DateUtils.toDate(rentRegisterDetailEntityList.stream().min(Comparator.comparing(RentRegisterDetailEntity::getPlanDate)).map(RentRegisterDetailEntity::getPlanDate).orElse(null));
            Date leaseDateEnd = DateUtils.toDate(rentRegisterDetailEntityList.stream().max(Comparator.comparing(RentRegisterDetailEntity::getPlanDate)).map(RentRegisterDetailEntity::getPlanDate).orElse(null));

            for (RentRegisterEntity rentRegisterEntity : rentRegisterEntityList) {
                rentRegisterEntity.setLeaseDateStart(leaseDateStart);
                rentRegisterEntity.setLeaseDateEnd(leaseDateEnd);
                rentRegisterEntity.setRentTotal(totalAmt);
                rentRegisterMapper.updateById(rentRegisterEntity);
            }
        }

    }

    /**
     * 根据合同编号查询明细
     *
     * @param contractCodeList
     * @return
     */
    @Override
    public List<RentRegisterDetailVO> selectByContractCodeList(List<String> contractCodeList) {
        List<RentRegisterDetailEntity> list = list(new LambdaQueryWrapper<RentRegisterDetailEntity>().in(RentRegisterDetailEntity::getContractCode, contractCodeList));
        return ListBeanUtil.copyList(list, RentRegisterDetailVO.class);
    }

    /**
     * 根据出租登记id 删除租金计划
     * @param rentRegisterId
     */
    @Override
    public void removeByRentRegisterId(Long rentRegisterId) {
        if(ObjectUtil.isNotEmpty(rentRegisterId)){
            remove(new LambdaQueryWrapper<RentRegisterDetailEntity>().in(RentRegisterDetailEntity::getRentRegisterId,rentRegisterId));
        }
    }

}

