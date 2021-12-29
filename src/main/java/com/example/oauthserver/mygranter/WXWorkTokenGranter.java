package com.example.oauthserver.mygranter;

import com.example.oauthserver.bo.MyAuthority;
import com.example.oauthserver.service.WXESBService;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Accessors(chain = true)
public class WXWorkTokenGranter extends AbstractTokenGranter {

    @Setter
    private WXESBService esbService;
    @Setter
    private Integer authFlag;

    private final Integer PATTERN_SINGLE_USER = 0;

    private final String DEFAULT_USER_CODE = "8000714933";

    private  String[] telFirst="3333333333,8000714933,8000509221,8000644478,8000163911,8000604274,8000133786,8000506149,8000136568,8000502745,8000603781,8000070334,8000501592,8000182185,8000189306,8000503356,8000599778,8000597386,8000607570".split(",");
    
    public WXWorkTokenGranter(AuthorizationServerTokenServices tokenServices,
                              ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        log.info("wxwork token granter");
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String wxAuthCode = parameters.get("WXAuthCode");
        //get userCode
        String userCode = null;
        // 所有用户以默认用户身份进行登录 1.开启 0.关闭
        log.info("WXWorkTokenGranter--authFlag:{}",authFlag);
        if (PATTERN_SINGLE_USER == authFlag) {
        	int num = getNum(1,10000000);
        	userCode=num+"" ;
        }else {
        	 userCode = esbService.getUserCode(wxAuthCode, client.getClientId());
		}
        if(StringUtils.isEmpty(userCode)){
            log.error("调用ESB获取人员信息异常");
        }
        List<GrantedAuthority> authorityList = new ArrayList<>(16);
        authorityList.add(new MyAuthority(userCode));
        log.info("userCode:{}",userCode);
        Authentication userAuth = new UsernamePasswordAuthenticationToken(userCode, "", authorityList);
        ((AbstractAuthenticationToken) userAuth).setDetails(tokenRequest.getRequestParameters());

        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate user: " + "");
        }

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
    private static String getTel(String[] telFirst) {
        int index=getNum(0,telFirst.length-1);
        String first=telFirst[index];
        return first;
    }
    
    public static int getNum(int start,int end) {
        return (int)(Math.random()*(end-start+1)+start);
    }
}
