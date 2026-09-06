package com.utfinancing.financehub.common.mybatis.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;

public class ListBeanUtil {

	public static <T> List<T> copyList(List sourceList, Class<T> targetClass){
		List<T> resultList = new ArrayList<>();
		for (Object obj: sourceList){
			T target = BeanUtil.copyProperties(obj, targetClass);
			resultList.add(target);
		}
		return resultList;
	}

	public static <T> List<T> copyList(List sourceList, Class<T> targetClass, String... ignoreProperties){
		List<T> resultList = new ArrayList<>();
		for (Object obj: sourceList){
			T target = BeanUtil.copyProperties(obj, targetClass, ignoreProperties);
			resultList.add(target);
		}
		return resultList;
	}

	public static <T> IPage<T> copyPage(IPage sourcePage, Class<T> targetClass){
		List<T> dataList = new ArrayList<>();
		IPage<T> resultPage = BeanUtil.copyProperties(sourcePage, Page.class,"records");
		if (CollectionUtil.isNotEmpty(sourcePage.getRecords())){
			for (Object obj: sourcePage.getRecords()){
				T target = BeanUtil.copyProperties(obj, targetClass);
				dataList.add(target);
			}
		}
		resultPage.setRecords(dataList);
		return resultPage;
	}

	public static <T> IPage<T> copyPageNoData(IPage sourcePage, Class<T> targetClass){
		IPage<T> resultPage = BeanUtil.copyProperties(sourcePage, Page.class,"records");
		resultPage.setRecords(new ArrayList<>());
		return resultPage;
	}

	public static void main(String[] args) {

	}

}
