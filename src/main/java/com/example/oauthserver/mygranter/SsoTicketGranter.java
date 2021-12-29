package com.example.oauthserver.mygranter;

import com.example.oauthserver.bo.MyAuthority;
import com.example.oauthserver.dao.UserAuthorityDao;
import com.example.oauthserver.service.SsoService;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Accessors(chain = true)
public class SsoTicketGranter extends AbstractTokenGranter {

    @Setter
    private SsoService ssoService;
    @Setter
    private UserAuthorityDao userDao;

    public SsoTicketGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        log.info("SSO ticket check granter");

        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String ticket = parameters.get("ticket");
        String service = parameters.get("service");

        String userCode = ssoService.checkTicket(ticket, service);

        List<GrantedAuthority> authorityList = new ArrayList<>();

        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("USER_CODE", userCode);
        columnMap.put("USER_STATUS", 1);
        List<String> authorities = userDao.selectByMap(columnMap).stream().map(user -> user.getAuthority()).collect(Collectors.toList());

        for (String authoritie : authorities) {
            authorityList.add(new MyAuthority(authoritie));
        }

        //authenticated is set to true when we support the granted authorities
        Authentication userAuth = new UsernamePasswordAuthenticationToken(userCode, "", authorityList);

        ((AbstractAuthenticationToken) userAuth).setDetails(tokenRequest.getRequestParameters());

        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate user: " + "");
        }

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);

    }
}
