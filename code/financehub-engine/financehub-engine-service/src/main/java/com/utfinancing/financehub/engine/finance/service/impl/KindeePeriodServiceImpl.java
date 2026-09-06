package com.utfinancing.financehub.engine.finance.service.impl;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.service.KindeePeriodService;
import com.utfinancing.financehub.etl.api.RemoteKingdeeEasService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class KindeePeriodServiceImpl implements KindeePeriodService {

    private final RemoteKingdeeEasService remoteKingdeeEasService;

    @Override
    public Map<String, Integer> getAllOrgPeriodCode() {
        R<List<Map<String, String>>> periodCodeAll = remoteKingdeeEasService.getCurrentPeriodCodeAll();
        Map<String, Integer> periodCodeMap = new HashMap<>();
        for (Map<String, String> map : periodCodeAll.getData()) {
            periodCodeMap.put(String.valueOf(map.get("ORGID")), Integer.valueOf(map.get("PERIODCODE")));
        }
        return periodCodeMap;
    }
}
