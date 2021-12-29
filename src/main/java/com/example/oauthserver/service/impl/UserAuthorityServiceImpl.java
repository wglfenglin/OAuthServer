package com.example.oauthserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.oauthserver.dao.UserAuthorityDao;
import com.example.oauthserver.entity.UserAuthority;
import com.example.oauthserver.service.UserAuthorityService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 动态认证用户权限表 服务实现类
 * </p>
 *
 * @author liushaoyu
 * @since 2020-04-21
 */
@Service
public class UserAuthorityServiceImpl extends ServiceImpl<UserAuthorityDao, UserAuthority> implements UserAuthorityService {

}
