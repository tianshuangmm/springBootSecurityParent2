package com.ts.springboot.security;

import com.ts.springboot.security.handler.CustomAuthenticationFailureHandler;
import com.ts.springboot.security.handler.CustomAuthenticationSuccessHandler;
import com.ts.springboot.security.session.CustomExpiredSessionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

/**
 * @author jitwxs
 * @date 2018/3/29 16:57
 */
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity 开启Spring Security 全局方法安全，等价的XML配置如下：
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationDetailsSource<HttpServletRequest,WebAuthenticationDetails> authenticationDetailsSource;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    //@Autowired
   // private VerifyFilter verifyFilter;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
        auth.userDetailsService(userDetailsService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(charSequence.toString());
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .invalidSessionUrl("/login/invalid")
                 //指定最大登录数
                 .maximumSessions(1)
                 //是否保留已经登录的用户：为true，新用户无法登录
                 //为false，旧用户被踢出
                 .maxSessionsPreventsLogin(false)
                //当达到最大值时，旧用户别踢出后的操作
                 .expiredSessionStrategy(new CustomExpiredSessionStrategy());
        http.authorizeRequests()

                /*.and.sessionManagement()
                .invalidSessionUrl("/login/invalid")*/
                // 如果有允许匿名的url，填在下面
                // .antMatchers().permitAll()
                .antMatchers("/getVerifyCode","/login","/login/invalid").permitAll()
                .anyRequest().authenticated()
                .and()

                // 设置登陆页
                .formLogin().loginPage("/login")
                // 设置登陆成功页
              ///  .defaultSuccessUrl("/").permitAll()
                   .successHandler(customAuthenticationSuccessHandler).permitAll()
                // 登录失败Url  设置登录异常跳转页面
             ////   .failureUrl("/login")
                .failureHandler(customAuthenticationFailureHandler).permitAll()
                // 自定义登陆用户名和密码参数，默认为username和password
                //usernameParameter("username")
                //.passwordParameter("password")
                //springSecurity验证表单登录  03过滤器器登录验证
                .authenticationDetailsSource(authenticationDetailsSource)
                .and()
                //.addFilterBefore(new VerifyFilter(),UsernamePasswordAuthenticationFilter.class)
                .logout().permitAll()


                //and().csrf().disable()                     // 关闭csrf防护
                //自动登录
                //当我们自动勾选自动登录时，会自动在cookie中保存一个  02- cookie存储
                // 名为remember-me的cookie，默认有效期为2周，其值是一个加密的字符串
                .and().rememberMe()
                       //上面一行加下面这些数据库存储
                       .tokenRepository(persistentTokenRepository())
                       //设置有效时间： 单位 s
                       .tokenValiditySeconds(6000)
                       .userDetailsService(userDetailsService);




        // 关闭CSRF跨域
        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 设置拦截忽略文件夹，可以对静态资源放行
        web.ignoring().antMatchers("/css/**", "/js/**");
    }

    @Autowired
    private DataSource dataSource;

    @Bean
    //在 WebSecurityConfig 中注入 dataSource ，创建一个 PersistentTokenRepository 的Bean
    //数据库存储加  02-数据库存储
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    private CustomPermissionEvaluator customPermissionEvaluator;

    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler(){
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(customPermissionEvaluator);
        return handler;
    }

}