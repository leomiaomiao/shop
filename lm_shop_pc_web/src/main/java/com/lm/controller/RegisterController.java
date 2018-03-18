package com.lm.controller;

import com.lm.base.ResponseBase;
import com.lm.constant.Constants;
import com.lm.entity.UserEntity;
import com.lm.feign.MemberServiceFegin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RegisterController {

    @Autowired
    private MemberServiceFegin memberServiceFegin;
    private static final String REGISTSER = "register";
    private static final String LOGIN = "login";
    @RequestMapping("/register")
    public String registserGet(){
        return REGISTSER;
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String registerPost(UserEntity userEntity, HttpServletRequest request){
        //1.验证参数
        //2.调用会员注册接口
        ResponseBase responseBase = memberServiceFegin.regUser(userEntity);
        //3.如果失败跳转到失败页面
        if (!responseBase.getCode().equals(Constants.HTTP_RES_CODE_200)){
            request.setAttribute("error","注册失败");
            return REGISTSER;
        }
        //4.如果成功跳转到登录页面

        return LOGIN;
    }
}
