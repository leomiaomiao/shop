package com.lm.api.service;

import com.lm.base.ResponseBase;
import com.lm.entity.UserEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/member")
public interface MemberService {

    //根据用户id查询用户
    @RequestMapping("/findUserById")
    ResponseBase findUserById(Long userId);
    //用户注册
    @RequestMapping("/regUser")
    ResponseBase regUser(@RequestBody UserEntity userEntity);
    //用户登录
    @RequestMapping("/login")
    ResponseBase login(@RequestBody UserEntity userEntity);
    //使用token进行登录
    @RequestMapping("/findUserByToken")
    ResponseBase findUserByToken(@RequestParam("token") String token);
}
