package com.example.oauthserver.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author yls
 * @date 2019/4/26 11:03
 */
@Component
@Data
@RefreshScope
@ConfigurationProperties(prefix = "oauthserver")
public class OAuthServerProperties {

	private JNDIProperties jndiProperties;

	private AuthProperties authProperties;

	private ESBProperties esbProperties;
}
