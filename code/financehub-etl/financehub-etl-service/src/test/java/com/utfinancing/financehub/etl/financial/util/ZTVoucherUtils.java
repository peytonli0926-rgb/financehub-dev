package midground;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.webservice.SoapClient;
import com.alibaba.fastjson.JSON;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.etl.financial.util.ShowBoxUtil;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class ZTVoucherUtils {

    private static String sessionId;

    public static void main(String[] args) {
        String rows = ShowBoxUtil.getText();
//		IntStream.range(0, 10).parallel().forEach(i->{
//			log.info("模拟中台调用接口第 {} 次", ++i);
        callEAS(rows);
//		});
    }

    /**
     * 调用金蝶凭证接口
     * @param rows
     * @throws Exception
     */
    private static void callEAS(String rows) {
        System.out.println("开始时间"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        try {
            String ip = "http://192.168.13.64:9888";
            String LOGIN_URL = ip + "/ormrpc/services/EASLogin?wsdl";
            String BIZ_URL = ip + "/ormrpc/services/WSWSVoucher?wsdl";
            // 登录接口
            getSessionid(LOGIN_URL);
            // 调用业务接口
            String response = callBiz(BIZ_URL, rows);
            log.info(response);
            // 返回结果解析
            Node returnNode = XmlUtil.getNodeByXPath("//nImportVoucherReturn", XmlUtil.parseXml(response));
            Map result = new HashMap();
            result = JSON.parseObject(returnNode.getTextContent(), Map.class);
            if (result == null || result.keySet().size() == 0) {
                throw new ServiceException(StrUtil.format("EAS返回数据为空，完整响应内容：{}", response));
            }
            System.out.println(result);
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        System.out.println("结束时间"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    /**
     * 业务接口
     *
     * @param url
     * @param params
     * @param rows
     * @return
     * @throws SOAPException
     */
    private static String callBiz(String BIZ_URL, String rows) throws SOAPException {
        // 业务接口参数
        String params = "{" + "    \"isCompress\": \"false\"," + "    \"useGlobalCache\": \"true\","
                + "    \"isAudit\": \"false\"," + "    \"isImpCashflow\": \"false\","
                + "    \"isSubmit\": \"false\"," + "    \"clearGlobalCache\": \"true\","
                + "    \"isPost\": \"false\"," + "    \"overwrite\": \"true\","
                + "    \"extend\": \"true\"," + "    \"isVoucherTypeIdField\": \"true\"" + "}";
        // 封装header

//        SoapClient client = SoapClient.create(BIZ_URL).header("SOAPAction", "");
//        client.setMethod("web:nImportVoucher", "http://schemas.xmlsoap.org/soap/encoding/");
//        SOAPHeaderElement soapHeaderElement = client.getMessage().getSOAPHeader().addHeaderElement(new QName("http://login.webservice.bos.kingdee.com","SessionId"));
//        soapHeaderElement.addChildElement("SessionId").setValue(sessionId);

        SoapClient client = SoapClient.create(BIZ_URL).header("SOAPAction", "");
        client.setMethod("web:nImportVoucher", "http://login.webservice.bos.kingdee.com");
        SOAPMessage msg = client.getMessage();
        SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
        SOAPHeader hdr = env.getHeader();
        QName e = new QName("http://login.webservice.bos.kingdee.com", "SessionId");
        SOAPHeaderElement vv = hdr.addHeaderElement(e);
        vv.setValue(sessionId);
        client.setParam("params", params, false);
        client.setParam("rows", rows, false);
        msg.saveChanges();

        String response = client.send();
        //log.info("EasWebServiceUtil.send response: {}", response);
        return response;
    }



    private static void getSessionid(String LOGIN_URL) throws ServiceException {
        Map<String, Object> param = new LinkedHashMap<>(10);
		param.put("user", "CWZT");
		param.put("password", "UT@202412@");
//        param.put("user", "100343");
//        param.put("password", " Jay-1943@cdw1996");
        param.put("slnName", "eas");
        param.put("dcName", "HX001");
        param.put("language", "L2");
        param.put("dbType", 2);
        String response = callLogin(LOGIN_URL, "login", null, param);
        // 解析xml,获取响应
        Node sessionNode = XmlUtil.getNodeByXPath("//sessionId", XmlUtil.parseXml(response));
        if (sessionNode == null) {
            throw new ServiceException("获取金蝶sessionId失败");
        }
        sessionId = sessionNode.getTextContent();
        System.out.println(sessionId);
        if (StrUtil.isBlank(sessionId)) {
            throw new ServiceException("获取金蝶sessionId失败, sessionId为空");
        }
    }

    private static String callLogin(String url, String method, Map<String, String> headers,
                                    Map<String, Object> params) {
        SoapClient client = SoapClient.create(url).header("SOAPAction", "");
        client.setMethod(method, "http://login.webservice.bos.kingdee.com");
        if (headers != null) {
            client.addHeaders(headers);
        }
        for (String key : params.keySet()) {
            client.setParam(key, params.get(key), false);
        }
        //log.info("EasWebServiceUtil.send request: url:{}, method:{}, headers:{}, params:{}", url, method, headers, params);
        String response = client.send();
        //log.info("EasWebServiceUtil.send response: {}", response);
        return response;
    }
}
