package com.utfinancing.financehub.common.core.utils;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.DES;

import java.nio.charset.StandardCharsets;

/**
 * @Author : lixin
 * @Date : Create in 31/10/2023
 */
public class SecureUtil {


    /**
     * DES 加密
     * @param key
     * @param content
     * @return
     */
    public static String desEncrypt(String key, String content){
        return cn.hutool.crypto.SecureUtil.des(key.getBytes(StandardCharsets.UTF_8)).encryptHex(content);
    }

    /**
     * DES 解密，解密失败返回null
     * @param key
     * @param content
     * @return
     */
    public static String desDecrypt(String key, String content){
        String decryptStr = null;
        try {
            decryptStr = cn.hutool.crypto.SecureUtil.des(key.getBytes(StandardCharsets.UTF_8)).decryptStr(content);
        }catch (Exception e){
            return null;
        }
        return decryptStr;
    }



}
