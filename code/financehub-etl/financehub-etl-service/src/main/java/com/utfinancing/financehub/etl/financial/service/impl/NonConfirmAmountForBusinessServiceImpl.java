package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.commveh.service.ICommercialVehicleDataService;
import com.utfinancing.financehub.etl.enums.SystemEnum;
import com.utfinancing.financehub.etl.financial.model.dto.NonConfirmAmountForBusinessQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.NonConfirmAmountForBusinessDTO;
import com.utfinancing.financehub.etl.financial.model.vo.NonConfirmAmountForBusinessVO;
import com.utfinancing.financehub.etl.financial.entity.NonConfirmAmountForBusinessEntity;
import com.utfinancing.financehub.etl.financial.mapper.NonConfirmAmountForBusinessMapper;
import com.utfinancing.financehub.etl.financial.service.INonConfirmAmountForBusinessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.micro.service.IMicroDataService;
import com.utfinancing.financehub.etl.passveh.service.IPassengerVehicleDataService;
import com.utfinancing.financehub.etl.platform.model.SelectNonConfirmAmountOutputDTO;
import com.utfinancing.financehub.etl.platform.service.IPlatformDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @Author : robjiang
 * @Date : Create in 2024-07-02
 * @Description :  NonConfirmAmountForBusiness服务实现类
 * @Modified :
 */
@Service
@Slf4j
public class NonConfirmAmountForBusinessServiceImpl
        extends ServiceImpl<NonConfirmAmountForBusinessMapper, NonConfirmAmountForBusinessEntity>
        implements INonConfirmAmountForBusinessService {

    @Resource
    private NonConfirmAmountForBusinessMapper nonConfirmAmountForBusinessMapper;

    @Resource
    private IPlatformDataService platformDataService;

    @Resource
    private IMicroDataService microDataService;

    @Resource
    private IPassengerVehicleDataService passengerVehicleDataService;

    @Resource
    private ICommercialVehicleDataService commercialVehicleDataService;

    @Override
    public Long saveNonConfirmAmountForBusiness(NonConfirmAmountForBusinessDTO dto) {
        NonConfirmAmountForBusinessEntity entity = BeanUtil.copyProperties(dto, NonConfirmAmountForBusinessEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateNonConfirmAmountForBusiness(Long id, NonConfirmAmountForBusinessDTO dto) {
        NonConfirmAmountForBusinessEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public NonConfirmAmountForBusinessDTO getNonConfirmAmountForBusinessDTOById(Long id) {
        NonConfirmAmountForBusinessEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, NonConfirmAmountForBusinessDTO.class);
    }

    @Override
    public IPage<NonConfirmAmountForBusinessVO> selectPage(NonConfirmAmountForBusinessQueryDTO queryDTO) {
        LambdaQueryWrapper<NonConfirmAmountForBusinessEntity> queryWrapper = Wrappers.<NonConfirmAmountForBusinessEntity>lambdaQuery();
        //这里注入查询条件
        IPage<NonConfirmAmountForBusinessEntity> entityIPage = nonConfirmAmountForBusinessMapper.selectPage(new Page<NonConfirmAmountForBusinessEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, NonConfirmAmountForBusinessVO.class);
    }

    /**
     * 从业务系统同步未确认金额
     */
    public void nonConfirmAmountSyncJob() {
        Date nowDate = DateUtils.getNowDate();

        // 清空表数据
        this.remove(new LambdaQueryWrapper<>());

        // 统一平台未确认金额同步
        List<SelectNonConfirmAmountOutputDTO> selectNonConfirmAmountOutputDTOList =
                platformDataService.queryNonConfirmAmount(nowDate);
        List<NonConfirmAmountForBusinessEntity> entityList = new ArrayList<>();
        if (selectNonConfirmAmountOutputDTOList != null && !selectNonConfirmAmountOutputDTOList.isEmpty()) {
            for (SelectNonConfirmAmountOutputDTO dto : selectNonConfirmAmountOutputDTOList) {
                NonConfirmAmountForBusinessEntity entity = new NonConfirmAmountForBusinessEntity();
                entity.setId(IdWorker.getId());
                entity.setSystemAmount(dto.getNonConfirmedAmount());
                entity.setAccountCheckingMonth(DateUtils.parseDateToStr("yyyy-MM-dd", nowDate));
                entity.setSystemCode(SystemEnum.TYPT.getCode());
                entity.setEbankSerialNumber(dto.getEbankSerialNumber());
                entityList.add(entity);
            }
            this.saveBatch(entityList);
            entityList.clear();
        }

//         小微未确认金额同步
        List<com.utfinancing.financehub.etl.micro.model.SelectNonConfirmAmountOutputDTO> microNonConfirmAmountList =
                microDataService.queryNonConfirmAmount(nowDate);
        if (microNonConfirmAmountList != null && !microNonConfirmAmountList.isEmpty()) {
            for (com.utfinancing.financehub.etl.micro.model.SelectNonConfirmAmountOutputDTO dto : microNonConfirmAmountList) {
                NonConfirmAmountForBusinessEntity entity = new NonConfirmAmountForBusinessEntity();
                entity.setId(IdWorker.getId());
                entity.setSystemAmount(dto.getNonConfirmedAmount());
                entity.setAccountCheckingMonth(DateUtils.parseDateToStr("yyyy-MM-dd", nowDate));
                entity.setSystemCode(SystemEnum.XWXT.getCode());
                entity.setEbankSerialNumber(dto.getEbankSerialNumber());
                entityList.add(entity);
            }
            this.saveBatch(entityList);
            entityList.clear();
        }

        // 乘用车未确认金额同步
        List<com.utfinancing.financehub.etl.passveh.model.SelectNonConfirmAmountOutputDTO> cycxtNonConfirmAmountList =
                passengerVehicleDataService.queryNonConfirmAmount(nowDate);
        if (cycxtNonConfirmAmountList != null && !cycxtNonConfirmAmountList.isEmpty()) {
            for (com.utfinancing.financehub.etl.passveh.model.SelectNonConfirmAmountOutputDTO dto : cycxtNonConfirmAmountList) {
                NonConfirmAmountForBusinessEntity entity = new NonConfirmAmountForBusinessEntity();
                entity.setId(IdWorker.getId());
                entity.setSystemAmount(dto.getDecKehxje());
                entity.setAccountCheckingMonth(DateUtils.parseDateToStr("yyyy-MM-dd", nowDate));
                entity.setSystemCode(SystemEnum.CYCXT.getCode());
                entity.setEbankSerialNumber(dto.getVcWangybHy());
                entityList.add(entity);
            }
            this.saveBatch(entityList);
            entityList.clear();
        }

        // 商用车未确认金额同步
        List<com.utfinancing.financehub.etl.commveh.model.SelectNonConfirmAmountOutputDTO> sycxtNonConfirmAmountList =
                commercialVehicleDataService.queryNonConfirmAmount(nowDate);
        if (sycxtNonConfirmAmountList != null && !sycxtNonConfirmAmountList.isEmpty()) {
            for (com.utfinancing.financehub.etl.commveh.model.SelectNonConfirmAmountOutputDTO dto : sycxtNonConfirmAmountList) {
                NonConfirmAmountForBusinessEntity entity = new NonConfirmAmountForBusinessEntity();
                entity.setId(IdWorker.getId());
                entity.setSystemAmount(dto.getDecKehxje());
                entity.setAccountCheckingMonth(DateUtils.parseDateToStr("yyyy-MM-dd", nowDate));
                entity.setSystemCode(SystemEnum.SYCXT.getCode());
                entity.setEbankSerialNumber(dto.getVcWangybHy());
                entityList.add(entity);
            }
            this.saveBatch(entityList);
            entityList.clear();
        }
    }
}

