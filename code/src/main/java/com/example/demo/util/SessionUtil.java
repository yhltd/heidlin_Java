
package com.example.demo.util;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Session工具类
 *
 * @author dai
 */
@Component
public class SessionUtil {

    //session过期时间（秒）
    private static final Integer MAX_INTERVAL = 7200;

    /**
     * 检查token
     *
     * @return 是否过期
     */
    public static boolean checkToken(HttpSession session) {
        Object token = session.getAttribute("token");
        //Object power = session.getAttribute("power");
        return StringUtils.isNotNull(token);
                //&& StringUtils.isNotNull(power);
        //return StringUtils.isNotNull(token);
    }

    /**
     * set
     * @param session session对象
     * @param data 需要设置的值
     */
    public static void setToken(HttpSession session, String data) {
        //设置session过期时间
        session.setMaxInactiveInterval(MAX_INTERVAL);
        session.setAttribute("token", data);
    }

    /**
     * get
     * @param session session对象
     * @return 获取data
     */
    public static String getToken(HttpSession session) {
        return checkToken(session) ? session.getAttribute("token").toString() : StringUtils.EMPTY;
    }

    /**
     * 删除某个键
     * @param session
     * @param key
     */
    public static void remove(HttpSession session, String key) {
        session.removeAttribute(key);
    }

}