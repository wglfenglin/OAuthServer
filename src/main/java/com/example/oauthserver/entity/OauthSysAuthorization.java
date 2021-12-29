//package com.example.oauthserver.entity;
//
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableName;
//import com.baomidou.mybatisplus.extension.activerecord.Model;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.experimental.Accessors;
//
//import java.io.Serializable;
//import java.time.LocalDateTime;
//
///**
// * <p>
// * 应用访问权限表
// * </p>
// *
// * @author liushaoyu
// * @since 2020-01-09
// */
//@Data
//@EqualsAndHashCode(callSuper = false)
//@Accessors(chain = true)
//@TableName("TBL_OAUTH_SYS_AUTHORIZATION")
//public class OauthSysAuthorization extends Model<OauthSysAuthorization> {
//
//    private static final long serialVersionUID = 1L;
//
//    /**
//     *主键
//     */
//    @TableField("ID")
//    private String id;
//
//    /**
//     *用户id
//     */
//    @TableField("USER_CODE")
//    private String userCode;
//
//    /**
//     * 权限
//     */
//    @TableField("AUTHORIZATION")
//    private String authorization;
//
//    /**
//     * 更改时间
//     */
//    @TableField("UPDATE_TIME")
//    private LocalDateTime updateTime;
//
//
//    @Override
//    protected Serializable pkVal() {
//        return null;
//    }
//
//}
