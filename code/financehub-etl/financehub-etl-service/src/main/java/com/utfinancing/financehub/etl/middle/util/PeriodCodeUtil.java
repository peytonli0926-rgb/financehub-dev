package com.utfinancing.financehub.etl.middle.util;

import io.swagger.models.auth.In;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 02/12/2023
 */
public class PeriodCodeUtil {

    private static Map<Integer, Integer> nextPeriodMap;

    static
    {
        nextPeriodMap = new HashMap<>();
        nextPeriodMap.put(202101, 202102);
        nextPeriodMap.put(202102, 202103);
        nextPeriodMap.put(202103, 202104);
        nextPeriodMap.put(202104, 202105);
        nextPeriodMap.put(202105, 202106);
        nextPeriodMap.put(202106, 202107);
        nextPeriodMap.put(202107, 202108);
        nextPeriodMap.put(202108, 202109);
        nextPeriodMap.put(202109, 202110);
        nextPeriodMap.put(202110, 202111);
        nextPeriodMap.put(202111, 202112);
        nextPeriodMap.put(202112, 202201);

        nextPeriodMap.put(202201, 202202);
        nextPeriodMap.put(202202, 202203);
        nextPeriodMap.put(202203, 202204);
        nextPeriodMap.put(202204, 202205);
        nextPeriodMap.put(202205, 202206);
        nextPeriodMap.put(202206, 202207);
        nextPeriodMap.put(202207, 202208);
        nextPeriodMap.put(202208, 202209);
        nextPeriodMap.put(202209, 202210);
        nextPeriodMap.put(202210, 202211);
        nextPeriodMap.put(202211, 202212);
        nextPeriodMap.put(202212, 202301);

        nextPeriodMap.put(202301, 202302);
        nextPeriodMap.put(202302, 202303);
        nextPeriodMap.put(202303, 202304);
        nextPeriodMap.put(202304, 202305);
        nextPeriodMap.put(202305, 202306);
        nextPeriodMap.put(202306, 202307);
        nextPeriodMap.put(202307, 202308);
        nextPeriodMap.put(202308, 202309);
        nextPeriodMap.put(202309, 202310);
        nextPeriodMap.put(202310, 202311);
        nextPeriodMap.put(202311, 202312);

    }


    public static Integer getNextPeriodCode(Integer periodCode){
        return nextPeriodMap.get(periodCode);
    }

}
