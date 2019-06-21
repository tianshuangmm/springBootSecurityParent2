package com.ts.springboot.security;

import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Source;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private CustomUserDetailsService customUserDetailsService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails = null;
        //获取用户输入的用户名和密码
        String inputName = authentication.getName().trim();
        System.out.println("inputName"+inputName);
        String inputPassword = authentication.getCredentials().toString().trim();
        System.out.println("inputPassword"+inputPassword);
        CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication.getDetails();

        String verifyCode = details.getVerifyCode().trim();

        if(verifyCode!=null&&"".equals(verifyCode)&&!validateVerify(verifyCode)){
            throw new BadCredentialsException("验证码输入错误!");
        }else if(inputName!=null){
            //userDetails为数据库中查询到的用户信息
            if(inputName!=null&&!"".equals(inputName)){
                try{
                    userDetails = customUserDetailsService.loadUserByUsername(inputName);
                }catch(Exception e){
                    throw new BadCredentialsException("用户名输入错误！");
                }


                if(userDetails!=null){
                    //如果是自定义authenticationProvider 需要手动密码校验
                    if(!userDetails.getPassword().equalsIgnoreCase(inputPassword)){
                        throw new BadCredentialsException("密码输入错误！");
                    }
                }else{
                    throw new BadCredentialsException("用户名输入错误输入错误！");
                }
            }
        }

        return new UsernamePasswordAuthenticationToken(inputName,inputPassword,userDetails.getAuthorities());
    }
    private boolean validateVerify(String inputVerify){
        //获取当前线程绑定的request对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //不区分大小写
        //这个validateCode是在servlet中存入session的名字
        String validateCode = (String)request.getSession().getAttribute("validateCode");
        System.out.println("验证码validateCode"+validateCode);
        System.out.println("验证码inputVerify"+inputVerify);
        if(validateCode!=null&&!"".equals(validateCode)){
            return validateCode.equalsIgnoreCase(inputVerify);
        }else{
            return false;
        }
    }
    @Override
    public boolean supports(Class<?> aClass) {
        //这里不要忘记，和UsernamePasswordAuthenticationToken
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
