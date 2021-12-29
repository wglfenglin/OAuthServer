package com.example.oauthserver.bo;
import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
@AllArgsConstructor
public class MyAuthority implements GrantedAuthority {
    
    /**
     * 
     */
    private static final long serialVersionUID = 6593983503068452436L;
    
    private String authorityName;
    
    
    @Override
    public String getAuthority() {
        // TODO Auto-generated method stub
        return authorityName;
    }

}
