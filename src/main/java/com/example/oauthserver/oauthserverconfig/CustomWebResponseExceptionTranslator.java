package com.example.oauthserver.oauthserverconfig;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * @author liushaoyu
 * @date 2019/8/1 18:59
 */
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        
        OAuth2Exception oAuth2Exception = null;
        
        if (e instanceof InternalAuthenticationServiceException) {
            oAuth2Exception = OAuth2Exception.create(HttpStatus.UNAUTHORIZED.name(), "用户不存在");
        }
        
        if (e instanceof InvalidGrantException) {
            oAuth2Exception = OAuth2Exception.create("401", "账号或者密码错误");
        }

        if (e instanceof InvalidTokenException) {
            oAuth2Exception = OAuth2Exception.create("401", "Token 未识别");
        }
        
        
        if (oAuth2Exception == null) {
            throw e;
        }//No satisfied result

        return ResponseEntity.ok(oAuth2Exception);
    }
        
}
