package com.utfinancing.financehub.common.mybatis.util;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;

public class PageBeanUtil {

	public static <T> IPage<T> copyList(IPage sourcePage, Class<T> targetClass){
		IPage<T> resultPage = new Page<>(sourcePage.getCurrent(), sourcePage.getSize(), sourcePage.getTotal());
		List<T> targetList = new ArrayList<>();
		for (Object obj: sourcePage.getRecords()){
			T target = BeanUtil.copyProperties(obj, targetClass);
			targetList.add(target);
		}
		resultPage.setRecords(targetList);
		return resultPage;
	}

}
