package com.ts.springboot.security;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/*
* 获取用户登录时携带的额外信息
* webAuthenticationDetails 该类提供了获取用户登录时携带的额外信息的功能
*                          默认提供了remoteAddress 与 sessionID信息
* */
//@Component
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    private static final long serialVersionUID = 6975601077710753878L;

    public String getVerifyCode() {
        return verifyCode;
    }

    private final String verifyCode;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        //verifyCode 为页面中验证码的name
        this.verifyCode = request.getParameter("verifyCode");
    }
}
