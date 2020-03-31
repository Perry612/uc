package com.zhuyz.adminuser;

import com.zhuyz.adminuser.entity.User;
import com.zhuyz.adminuser.mapper.UserMapper;
import com.zhuyz.adminuser.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminUserApplicationTests {

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private UserService userService;

    @Test
    public void contextLoads() {
    }


    @Test
    public void testCount() {

        System.out.println(userMapper.countAllUser());

    }
    @Test
    public void testFind() {
        System.out.println(userService.findUserById(22));

    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setName("wahaha");
        user.setId(22);
        user.setPassword("1231232132132");
        user.setAddress("US");
        user.setEmail("sdfdsfds@qq.com");
        System.out.println(userService.updateUserById(user));

    }

    @Test
    public void testFindService() {
        System.out.println(userService.findUserById(22));

    }

    @Test
    public void testSwitch() {
        System.out.println(userService.updateUserSwitchById(22, false));

    }

}