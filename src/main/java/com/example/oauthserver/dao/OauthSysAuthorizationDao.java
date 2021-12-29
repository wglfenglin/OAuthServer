//package com.example.oauthserver.dao;
//
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.example.oauthserver.entity.OauthSysAuthorization;
//import org.apache.ibatis.annotations.Param;
//import org.apache.ibatis.annotations.Select;
//
//import java.util.List;
//
///**
// * <p>
// * 应用访问权限表 Mapper 接口
// * </p>
// *
// * @author liushaoyu
// * @since 2020-01-09
// */
//public interface OauthSysAuthorizationDao extends BaseMapper<OauthSysAuthorization> {
//    /**
//     * 根据用户工号获取数据
//     * @param userCode
//     * @return
//     */
//    @Select("select id,user_code,authorization,update_time from tbl_oauth_sys_authorization where user_code = #{userCode}")
//    List<OauthSysAuthorization> listSysAuthorizationByUserCode(@Param("userCode")String userCode);
//}
