package com.utfinancing.financehub.engine.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionFileUploadRecordEntity;
import com.utfinancing.financehub.engine.finance.mapper.NonConfirmCollectionFileUploadRecordMapper;
import com.utfinancing.financehub.engine.finance.service.INonConfirmCollectionFileUploadRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * @Author : robjiang
 * @Date : Create in 2024-05-21
 * @Description :  NonConfirmCollectionFileUploadRecord服务实现类
 * @Modified :
 */
@Service
@Transactional
public class NonConfirmCollectionFileUploadRecordServiceImpl
        extends ServiceImpl<NonConfirmCollectionFileUploadRecordMapper, NonConfirmCollectionFileUploadRecordEntity>
        implements INonConfirmCollectionFileUploadRecordService {

    @Resource
    private NonConfirmCollectionFileUploadRecordMapper nonConfirmCollectionFileUploadRecordMapper;

    /**
     * 取得最新记录
     */
    public NonConfirmCollectionFileUploadRecordEntity getLastRecord() {
        String staffCode = UserUtils.getStaffCode();
        if (StringUtils.isEmpty(staffCode)) {
            return null;
        }

        LambdaQueryWrapper<NonConfirmCollectionFileUploadRecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NonConfirmCollectionFileUploadRecordEntity::getUploader, staffCode);
        wrapper.orderByDesc(NonConfirmCollectionFileUploadRecordEntity::getUploadTime);
        List<NonConfirmCollectionFileUploadRecordEntity> list = nonConfirmCollectionFileUploadRecordMapper.selectList(wrapper);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}

