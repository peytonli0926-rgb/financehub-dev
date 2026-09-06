package com.utfinancing.financehub.engine.integration.util;

import cn.hutool.http.webservice.SoapClient;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 31/10/2023
 */
@Slf4j
public class EasWebServiceUtil {

    public static String send(String url, String method, Map<String, String> headers, Map<String, Object> params){
        SoapClient client = SoapClient.create(url).header("SOAPAction","");
        client.setMethod(method, "http://login.webservice.bos.kingdee.com");
        if (headers != null){
            client.addHeaders(headers);
        }
        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, Object> entry = it.next();
            client.setParam(entry.getKey(), entry.getValue(), false);
        }
        log.info("EasWebServiceUtil.send request: url:{}, method:{}, headers:{}, params:{}", url, method, headers, params);
        String response = client.send();
        log.info("EasWebServiceUtil.send response: {}", response);
        return response;
    }

}
