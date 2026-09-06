package com.utfinancing.financehub.engine.hthx.base;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.utfinancing.financehub.engine.hthx.page.PageParam;
import com.utfinancing.financehub.engine.hthx.page.PageResult;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;


public class MyBaseServiceImpl<M extends MyBaseMapper<T>, T> extends ServiceImpl implements MyBaseService {


    @Override
    public <T> PageResult selectByPage(String funcName, T mapper, PageParam pageParam) throws Exception {
        Field[] declaredFields = pageParam.getClass().getDeclaredFields();
        boolean flag = false;
        if (declaredFields.length > 0) {
            flag = true;
        }
        // 分页PageHelper插件处理
        int startRow = pageParam.getPageNum();
        int pageSize = pageParam.getPageSize();
        String field = pageParam.getField();
        String order = pageParam.getOrder();
        PageHelper.startPage(startRow, pageSize);
        if (StrUtil.isNotBlank(field)){
            PageHelper.orderBy(StrUtil.toUnderlineCase(field) + " " + order);
        }
        // 数据库检索处理
        Method m1;
        List list;
        if (!flag) {
            m1 = mapper.getClass().getDeclaredMethod(funcName);
            list = (List) m1.invoke(mapper);
        } else {
            m1 = mapper.getClass().getDeclaredMethod(funcName, pageParam.getClass());
            list = (List) m1.invoke(mapper, pageParam);
        }
        PageInfo pageInfo = new PageInfo<>(list);
        // 封装数据
        PageResult pageResult = new PageResult();
        pageResult.setRecords(pageInfo.getList());
        pageResult.setPageNum(pageInfo.getPageNum());
        pageResult.setPageSize(pageInfo.getPageSize());
        pageResult.setTotal((int) pageInfo.getTotal());
        pageResult.setTotalPage(pageInfo.getPages());
        return pageResult;
    }
}
