package com.lm.api.service.impl;

import com.lm.base.ResponseBase;
import com.lm.entity.UserEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/member")
public interface MemberService {

    //根据用户id查询用户
    @RequestMapping("/findUserById")
    ResponseBase findUserById(Long userId);
    //用户注册
    @RequestMapping("/regUser")
    ResponseBase regUser(@RequestBody UserEntity userEntity);
}
