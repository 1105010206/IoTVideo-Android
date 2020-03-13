/*
 * Copyright (c) 2018 THL A29 Limited, a Tencent company. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.tencentcs.iotvideodemo.accountmgrtc.httpservice;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 签名工具类
 */
public class Sign {
    private final static Charset UTF8 = StandardCharsets.UTF_8;
    
    /**
     * 
     * @param sigStr
     * @param secretKey
     * @param sigMethod
     * @return string  sign string
     */
    public static String sign(String secretKey, String sigStr, String sigMethod) {
    	String sig = null;
    	try{
	    	Mac mac = Mac.getInstance(sigMethod);
	        byte[] hash;
	        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(UTF8), mac.getAlgorithm());

	        mac.init(secretKeySpec);
	        hash = mac.doFinal(sigStr.getBytes(UTF8));
	        sig = byteToHex(hash);
    	} catch (Exception e) {
            e.printStackTrace();
    	}
        return sig;	
    }
    
    public static String makeSignPlainText(TreeMap<String, String> requestParams, String reqMethod, String host, String path) {

        String retStr = "";
        retStr += reqMethod;
        retStr += host;
        retStr += path;
        retStr += buildParamStr(requestParams, reqMethod);
        return retStr;
    }

    protected static String buildParamStr(TreeMap<String, String> requestParams, String requestMethod) {

        String retStr = "";
        for(String key: requestParams.keySet()) {
            String value = requestParams.get(key);
            if (retStr.length() == 0) {
                retStr += '?';
            } else {
                retStr += '&';
            }
            retStr += key.replace("_", ".") + '=' + value;

        }
        return retStr;
    }

    public static String sha256Hex(String s) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] d = md.digest(s.getBytes(UTF8));
        return byteToHex(d).toLowerCase();
    }

    public static String sha256Hex(byte[] b) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] d = md.digest(b);
        return byteToHex(d).toLowerCase();
    }
    
    public static byte[] hmac256(byte[] key, String msg) {
        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, mac.getAlgorithm());
        try {
            mac.init(secretKeySpec);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return mac.doFinal(msg.getBytes(UTF8));
    }

    public static String byteToHex(byte[] bytes) {
        String strHex = "";
        StringBuilder sb = new StringBuilder("");
        for (byte aByte : bytes) {
            strHex = Integer.toHexString(aByte & 0xFF);
            sb.append((strHex.length() == 1) ? "0" + strHex : strHex); // 每个字节由两个字符表示，位数不够，高位补0
        }
        return sb.toString().trim();
    }
}
