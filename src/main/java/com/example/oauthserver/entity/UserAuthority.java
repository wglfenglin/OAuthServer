package com.example.oauthserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 动态认证用户权限表
 * </p>
 *
 * @author liushaoyu
 * @since 2020-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_OAUTH_USER_AUTHORITY")
public class UserAuthority extends Model<UserAuthority> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "USER_CODE", type = IdType.ID_WORKER_STR)
    private String userCode;

    @TableField("AUTHORITY")
    private String authority;

    @TableField("ACCESS_TOKEN")
    private String accessToken;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.userCode;
    }

}
