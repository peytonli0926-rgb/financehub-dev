package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.engine.constants.RedisConstant;
import com.utfinancing.financehub.engine.enums.CloseAccountStatusEnum;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.CloseAccountQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CloseAccountDTO;
import com.utfinancing.financehub.engine.finance.model.vo.CloseAccountVO;
import com.utfinancing.financehub.engine.finance.entity.CloseAccountEntity;
import com.utfinancing.financehub.engine.finance.mapper.CloseAccountMapper;
import com.utfinancing.financehub.engine.finance.service.ICloseAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.scene.model.dto.SceneDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author : bruyang
 * @Date : Create in 2024-01-16
 * @Description :  CloseAccount服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class CloseAccountServiceImpl extends ServiceImpl<CloseAccountMapper, CloseAccountEntity> implements ICloseAccountService {

    private final CloseAccountMapper closeAccountMapper;
    private final RedisService redisService;

    @Override
    public Long saveCloseAccount(CloseAccountDTO dto) {
        CloseAccountEntity entity = BeanUtil.copyProperties(dto, CloseAccountEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateCloseAccount(Long id, CloseAccountDTO dto) {
        CloseAccountEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public CloseAccountDTO getCloseAccountDTOById(Long id) {
        CloseAccountEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, CloseAccountDTO.class);
    }

    @Override
    public IPage<CloseAccountVO> selectPage(CloseAccountQueryDTO queryDTO) {
        LambdaQueryWrapper<CloseAccountEntity> queryWrapper = Wrappers.<CloseAccountEntity>lambdaQuery();
        //这里注入查询条件
        IPage<CloseAccountEntity> entityIPage = closeAccountMapper.selectPage(new Page<CloseAccountEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, CloseAccountVO.class);
    }

    @Override
    public Integer queryCurrentPeriodCode(String systemCode) {
        Map<String, List<CloseAccountEntity>> allCloseAccountEntityMap = getCloseAccountEntityFromRedis();
        List<CloseAccountEntity> closeAccountEntityList = allCloseAccountEntityMap.get(systemCode.concat("|").
                concat(CloseAccountStatusEnum.VALID.getCode()));
        if (closeAccountEntityList == null || closeAccountEntityList.isEmpty()) {
            return null;
        }

        CloseAccountEntity entity = closeAccountEntityList.stream().
                sorted(Comparator.comparingInt(CloseAccountEntity::getYear).reversed()).
                sorted(Comparator.comparingInt(CloseAccountEntity::getMonth).reversed()).findFirst().get();
        if (entity == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, entity.getYear());
        //Calendar的month是从0开始，这里不减1，表示加一个月，即当前打开的会计期间
        calendar.set(Calendar.MONTH, entity.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return NumberUtil.parseInt(DateUtil.format(calendar.getTime(), "yyyyMM"));
    }

    @Override
    public CloseAccountEntity queryCloseDate(String systemCode) {
        Map<String, List<CloseAccountEntity>> allCloseAccountEntityMap = getCloseAccountEntityFromRedis();
        List<CloseAccountEntity> closeAccountEntityList = allCloseAccountEntityMap.get(systemCode.concat("|").
                concat(CloseAccountStatusEnum.VALID.getCode()));
        if (closeAccountEntityList == null || closeAccountEntityList.isEmpty()) {
            return null;
        }
        return closeAccountEntityList.stream().
                sorted(Comparator.comparingInt(CloseAccountEntity::getYear).reversed().
                        thenComparing(Comparator.comparingInt(CloseAccountEntity::getMonth).reversed())).findFirst().get();
    }

    public Map<String, List<CloseAccountEntity>> getCloseAccountEntityFromRedis() {
        List<CloseAccountEntity> closeAccountEntityList = redisService.getCacheObject(RedisConstant.CLOSE_ACCOUNT_PERIOD);
        if (closeAccountEntityList == null || closeAccountEntityList.isEmpty()) {
            LambdaQueryWrapper<CloseAccountEntity> wrapper = new LambdaQueryWrapper();
            wrapper.eq(CloseAccountEntity::getDelFlag, YesOrNoEnum.NO.getCode());
            wrapper.eq(CloseAccountEntity::getStatus, CloseAccountStatusEnum.VALID.getCode());
            wrapper.orderByDesc(CloseAccountEntity::getYear);
            wrapper.orderByDesc(CloseAccountEntity::getMonth);
            closeAccountEntityList = closeAccountMapper.selectList(wrapper);
            redisService.setCacheObject(RedisConstant.CLOSE_ACCOUNT_PERIOD, closeAccountEntityList, RedisConstant.TIME_OUT, TimeUnit.MINUTES);
        }
        return closeAccountEntityList.stream().collect(Collectors.groupingBy(e->getBusKey(e)));
    }

    public String getBusKey(CloseAccountEntity entity) {
        StringBuffer result = new StringBuffer(entity.getSystemCode());
        result.append("|").append(entity.getStatus());
        return result.toString();
    }
}

