
package com.plat.auto.test.autotest.common.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @Author: 丑牛
 * @Date: 2021-10-28 13:39:41
 * @LastEditors: 丑牛
 * @LastEditTime: 2021-10-28 13:39:48
 * @Description: cookie工具类
 */
public class CookieUtil {
     // 默认缓存时间，单位秒
    private static final int COOKIE_MAX_AGE = 60 * 60 * 2;
    // 保存路径
    private static final String COOKIE_PATH = "/";

    /**
     * @description: 保存
     * @param {HttpServletResponse} response
     * @param {String} key
     * @param {String} value
     * @param {boolean} ifRemember
     * @return {*}
     * @Author: 丑牛
     */

    public static void set(HttpServletResponse response, String key, String value, boolean ifRemember){
        int age = ifRemember?COOKIE_MAX_AGE:-1;
        set(response, key, value, null, COOKIE_PATH, age, true);
    }

    /**
     * @description: 重写保存
     * @param {HttpServletResponse} response
     * @param {String} key
     * @param {String} value
     * @param {String} domain
     * @param {String} path
     * @param {int} maxAge
     * @param {boolean} isHttpOnly
     * @return {*}
     * @Author: 丑牛
     */
    public static void set(HttpServletResponse response, String key, String value, String domain, String path, int maxAge, boolean isHttpOnly){
        Cookie cookie = new Cookie(key, value);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(isHttpOnly);
        response.addCookie(cookie);
    }


    /**
     * @description: 查询cookie
     * @param {HttpServletRequest} request
     * @param {String} key
     * @return cookie
     * @Author: 丑牛
     */
	private static Cookie get(HttpServletRequest request, String key) {
		Cookie[] arr_cookie = request.getCookies();
		if (arr_cookie != null && arr_cookie.length > 0) {
			for (Cookie cookie : arr_cookie) {
				if (cookie.getName().equals(key)) {
					return cookie;
				}
			}
		}
		return null;
	}

    /**
     * @description: 查询value
     * @param {HttpServletRequest} request
     * @param {String} key
     * @return value
     * @Author: 丑牛
     */
    public static String getValue(HttpServletRequest request, String key){
        Cookie cookie = get(request, key);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    /**
     * @description: 删除cookie
     * @param {HttpServletRequest} request
     * @param {HttpServletResponse} response
     * @param {String} key
     * @return {*}
     * @Author: 丑牛
     */
    public static void remove(HttpServletRequest request, HttpServletResponse response, String key) {
        Cookie cookie = get(request, key);
        if (cookie != null) {
            set(response, key, "", null, COOKIE_PATH, 0, true);
        }
    }

     
 }
