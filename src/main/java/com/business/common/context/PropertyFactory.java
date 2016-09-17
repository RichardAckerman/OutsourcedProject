/**
 * Filename:	PropertyFactory.java
 * Description:
 * Copyright:   Copyright (c)2011
 * Company:    easy
 *
 * @author: guosheng.zhu
 * @version: 1.0
 * Create at:   2011-6-10 下午08:08:37
 * <p>
 * Modification History:
 * Date           Author       Version      Description
 * ------------------------------------------------------------------
 * 2011-6-10    guosheng.zhu       1.0        1.0 Version
 */
package com.business.common.context;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * @ClassName: PropertyFactory
 * @Description: 属性加载工具类
 * @date 2011-6-10 下午08:08:37
 */
@Slf4j
public class PropertyFactory {

    private static Map<String, Properties> propMap = null;

    /**
     * @Title: getProperty
     * @Description: 获取属性数据
     * @param @param filePath
     * @param @param name
     * @param @return
     * @return String
     */
    public static String getProperty(String filePath, String name) {
        if (propMap == null) {
            propMap = new HashMap<>();
        }
        String key = getFileNameFromPath(filePath);
        if (!propMap.containsKey(key)) {
            Properties prop = new Properties();
            try {
                InputStream is = new BufferedInputStream(new FileInputStream(
                        filePath));
                prop.load(is);
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                return null;
            }
            propMap.put(key, prop);
            return prop.getProperty(name);
        }
        return propMap.get(key).getProperty(name);
    }

    /**
     * @Title: getFileNameFromPath
     * @Description: 根据路径解析文件名
     * @param @param path
     * @param @return
     * @return String
     */
    private static String getFileNameFromPath(String path) {
        int location = path.lastIndexOf("/");
        if (location == -1) {
            return null;
        }
        String name = path.substring(location + 1);
        return name;
    }

//    /**
//     *
//     * @Title: getPdf2swfLanguagePath
//     * @Description: 获取pdf2swf_language安装路径
//     * @param @return
//     * @return String
//     */
//    public static String getPdf2swfLanguagePath() {
//        return getProperty(BasePathFactory.getResourcePath("other.properties"), "pdf2swf_language");
//    }
//
//    public static String getImageBasePath() {
//        return getProperty(BasePathFactory.getResourcePath("other.properties"),
//                "picpath");
//    }

}
