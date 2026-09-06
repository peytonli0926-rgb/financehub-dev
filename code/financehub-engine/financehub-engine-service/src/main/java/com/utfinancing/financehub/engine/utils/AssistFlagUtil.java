package com.utfinancing.financehub.engine.utils;

import cn.hutool.core.collection.CollectionUtil;

import com.utfinancing.financehub.engine.enums.AssistFlagEnum;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 21/12/2023
 */
public class AssistFlagUtil {

    public static boolean hasClientFlag(List<String> assistFlags){
        return CollectionUtil.isEmpty(assistFlags)? false : assistFlags.contains(AssistFlagEnum.CLIENT.getCode());
    }
    public static boolean hasActualClientFlag(List<String> assistFlags){
        return CollectionUtil.isEmpty(assistFlags)? false : assistFlags.contains(AssistFlagEnum.ACTUAL_CLIENT.getCode());
    }

    public static boolean hasContractFlag(List<String> assistFlags){
        return CollectionUtil.isEmpty(assistFlags)? false : assistFlags.contains(AssistFlagEnum.CONTRACT.getCode());
    }

    public static boolean hasBillContractFlag(List<String> assistFlags){
        return CollectionUtil.isEmpty(assistFlags)? false : assistFlags.contains(AssistFlagEnum.BILL_CONTRACT.getCode());
    }


}
