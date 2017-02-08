package com.business.common.http.token;

import com.business.common.CommonTools;
import com.business.common.context.SpringContextUtil;
import com.business.common.other.Files.MD5Util;
import com.business.common.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


/**
 * @author yuton
 * @ClassName: SessionUtil
 * @Description: session 统一管理处理
 * @date 2012-3-19 上午10:39:04
 */
@Slf4j
public class SessionUtil extends CommonTools {

    private static final String COOKIE_USER_KEY = "OUTSOURCED_USER_TOKEN";
    private static final Long TIMEOUT = 60 * 60 * 6L; //保存2小时
    @SuppressWarnings("unchecked")
    private static RedisUtil<String, Object> redisUtil = SpringContextUtil.getBean(RedisUtil.class);

    /**
     * @param request
     * @param response
     * @param name
     * @param value
     * @return void
     * @throws Exception
     * @Title: setSessionAttribute
     * @Description: 保存会话变量
     */
    public static String setSessionAttribute(HttpServletRequest request,
                                             HttpServletResponse response, String name, Object value) {
        String sessionKey = getSessionKey(request, response);
        String key = MD5Util.getMD5Encode(sessionKey, name);
        redisUtil.set(key, value, TIMEOUT);
        return key;
    }

    /**
     * @param request
     * @param response
     * @param name
     * @param value
     * @return void
     * @Title: setSessionAttributeString
     * @Description: 保存会话变量
     */
    public static void setSessionAttributeString(HttpServletRequest request,
                                                 HttpServletResponse response, String name, String value) {
        setSessionAttribute(request, response, name, value);
    }


    /**
     * @param request
     * @param name
     * @return String
     * @Title: getSessionAttributeString
     * @Description: 获取会话变量
     */
    public static String getSessionAttributeString(HttpServletRequest request, String name) {
        String sessionKey = getSessionKey(request);
        return (!isEmpty(sessionKey) ? (String) redisUtil.get(MD5Util.getMD5Encode(sessionKey, name)) : null);
    }


    /**
     * @param <T>
     * @param request
     * @param name
     * @return T
     * @throws Exception
     * @Title: getSessionAttribute
     * @Description: 获取会话变量值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getSessionAttribute(HttpServletRequest request, String name) throws Exception {
        String sessionKey = getSessionKey(request);
        return (!isEmpty(sessionKey) ? (T) redisUtil.get(MD5Util.getMD5Encode(sessionKey, name)) : null);
    }

    /**
     * @param request
     * @param name
     * @return void
     * @Title: removeSessionAttribute
     * @Description: 移除会话变量
     */
    public static void removeSessionAttribute(HttpServletRequest request,
                                              String name) {
        String sessionKey = getSessionKey(request);
        if (sessionKey != null) {
            redisUtil.delete(MD5Util.getMD5Encode(sessionKey,name) + name);
        }
    }

    /**
     * @param request
     * @param response
     * @return boolean
     * @Title: removeSessionKey
     * @Description: 移出会话Key
     */
    public static boolean removeSessionKey(HttpServletRequest request, HttpServletResponse response) {
        String sessionKey = getSessionKey(request);
        if (!isEmpty(sessionKey)) {
            CookieUtil.removeCookieByName(request, response, COOKIE_USER_KEY);
            return true;
        }
        return false;
    }

    /**
     * @param request
     * @param response
     * @param name
     * @return
     * @Description: 移出Session
     */
    public static boolean removeSession(HttpServletRequest request, HttpServletResponse response, String name) {
        String sessionKey = getSessionKey(request);
        if (!isEmpty(sessionKey)) {
            CookieUtil.removeCookieByName(request, response, COOKIE_USER_KEY);
            redisUtil.delete(sessionKey + name);
            return true;
        }
        return false;
    }

    /**
     * @return {@link String}
     * @Title: getSessionKey
     * @Description: 获取会话Key, 不存在返回null
     */
    public static String getSessionKey(HttpServletRequest request) {
        return CookieUtil.getCookieByName(request, COOKIE_USER_KEY);
    }

    /**
     * @return {@link String}
     * @Title: getSessionKey
     * @Description: 获取会话Key, 不存在返回新Key
     */
    public static String getSessionKey(HttpServletRequest request, HttpServletResponse response) {
        String sessionKey = getSessionKey(request);
        if (sessionKey == null) {
            sessionKey = UUID.randomUUID().toString();
            CookieUtil.setCookie(request, response, COOKIE_USER_KEY, sessionKey);
        }
        return sessionKey;
    }

    /**
     * @return {@link String}
     * @Title: getNewSessionKey
     * @Description: 添加并获取新会话Key
     */
    public static String getNewSessionKey(HttpServletRequest request, HttpServletResponse response) {
        String sessionKey = UUID.randomUUID().toString();
        CookieUtil.setCookie(request, response, COOKIE_USER_KEY, sessionKey);
        return sessionKey;
    }

    /**
     * @param request
     * @return {@link String}
     * @Description: 获取会话的sessionId
     */
    public static String getSessionId(HttpServletRequest request) {
        return request.getSession().getId();
    }

}
