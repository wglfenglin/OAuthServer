package com.example.oauthserver.oauthserverconfig;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.example.oauthserver.properties.OAuthServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author joe 2020-01-02 14:32
 */
@Slf4j
@Component
public class SpringBootApolloRefreshConfig {

    private OAuthServerProperties OAuthServerProperties;
    private RefreshScope refreshScope;

    public SpringBootApolloRefreshConfig(OAuthServerProperties OAuthServerProperties, RefreshScope refreshScope) {
        this.OAuthServerProperties = OAuthServerProperties;
        this.refreshScope = refreshScope;
    }

    @ApolloConfigChangeListener(value = {"application.properties"},interestedKeyPrefixes={"oauthserver.authProperties.","oauthserver.jndiProperties.","oauthserver.esbProperties."})
    public void onChange(ConfigChangeEvent changeEvent) {
        log.info("before refresh {}", OAuthServerProperties.toString());
        refreshScope.refresh("OAuthServerProperties");
        log.info("after refresh {}", OAuthServerProperties.toString());
    }
}
