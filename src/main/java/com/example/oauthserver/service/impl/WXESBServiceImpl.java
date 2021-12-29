package com.example.oauthserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.oauthserver.service.WXESBService;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
@Service
public class WXESBServiceImpl implements WXESBService {


    @Autowired
    private RestTemplate template;

    @Value("${getWXuserinfoUrl}")
    private String getWXuserinfoUrl;

    @Override
    public String getUserCode(String authCode,String systemId)  {
        String url = getWXuserinfoUrl+"?code="+authCode+"&systemId="+systemId;
        ResponseEntity<String> forEntity = template.getForEntity(url, String.class);
        log.info("forEntity.getBody:{}",forEntity.getBody());
        JSONObject jsonObject = JSONObject.fromObject(forEntity.getBody());
        if(jsonObject!=null && jsonObject.getString("state").equals("200")){
            net.sf.json.JSONObject ob = (net.sf.json.JSONObject)jsonObject.get("ob");
            if(ob.has("UserId")){
                return String.valueOf(ob.get("UserId"));
            }else{
                return String.valueOf(ob.get("OpenId"));
            }
        }else{
            return null;
        }
    }

}
