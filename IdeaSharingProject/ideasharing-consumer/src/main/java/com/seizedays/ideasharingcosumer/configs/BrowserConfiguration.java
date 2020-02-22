package com.seizedays.ideasharingcosumer.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class BrowserConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()                                // 定义当需要用户登录时候，会跳转到的登录页面。
                .loginPage("/login")                        // 设置登录页面
                .loginProcessingUrl("/login.do")          // 自定义的登录接口
                .usernameParameter("username")              //用户名参数
                .passwordParameter("password")              //密码参数
                .defaultSuccessUrl("/index").permitAll()     // 登录成功之后，默认跳转的页面
                .failureUrl("/login.fail")
                .and().authorizeRequests()                  // 定义哪些URL需要被保护、哪些不需要被保护
                .antMatchers("/", "/index","/login.do","/register","/register.do","/duplicateCheck").permitAll() // 设置所有人都可以访问的页面
                .antMatchers("/authority/**").hasAuthority("ADMIN")  //只有admin用户可以访问权限管理
                .anyRequest().authenticated()               // 任何请求,登录后可以访问
                .and().csrf().disable();                    // 关闭csrf防护

        //注销 注销之后跳到首页
        http.logout()
            .logoutSuccessUrl("/index");
        //设置X-Frame-Options
        http.headers()
            .frameOptions().disable();

        //"记住我"功能的实现 默认保存在cookie中两周时间
        http.rememberMe()
            .rememberMeParameter("remember");
    }

    //静态资源忽略配置
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/webjars/**/*", "/css/**/*", "/js/**/*","/images/**/*","/webfonts/**/*","/pages/**/*");
    }
}
