package com.utfinancing.financehub.engine.utils;
import com.alibaba.druid.pool.ha.PropertiesUtils;
import com.kingdee.eas.cp.eip.sso.ltpa.LtpaTokenManager;
import com.utfinancing.financehub.admin.api.model.LoginUser;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.Resource;
import java.net.URL;
import java.util.Properties;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.utils.UserUtils</li>
 * <li>CreateTime : 2024/01/09 10:00</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Slf4j
public class UserUtils {

    @Resource
    private static ResourceLoader resourceLoader;

    /**
     * 获取登录用户工号
     * @return
     */
    public static String getStaffCode(){
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (null == loginUser) {
            return "system";
        }
        return loginUser.getStaffCode();
    }

    /**
     * 获取登录用户名
     * @return
     */
    public static String getStaffName(){
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (null == loginUser) {
            return "system";
        }
        return loginUser.getStaffName();
    }

    public static void main(String[] args) {
//        List<Map<String,Object>> items = Lists.newArrayList();
//        Map<String,Object> map2 = new HashMap<>();
//        map2.put("businessCode","1");
//        map2.put("id",2);
//        items.add(map2);
//        Map<String,Object> map3 = new HashMap<>();
//        map3.put("businessCode","2");
//        map3.put("id",1);
//        items.add(map3);
//        Map<String,Object> map4 = new HashMap<>();
//        map4.put("businessCode","1");
//        map4.put("id",3);
//        items.add(map4);
//        Map<String,Object> map1 = new HashMap<>();
//        map1.put("businessCode","1");
//        map1.put("id",1);
//        items.add(map1);
//        // 假设我们已经根据category属性对items进行了排序
////        items.sort(Comparator.comparingInt(Item::getCategory));
//
//        Map<String, List<Map<String,Object>>> groupedItems = items.stream()
//                .collect(Collectors.groupingBy(v -> MapUtil.getStr(v, RuleConstant.FIELD_BUSINESS_CODE)));
//        log.info("结果："+ JSON.toJSONString(groupedItems));
//        // 此时groupedItems的每个列表中的元素应该保持了它们在原始列表中的排序
        getKindeeLoginPassword("");
    }

    public static String getKindeeLoginPassword(String parth){
        log.info("path："+parth);
        String password = LtpaTokenManager.generate(getStaffCode(),parth).toString();
        log.info("password:"+password);
        return password;
    }
}
