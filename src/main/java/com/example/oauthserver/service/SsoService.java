package com.example.oauthserver.service;

public interface SsoService {
    /**
     * 单点票据检查
     *
     * @param ticket
     * @param service
     * @return
     */
    String checkTicket(String ticket, String service);
}
