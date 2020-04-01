package com.zhuyz.adminuser.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.zhuyz.adminuser.entity.User;

import java.util.List;

public interface IUserService extends IService<User> {

    List<User> findAllUser();

    PageInfo<User> findAllUserByPage(int pageNum, int pageSize);

    User findUserById(Integer id);

    Integer countAllUser();

    Integer deleteUserById(Integer id);

    Integer updateUserById(User user);

    Integer updateUserSwitchById(Integer id ,boolean isSwitch);

    User findAllUserInfoByUserId(Integer userId);

}