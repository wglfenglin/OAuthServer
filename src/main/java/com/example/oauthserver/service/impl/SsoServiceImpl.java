package com.example.oauthserver.service.impl;

import com.example.oauthserver.properties.OAuthServerProperties;
import com.example.oauthserver.service.SsoService;
import com.example.oauthserver.utils.ESBHttpUtil;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Iterator;

@Slf4j
@Service
public class SsoServiceImpl implements SsoService {

    @Autowired
    private OAuthServerProperties serverProperties;

    @Override
    public String checkTicket(String ticket, String service) {
        try {
            String url = serverProperties.getAuthProperties().getSsoCheckTicket()
                    .replace("TICKET", ticket).replace("SERVICE", service);
            log.info("url: {}",url);
            String requestSsoResult = ESBHttpUtil.doGet(url, Collections.EMPTY_MAP);
            if (!StringUtils.isEmpty(requestSsoResult)) {
                Document doc = DocumentHelper.parseText(requestSsoResult);
                Element rootElt = doc.getRootElement();
                // SSO 成功返回字样:" <cas:authenticationSuccess>...</cas:authenticationSuccess>"
                Element authenticationSuccess = rootElt.element("authenticationSuccess");
                // SSO 失败返回字样:" <cas:authenticationFailure code="INVALID_TICKET">未能够识别出目标 'TICKET'票根</cas:authenticationFailure>"
                String authenticationFailure = rootElt.elementTextTrim("authenticationFailure");
                if (!StringUtils.isEmpty(authenticationFailure)) {
                    log.info("SSO单点凭证验证失败:{}", authenticationFailure);
                    return "";
                }
                Iterator attributes = authenticationSuccess.elementIterator("attributes");
                // SSO根据ticket只会返回一个用户,取到后直接返回userCode即可     asUserCode
                while (attributes.hasNext()) {
                    Element user = (Element) attributes.next();
                    String userCode = user.elementTextTrim("userCode");
                    String asUserCode = user.elementTextTrim("asUserCode");
                    log.info("userCode:{},asUserCode{}",userCode,asUserCode);
                    if(StringUtils.isNotBlank(asUserCode)) {
                    	userCode=	asUserCode;
                    }
                    return userCode;
                }
                return "";
            } else {
                log.error("调用SSO接口获取人员信息异常,请求接口返回:{}", requestSsoResult);
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
