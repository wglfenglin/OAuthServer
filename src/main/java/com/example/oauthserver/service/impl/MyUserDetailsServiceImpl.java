package com.example.oauthserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.oauthserver.bo.MyAuthority;
import com.example.oauthserver.dao.PartyUserauthDao;
import com.example.oauthserver.dao.UserAuthorityDao;
import com.example.oauthserver.entity.MyUser;
import com.example.oauthserver.entity.PartyUserauth;
import com.example.oauthserver.entity.UserAuthority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PartyUserauthDao partyUserauthDao;
    @Autowired
    private UserAuthorityDao userAuthorityDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        PartyUserauth user = partyUserauthDao.getUserInfo(username);
        String pass = "";

        // 用户应用访问权限
        QueryWrapper<UserAuthority> sysAuthorizationQueryWrapper = new QueryWrapper<UserAuthority>()
                .eq("USER_CODE", username);
        List<String> sysAuthorities = userAuthorityDao.selectList(sysAuthorizationQueryWrapper).stream().map(u -> u.getAuthority()).collect(Collectors.toList());


        List<GrantedAuthority> authorities = new ArrayList<>();
        if (null != user) {
            pass = user.getPassword();
            for (String authoritie : sysAuthorities) {
                authorities.add(new MyAuthority(authoritie));
            }

        }

        log.info("UserName:" + username + ",(DB)Password:" + pass);
        log.info("Authorities:" + Arrays.asList(authorities));
        UserDetails resultDetails = new MyUser(username, passwordEncoder.encode(pass), authorities, sysAuthorities);
        log.info("resultDetails is :{} ", resultDetails.toString());
        return resultDetails;


    }


}
