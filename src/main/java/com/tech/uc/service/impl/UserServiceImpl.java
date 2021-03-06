package com.tech.uc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tech.uc.common.exception.ServiceException;
import com.tech.uc.entity.Resource;
import com.tech.uc.entity.Role;
import com.tech.uc.entity.User;
import com.tech.uc.mapper.RoleMapper;
import com.tech.uc.mapper.UserMapper;
import com.tech.uc.service.*;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhuyz
 * @since 2020-04-05
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private OrgService orgService;

    @Autowired
    private ServerService serverService;

    @Autowired
    private DictGroupService dictGroupService;

    @Autowired
    private DictItemService dictItemService;

    @CacheEvict(cacheNames = "sys", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveUser(User user) {
        if (user == null) {
            throw new ServiceException("user cannot be null");
        }
        String userId = user.getId();
        if (userId == null) {
            // 执行insert
            super.insert(user);
        } else {
            // 执行update
            super.update(user, new EntityWrapper<User>().where("id={0}", userId));
            // 删除用户组织关系
            userMapper.deleteUserOrgList(userId);
            // 删除用户权限关系
            userMapper.deleteUserRoleList(userId);
        }
        // 保存用户组织关系
        if (user.getOrgList().size() > 0) {
            userMapper.insertUserOrgList(userId, user.getOrgList());
        }
        // 保存用户角色关系
        if (user.getRoleList().size() > 0) {
            userMapper.insertUserRoleList(userId, user.getRoleList());
        }
    }

    @Override
    public void deleteUser(User user) {

    }


    @Override
    public User findByUsername(String username, String uri) {
        User user = userMapper.findByUsername(username);
        HashSet<Resource> resources = new HashSet<>();

        for (Role role : user.getRoleList()) {
            resources.addAll(role.getResourceList());
        }
        user.setResourceList(new ArrayList<>(resources));
        Map<String, String> serverMap = serverService.getServerMapByUri(uri);

        List<Resource> menus = resources.parallelStream()
                .filter(o -> o.getEnabled() == 1 && "1".equals(o.getType()))
                .sorted((o1, o2) -> o1.getSort() <= o2.getSort() ? (o1.getSort() == o2.getSort() ? 0 : -1) : 1)
                .collect(Collectors.toList());

        for (Resource menu : menus) {
            String prefix = StringUtils.isEmpty(serverMap.get(menu.getProjectNo())) ? "" : serverMap.get(menu.getProjectNo());
            menu.setUri(prefix + (menu.getUri() == null ? StringUtils.EMPTY : menu.getUri()));
        }
        user.setMenus(list2tree(menus));
        return user;
    }

    @Override
    public List<User> findByOrgId(String orgId) {
        return null;
    }

    @Override
    public List<User> findByCondition(Map condition) {
        return null;
    }

    @Override
    public List<User> findByOrgIdAndRoleCode(String orgId, String roleCode) {
        return null;
    }

    @Override
    public List<User> findByRoleId(String roleId) {
        return null;
    }

    @Override
    public Map<String, Object> findByUsernameAndPassword(String username, String password) {
        return null;
    }

    @Override
    public PageInfo<User> findAllUserByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> users;
        users = userMapper.findAllUser();
        PageInfo<User> userPageInfo = new PageInfo<>(users);
        return userPageInfo;
    }

    public static List<Resource> list2tree(List<Resource> list){
        Map<String, Resource> map = new HashMap<>();
        //ID 为 key 存储到map 中
        for (Resource demo : list) {
            map.put(demo.getId(), demo);
        }
        List<Resource> resources = new ArrayList<>();
        for (Resource resource : list) {
            //子集ID返回对象，有则添加。
            Resource pResource = map.get(resource.getPid());
            if(pResource != null){
                pResource.getChildrens().add(resource);
            }else {
                resources.add(resource);
            }
        }
        return resources;
    }


}
