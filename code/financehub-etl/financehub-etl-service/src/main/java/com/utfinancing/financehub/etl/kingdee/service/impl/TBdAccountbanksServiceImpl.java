package com.utfinancing.financehub.etl.kingdee.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.model.dto.BankAccountDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TBdAccountbanksQueryDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TBdAccountbanksDTO;
import com.utfinancing.financehub.etl.kingdee.model.vo.TBdAccountbanksVO;
import com.utfinancing.financehub.etl.kingdee.entity.TBdAccountbanksEntity;
import com.utfinancing.financehub.etl.kingdee.mapper.TBdAccountbanksMapper;
import com.utfinancing.financehub.etl.kingdee.service.ITBdAccountbanksService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description :  TBdAccountbanks服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TBdAccountbanksServiceImpl extends ServiceImpl<TBdAccountbanksMapper, TBdAccountbanksEntity> implements ITBdAccountbanksService {

    private final TBdAccountbanksMapper tBdAccountbanksMapper;

    @Override
    public List<BankAccountDTO> selectAllAccountBank() {
        return tBdAccountbanksMapper.selectAllAccountBank();
    }

}

