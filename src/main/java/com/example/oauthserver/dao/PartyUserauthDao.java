package com.example.oauthserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.oauthserver.bo.FullPartyUserauth;
import com.example.oauthserver.entity.PartyUserauth;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 人员权限信息表 Mapper 接口
 * </p>
 *
 * @author liushaoyu
 * @since 2019-07-10
 */
public interface PartyUserauthDao extends BaseMapper<PartyUserauth> {
    @Select("select u.user_code  AS userCode,\n" +
            "                   u.password   AS password,\n" +
            "                   u.auth_state as authState\n" +
            "              from TBL_PARTY_USERAUTH u\n" +
            "             where u.user_code = #{userCode} \n" +
            "             and u.auth_state='1' ")
    PartyUserauth getUserInfo(String userCode);
}
