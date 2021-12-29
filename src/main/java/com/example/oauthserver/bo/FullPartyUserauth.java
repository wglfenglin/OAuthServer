package com.example.oauthserver.bo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.oauthserver.entity.PartyUserauth;
import lombok.Data;

@Data
public class FullPartyUserauth extends PartyUserauth {

    @TableField(exist = false)
    private String authority;

}
