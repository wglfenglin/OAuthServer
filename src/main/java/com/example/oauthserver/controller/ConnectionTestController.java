package com.example.oauthserver.controller;

import com.example.oauthserver.properties.OAuthServerProperties;
import com.netflix.discovery.EurekaClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Allen Wang
 * This Contoller is designed only for maintaience guys to monitor the availability of this AAPP
 */
@Slf4j
@RestController
@RequestMapping("/connection")
public class ConnectionTestController {

    @Autowired
    private EurekaClient client;

    @Autowired
    private OAuthServerProperties oAuthServerProperties;

    @GetMapping("/test")
    public String connectionTest() {
        return "ok";
    }

    @DeleteMapping("/offline")
    public void offLine(){
        client.shutdown();
        log.info("client has been shutdown");
    }

    @GetMapping("/auth")
    public String auth() {
        return oAuthServerProperties.getAuthProperties().toString();
    }

    @GetMapping("/esb")
    public String esb() {
        return oAuthServerProperties.getEsbProperties().toString();
    }
}
