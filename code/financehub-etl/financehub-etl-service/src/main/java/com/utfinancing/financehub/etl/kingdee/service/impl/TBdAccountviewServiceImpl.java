package com.utfinancing.financehub.etl.kingdee.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeAccountDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TBdAccountviewQueryDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TBdAccountviewDTO;
import com.utfinancing.financehub.etl.kingdee.model.vo.TBdAccountviewVO;
import com.utfinancing.financehub.etl.kingdee.entity.TBdAccountviewEntity;
import com.utfinancing.financehub.etl.kingdee.mapper.TBdAccountviewMapper;
import com.utfinancing.financehub.etl.kingdee.service.ITBdAccountviewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description :  TBdAccountview服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TBdAccountviewServiceImpl extends ServiceImpl<TBdAccountviewMapper, TBdAccountviewEntity> implements ITBdAccountviewService {

    private final TBdAccountviewMapper tBdAccountviewMapper;

    @Override
    public List<KingdeeAccountDTO> selectAllAccount() {
        return tBdAccountviewMapper.selectAllAccount();
    }

}

