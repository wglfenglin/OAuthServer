package com.example.oauthserver.oauthserverconfig;

import com.example.oauthserver.dao.UserAuthorityDao;
import com.example.oauthserver.mygranter.SsoTicketGranter;
import com.example.oauthserver.mygranter.WXWorkTokenGranter;
import com.example.oauthserver.properties.OAuthServerProperties;
import com.example.oauthserver.service.SsoService;
import com.example.oauthserver.service.WXESBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Order(6)
@EnableAuthorizationServer
@Slf4j
public class OAuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private OAuthServerProperties appProperties;
    @Autowired
    private WXESBService wxesbService;
    @Autowired
    private SsoService ssoService;
    @Autowired
    private UserAuthorityDao userAuthorityDao;
    @Value("${oauthserver.authProperties.authFlag}")
    private String authFlag;

    private final String AUTHENTICATION_WX = "WXAuthCode";

    private final String AUTHENTICATION_SSO_TICKET = "SsoTicketCheck";

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

////    @Bean
////    public ClientCredentialsTokenEndpointFilter checkTokenEndpointFilter() {
//    ClientCredentialsTokenEndpointFilter filter = new ClientCredentialsTokenEndpointFilter("/oauth/check_token");
////        filter.setAuthenticationManager(authenticationManager);
////        filter.setAllowOnlyPost(true);
////        return filter;
////    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()").allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(new MyJdbcClienDetailsService(dataSource));

    }

    // 用来对token进行相关设置，比如设置token有效时长
//    @Primary
//    @Bean
//    public AuthorizationServerTokenServices tokenServices() {
//        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//        defaultTokenServices
//                .setAccessTokenValiditySeconds(meetRoomBkProperties.getAuthProperties().getTokenExpiresIn());
////        defaultTokenServices.setRefreshTokenValiditySeconds(200000);
//        defaultTokenServices.setSupportRefreshToken(true);
//        defaultTokenServices.setReuseRefreshToken(false);
//        defaultTokenServices.setTokenStore(tokenStore());
////        defaultTokenServices.setTokenEnhancer(new WXAuthCodeTokenEnhancer());
//        return defaultTokenServices;
//    }

    @Bean
    @Primary
    public TokenStore tokenStore() {
//            TokenStore tokenStore = new InMemoryTokenStore();
        return new MyJdbcTokenStore(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager)
                // 没有它，在使用refresh_token的时候会报错 IllegalStateException, UserDetailsService is required.
                .userDetailsService(userDetailsService)
                .tokenEnhancer(new WXAuthCodeTokenEnhancer())
//                .exceptionTranslator(webResponseExceptionTranslator);
                .exceptionTranslator(new CustomWebResponseExceptionTranslator())
                // 不加报错"method_not_allowed",
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

        List<TokenGranter> tokenGrantersList = new ArrayList<TokenGranter>(16);
        log.info("OAuthServerConfig--authFlag:{}",authFlag);
        WXWorkTokenGranter wxWorkTokenGranter = new WXWorkTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(), AUTHENTICATION_WX);
        wxWorkTokenGranter.setEsbService(wxesbService);
        wxWorkTokenGranter.setAuthFlag(appProperties.getAuthProperties().getAuthFlag());
        SsoTicketGranter ssoTicketGranter = new SsoTicketGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(), AUTHENTICATION_SSO_TICKET);
        ssoTicketGranter.setSsoService(ssoService);
        ssoTicketGranter.setUserDao(userAuthorityDao);
        tokenGrantersList.add(wxWorkTokenGranter);
        tokenGrantersList.add(ssoTicketGranter);
        CompositeTokenGranter myCompositeTokenGranter = new CompositeTokenGranter(tokenGrantersList);
        //Add the default granters
        myCompositeTokenGranter.addTokenGranter(endpoints.getTokenGranter());

        endpoints.tokenGranter(myCompositeTokenGranter);
    }

}
