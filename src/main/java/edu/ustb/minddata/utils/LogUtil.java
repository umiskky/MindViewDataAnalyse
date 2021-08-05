package edu.ustb.minddata.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author UmiSkky
 */
public class LogUtil {

    /**
     * 输出请求信息
     * @param req HttpServletRequest
     * @return String
     */
    public static String interfaceLog(HttpServletRequest req){
        return "[HTTP Request] "
                + (req.getHeader("x-forwarded-for") == null ? req.getRemoteAddr() : req.getHeader("x-forwarded-for"))
                + " "
                + req.getMethod()
                + ": "
                + req.getRequestURI();
    }
}
