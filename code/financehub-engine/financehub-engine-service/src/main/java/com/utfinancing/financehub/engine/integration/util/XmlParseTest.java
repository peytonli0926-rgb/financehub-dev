package com.utfinancing.financehub.engine.integration.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.escape.XmlUnescape;
import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson2.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 31/10/2023
 */
public class XmlParseTest {

    public static void main(String[] args) {
        String xmlStr = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Body><ns1:addResponse soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns1=\"http://tempuri.org/xxx.xsd\"><addReturn xsi:type=\"xsd:string\">[{&quot;voucherNumber&quot;:&quot;20231031-001&quot;,&quot;status&quot;:&quot;&#x5931;&#x8D25;&quot;,&quot;flag&quot;:&quot;1111&quot;,&quot;log&quot;:&quot;&#x51ED;&#x8BC1;&#x6570;&#x636E;&#x4E2D;&#x516C;&#x53F8;&#x7F16;&#x7801;&lt;&#x53C2;&#x6570;&#xFF1A;companyNumber&#xFF1B; &#x503C;&#xFF1A;01-C0001aaa&gt;&#xFF0C;&#x65E0;&#x6CD5;&#x4E0E;EAS&#x516C;&#x53F8;&#x57FA;&#x7840;&#x8D44;&#x6599;&#x76F8;&#x5339;&#x914D;&#xFF01;&#x4F4D;&#x4E8E;&#x5206;&#x5F55;&#x884C; 2&#x3002; &quot;,&quot;system&quot;:&quot;CWZT&quot;},{&quot;voucherNumber&quot;:&quot;20231031-001&quot;,&quot;periodYear&quot;:&quot;0&quot;,&quot;periodMonth&quot;:&quot;0&quot;,&quot;status&quot;:&quot;&#x5931;&#x8D25;&quot;,&quot;flag&quot;:&quot;4129&quot;,&quot;log&quot;:&quot;&#x51ED;&#x8BC1;&#x5E94;&#x8BE5;&#x81F3;&#x5C11;&#x5B58;&#x5728;&#x4E24;&#x6761;&#x5206;&#x5F55;&#x3002;&quot;,&quot;system&quot;:&quot;CWZT&quot;}]</addReturn></ns1:addResponse></soapenv:Body></soapenv:Envelope>";
        Document document = XmlUtil.parseXml(xmlStr);
        Element rootElement = XmlUtil.getRootElement(document);
        System.out.println(rootElement.getNodeName());
        Element bodyElement = XmlUtil.getElement(rootElement, "soapenv:Body");
        System.out.println(bodyElement.getNodeName());
        System.out.println(XmlUtil.getNodeByXPath("//addReturn", XmlUtil.parseXml(xmlStr)).getTextContent());
    }

}
