package com.utfinancing.financehub.etl.kingdee.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.model.dto.VoucherTypeDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TBdVouchertypesQueryDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TBdVouchertypesDTO;
import com.utfinancing.financehub.etl.kingdee.model.vo.TBdVouchertypesVO;
import com.utfinancing.financehub.etl.kingdee.entity.TBdVouchertypesEntity;
import com.utfinancing.financehub.etl.kingdee.mapper.TBdVouchertypesMapper;
import com.utfinancing.financehub.etl.kingdee.service.ITBdVouchertypesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description :  TBdVouchertypes服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TBdVouchertypesServiceImpl extends ServiceImpl<TBdVouchertypesMapper, TBdVouchertypesEntity> implements ITBdVouchertypesService {

    private final TBdVouchertypesMapper tBdVouchertypesMapper;


    @Override
    public List<VoucherTypeDTO> selectAllVoucherTypes() {
        return tBdVouchertypesMapper.selectAllVoucherTypes();
    }
}

