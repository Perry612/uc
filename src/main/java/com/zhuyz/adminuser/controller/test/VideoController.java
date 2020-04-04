package com.zhuyz.adminuser.controller.test;

import com.zhuyz.adminuser.entity.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhuyz
 * @date 2020/4/3 0003 22:19
 * @description
 */
@RestController
@RequestMapping("/video")
public class VideoController {



    @GetMapping("/update")
    public ResponseEntity updateVideo() {
        return ResponseEntity.buildSuccess("video更新成功");
    }



}