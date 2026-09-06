package com.utfinancing.financehub.etl.financial.util;

import cn.hutool.http.webservice.SoapClient;
import lombok.extern.slf4j.Slf4j;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
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
        for (String key: params.keySet()){
            client.setParam(key, params.get(key), false);
        }
        log.info("EasWebServiceUtil.send request: url:{}, method:{}, headers:{}, params:{}", url, method, headers, params);
        String response = client.send();
        log.info("EasWebServiceUtil.send response: {}", response);
        return response;
    }

    public static String send1(String url, String method, String sessionId, Map<String, Object> params) throws SOAPException {
//        SoapClient client = SoapClient.create(url).header("SOAPAction","");
//        client.setMethod(method, "http://schemas.xmlsoap.org/soap/encoding/");
//        SOAPHeaderElement soapHeaderElement = client.getMessage().getSOAPHeader().addHeaderElement(new QName("http://login.webservice.bos.kingdee.com",sessionId));
//        soapHeaderElement.addChildElement("SessionId").setValue(sessionId);

        SoapClient client = SoapClient.create(url).header("SOAPAction", "");
        client.setMethod(method, "http://login.webservice.bos.kingdee.com");
        SOAPMessage msg = client.getMessage();
        SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
        SOAPHeader hdr = env.getHeader();
        QName e = new QName("http://login.webservice.bos.kingdee.com", "SessionId");
        SOAPHeaderElement vv = hdr.addHeaderElement(e);
        vv.setValue(sessionId);
        for (String key: params.keySet()){
            client.setParam(key, params.get(key), false);
        }
        msg.saveChanges();
        String response = client.send();
        return response;
    }
}
