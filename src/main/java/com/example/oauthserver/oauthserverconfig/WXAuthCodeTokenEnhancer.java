package com.example.oauthserver.oauthserverconfig;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.oauthserver.dao.UserAuthorityDao;
import com.example.oauthserver.entity.MyUser;
import com.example.oauthserver.entity.UserAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is designed to add user information to the Token info
 * the main purpose is returning the userCode for WXAuthCode mode, so that the
 * caller can get the userCode info and return it to the Frontend-point which didn't know he is
 *
 * @author Allen Wang
 */
public class WXAuthCodeTokenEnhancer implements TokenEnhancer {

    @Autowired
    UserAuthorityDao userAuthorityDao;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        Map<String, Object> additionalInformationMap = new HashMap<String, Object>();
        Object principal = authentication.getPrincipal();
        if (principal instanceof MyUser) {
            MyUser principal1 = (MyUser) principal;
            additionalInformationMap.put("Principal", principal1.getUsername());
            if (principal instanceof String) {
                // 用户应用访问权限
                QueryWrapper<UserAuthority> sysAuthorizationQueryWrapper= new QueryWrapper<UserAuthority>()
                        .eq("USER_CODE", principal);
                Set<String> sysAuthorities = userAuthorityDao.selectList(sysAuthorizationQueryWrapper).stream().map(u->u.getAuthority()).collect(Collectors.toSet());
                principal1.setSysAuthorities(sysAuthorities);
            } else {
                principal1.setSysAuthorities(null);
            }
        } else {
            additionalInformationMap.put("Principal", principal.toString());
        }
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformationMap);
        return accessToken;
    }

}
