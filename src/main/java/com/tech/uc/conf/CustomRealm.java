package com.tech.uc.conf;

import com.tech.uc.common.exception.UserNotFoundException;
import com.tech.uc.common.utils.RedisClient;
import com.tech.uc.common.utils.UserContextUtil;
import com.tech.uc.entity.Resource;
import com.tech.uc.entity.Role;
import com.tech.uc.entity.User;
import com.tech.uc.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zhuyz
 * @date 2020/4/1 0001 23:24
 * @description 自定义realm
 */
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisClient redisClient;

    /**
     * 授权,进行权限校验的时候回调
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("授权-----doGetAuthorizationInfo");
        User user = (User) principals.fromRealm(getName()).stream().findFirst().get();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(user.getRoleList().stream().map(Role:: getCode).collect(Collectors.toSet()));
        authorizationInfo.setStringPermissions(user.getResourceList().stream()
                .map(Resource:: getPermission)
                .filter(w -> w != null && !"".equals(w))
                .collect(Collectors.toSet()));
        return authorizationInfo;
    }

    /**
     * 登录认证
     * @param token 用户登录输入的信息
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("登录认证----doGetAuthenticationInfo");
        String username = (String)token.getPrincipal();
        String baseURL = UserContextUtil.currentBaseURL();
        User user = userService.findByUsername(username, baseURL);
        if (user == null) {
            throw new UserNotFoundException("用户不存在");
        }
        // 账户冻结
        if (user.getStatus() == 0) {
            throw new LockedAccountException();
        }
        UserContextUtil.setCurrentMenus(user.getMenus());
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }














}
