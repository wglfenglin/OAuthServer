package com.example.oauthserver.properties;


import lombok.Data;


/**
 * @author liushaoyu
 * @date 2019/8/1 10:46
 */
@Data
public class AuthProperties {

    private String clientId;

    private String clientSecret;
    
    private int tokenExpiresIn;

    private String ssoCheckTicket;

    private int authFlag;

}
