package com.example.oauthserver.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 人员权限信息表
 * </p>
 *
 * @author liushaoyu
 * @since 2019-07-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_PARTY_USERAUTH")
public class PartyUserauth extends Model<PartyUserauth> {

    private static final long serialVersionUID = 1L;


    @TableField("ID")
    private byte[] id;

    @TableField("USER_CODE")
    private String userCode;

    /**
     *  权限状态：1.启用，0.禁用
     */
    @TableField("AUTH_STATE")
    private String authState;

    /**
     *修改时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     *登录密码
     */
    @TableField("PASSWORD")
    private String password;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
