package com.plat.auto.test.autotest.service;

import com.plat.auto.test.autotest.entity.ReturnT;
import com.plat.auto.test.autotest.entity.User;
import com.plat.auto.test.autotest.mapper.UserMapper;
import com.plat.auto.test.autotest.util.CookieUtil;
import com.plat.auto.test.autotest.util.JacksonUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/11
 * @description
 */
@Configuration
public class LoginService {
    public static final String LOGIN_IDENTITY = "LOGIN_IDENTITY";
    @Resource
    private UserMapper userMapper;

    private String makeToken(User user) {
        String tokenHex = null;
        String tokenJson = JacksonUtil.writeValueAsString(user);
        if (tokenJson != null) {
            tokenHex = new BigInteger(tokenJson.getBytes()).toString(16);
        }

        return tokenHex;
    }

    private User parseToken(String tokenHex) {
        User user = null;
        if (tokenHex != null) {
            String tokenJson = new String(new BigInteger(tokenHex, 16).toByteArray());
            user = JacksonUtil.readValue(tokenJson, User.class);
        }
        return user;
    }
    /**
     * @Description: login
     * @Param: [response, userNameParam, passwordParam, ifRemember]
     * @return: com.plat.auto.test.autotest.entity.ReturnT<java.lang.String>
     * @Author: wangzhaoxian
     * @Date: 2021/11/12
     */
    public ReturnT<String> login(HttpServletResponse response, String userNameParam, String passwordParam, boolean ifRemember) {
        User user = userMapper.findByUserName(userNameParam);
        if (user == null) {
            return new ReturnT<String>(500, "账号或密码错误");
        }
        String passWordParamMd5 = DigestUtils.md5DigestAsHex(passwordParam.getBytes());
        if (!user.getPassWord().equals(passWordParamMd5)) {
            return new ReturnT<String>(500, "账号或密码错误");
        }
        String loginToken = makeToken(user);
        CookieUtil.set(response, LOGIN_IDENTITY, loginToken, ifRemember);
        return ReturnT.SUCCESS;
    }
    /**
     * @Description: logout
     * @Param: [request, response]
     * @return: void
     * @Author: wangzhaoxian
     * @Date: 2021/11/12
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.remove(request, response, LOGIN_IDENTITY);
    }
/**
 * @Description:  logout
 * @Param: [request]
 * @return: com.plat.auto.test.autotest.entity.User
 * @Author: wangzhaoxian
 * @Date: 2021/11/12
 */
    public User ifLogin(HttpServletRequest request) {
        String cookieToken = CookieUtil.getValue(request, LOGIN_IDENTITY);
        if (cookieToken != null) {
            User cookieUser = parseToken(cookieToken);
            if (cookieUser != null) {
                User dbUser = userMapper.findByUserName(cookieUser.getUserName());
                if (dbUser != null) {
                    if (cookieUser.getPassWord().equals(dbUser.getPassWord())) {
                        return dbUser;
                    }
                }
            }
        }
        return null;
    }
}
