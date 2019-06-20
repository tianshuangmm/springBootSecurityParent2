package com.ts.springboot.filter;

import org.springframework.security.authentication.DisabledException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


///@WebFilter(urlPatterns = "/",filterName = "verifyFilter")
public class VerifyFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if(isProtectedUrl(httpServletRequest)){
            String verifyCode = (String) httpServletRequest.getParameter("verifyCode");
            if(verifyCode!=null&&"".equals(verifyCode)){
                if(!validateVerify(verifyCode,httpServletRequest)){
                    //手动设置异常
                    httpServletRequest.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION",new DisabledException("验证码输入错误"));
                    //转发到错误url
                    httpServletRequest.getRequestDispatcher("/login").forward(httpServletRequest,httpServletResponse);
                }else{
                    filterChain.doFilter(httpServletRequest,httpServletResponse);
                }
            }else{
                filterChain.doFilter(httpServletRequest,httpServletResponse);
            }
        }else{
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }
    }

    //比对验证码
    private boolean validateVerify(String inputVerify,HttpServletRequest httpServletRequest){
        String validateCode = (String) httpServletRequest.getSession().getAttribute("validateCode");
        if(validateCode!=null&&!"".equals(validateCode)){
            return validateCode.equalsIgnoreCase(inputVerify);
        }else{
            return false;
        }
    }

    //拦截 /login的post请求//https://blog.csdn.net/qq_37993823/article/details/85128287
    private boolean isProtectedUrl(HttpServletRequest request){
        boolean flag = match("/login",request.getServletPath())&&"post".equalsIgnoreCase(request.getMethod());
        return flag;
    }

    private boolean match(String str1,String str2){
        return str2.equals("/login");
    }
}
