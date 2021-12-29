package com.example.oauthserver.service;

public interface WXESBService {
    /**
     * 调用esb获取用户信息
     * @param authCode
     * @param systemId
     * @return
     */
     String getUserCode(String authCode,String systemId) ;
}
