package com.seizedays.ideasharingcosumer.services;

import com.alibaba.dubbo.config.annotation.Reference;
import com.seizedays.beans.IdeaUser;
import com.seizedays.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;


@Service
@Primary
public class MyUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        IdeaUser loginUser = userService.selectUser(username);

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        //在Security中，角色和权限共用GrantedAuthority接口，唯一的不同角色就是多了个前缀"ROLE_"，而且它没有Shiro的那种从属关系，即一个角色包含哪些权限等等。
        // 在Security看来角色和权限时一样的，它认证的时候，把所有权限（角色、权限）都取出来，而不是分开验证。
        authorities.add(new SimpleGrantedAuthority(loginUser.getAuthority()));
        authorities.add(new SimpleGrantedAuthority(loginUser.getRole()));

        logger.info("登录用户名:" + username);
        System.out.println(username);
        if(loginUser != null){
            String pwd = loginUser.getPassword();
//            User user  = new User("nickname", pwd, authorities);
            User user  = new User(username, pwd, authorities);
            System.out.println(user.getAuthorities());
            //根据用户名查找用户信息
            return user;

        }

        throw new UsernameNotFoundException("*用户名不存在");



        //根据用户名查找到的用户信息判断是否被冻结
        // return new User(username,new BCryptPasswordEncoder().encode("root"), true,true,true,false,authorities);

         }

}
