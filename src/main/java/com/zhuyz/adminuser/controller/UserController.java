package com.zhuyz.adminuser.controller;

import com.github.pagehelper.PageInfo;
import com.zhuyz.adminuser.entity.ResponseEntity;
import com.zhuyz.adminuser.entity.User;
import com.zhuyz.adminuser.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService userService;

    /**
     * 分页查询用户列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/findAll/{pageNum}/{pageSize}")
    public ResponseEntity<PageInfo<User>> findAllUser(@PathVariable("pageNum") Integer pageNum,
                                                @PathVariable("pageSize") Integer pageSize) {
        ResponseEntity<PageInfo<User>> responseEntity = new ResponseEntity<>();
        PageInfo<User> userPageInfos = userService.findAllUserByPage(pageNum, pageSize);
        return ResponseEntity.buildSuccess(userPageInfos, "query ok");
    }

    @GetMapping("/findUser/{id}")
    public ResponseEntity findUserById(@PathVariable("id") Integer id) {
        ResponseEntity responseEntity = new ResponseEntity<>();
        User userById = userService.findUserById(id);
        if (userById != null) {
            return ResponseEntity.buildSuccess(userById);
        } else {
            return ResponseEntity.buildCustom("user not found", 604);
        }
    }

    /**
     * 根据用户id删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Integer> deleteUserById(@PathVariable("id") Integer id) {
        ResponseEntity<Integer> responseEntity = new ResponseEntity<>();
        Integer state = userService.deleteUserById(id);
        if (state == 1) {
            Integer pageTotal = userService.countAllUser();
            return ResponseEntity.buildSuccess(pageTotal, "delete ok");
        } else {
           return ResponseEntity.buildCustom(null, 601, "delete error");
        }
    }

    /**
     * 根据用户id更新是否启用开关
     * @param user
     * @return
     */
    @PutMapping("/update")
    public ResponseEntity<User> updateUserById(@RequestBody User user) {
        ResponseEntity<User> responseEntity = new ResponseEntity<>();
        Integer state = userService.updateUserById(user);
        User userById = userService.findUserById(user.getId());
        if (state == 1) {
            return ResponseEntity.buildSuccess(userById, "update ok");
        } else {
            return  ResponseEntity.buildError(userById, 602, "update error");
        }
    }

    /**
     * 根据用户id更新用户
     * @param id
     * @param open
     * @return
     */
    @PutMapping("/updateSwitch/{id}/{open}")
    public ResponseEntity<Integer> updateUserSwitchById(@PathVariable("id") Integer id, @PathVariable("open") boolean open) {
        ResponseEntity<Integer> responseEntity = new ResponseEntity<>();
        Integer state = userService.updateUserOpenById(id, open);
        if (state == 1) {
           return ResponseEntity.buildSuccess(state, "update open ok");
        } else {
            return ResponseEntity.buildError(state, 603, "update open error");
        }
    }

}
