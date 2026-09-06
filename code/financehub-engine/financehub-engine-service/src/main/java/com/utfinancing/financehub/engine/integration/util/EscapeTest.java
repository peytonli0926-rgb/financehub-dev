package com.utfinancing.financehub.engine.integration.util;

import cn.hutool.core.util.EscapeUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.sql.SQLOutput;

/**
 * @Author : lixin
 * @Date : Create in 31/10/2023
 */
public class EscapeTest {

    public static void main(String[] args) {
        String s = "[{&quot;voucherNumber&quot;:&quot;20231031-001&quot;,&quot;status&quot;:&quot;&#x5931;&#x8D25;&quot;,&quot;flag&quot;:&quot;1111&quot;,&quot;log&quot;:&quot;&#x51ED;&#x8BC1;&#x6570;&#x636E;&#x4E2D;&#x516C;&#x53F8;&#x7F16;&#x7801;&lt;&#x53C2;&#x6570;&#xFF1A;companyNumber&#xFF1B; &#x503C;&#xFF1A;01-C0001aa&gt;&#xFF0C;&#x65E0;&#x6CD5;&#x4E0E;EAS&#x516C;&#x53F8;&#x57FA;&#x7840;&#x8D44;&#x6599;&#x76F8;&#x5339;&#x914D;&#xFF01;&#x4F4D;&#x4E8E;&#x5206;&#x5F55;&#x884C; 2&#x3002; &quot;,&quot;system&quot;:&quot;CWZT&quot;},{&quot;voucherNumber&quot;:&quot;20231031-001&quot;,&quot;periodYear&quot;:&quot;0&quot;,&quot;periodMonth&quot;:&quot;0&quot;,&quot;status&quot;:&quot;&#x5931;&#x8D25;&quot;,&quot;flag&quot;:&quot;4129&quot;,&quot;log&quot;:&quot;&#x51ED;&#x8BC1;&#x5E94;&#x8BE5;&#x81F3;&#x5C11;&#x5B58;&#x5728;&#x4E24;&#x6761;&#x5206;&#x5F55;&#x3002;&quot;,&quot;system&quot;:&quot;CWZT&quot;}]";
        System.out.println("unescape: "+EscapeUtil.unescape(s));
        System.out.println("unescapeHtml4: "+EscapeUtil.unescapeHtml4(s));
        System.out.println("unescapeXml: "+EscapeUtil.unescapeXml(s));
        System.out.println("safeUnescape: "+EscapeUtil.safeUnescape(s));

        System.out.println(EscapeUtil.unescapeXml("&#x81EA;&#x52A8;&#x8F6C;&#x8D26;"));

        JSONArray jsonObject = JSONArray.parseArray("[\n" +
                "\t{\n" +
                "\t\t\"voucherNumber\": \"20231031-001\",\n" +
                "\t\t\"status\": \"失败\",\n" +
                "\t\t\"flag\": \"1111\",\n" +
                "\t\t\"log\": \"凭证数据中币别编码<参数：currencyNumber； 值：001>，无法与EAS币别基础资料相匹配！位于分录行 1。 \",\n" +
                "\t\t\"system\": \"CWZT\"\n" +
                "\t}\n" +
                "]");
        System.out.println(jsonObject.getJSONObject(0).getString("log"));

    }


}
