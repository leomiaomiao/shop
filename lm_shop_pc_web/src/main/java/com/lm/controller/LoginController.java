package com.lm.controller;

import com.lm.base.ResponseBase;
import com.lm.constant.Constants;
import com.lm.entity.UserEntity;
import com.lm.feign.MemberServiceFegin;
import com.lm.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

@Controller
public class LoginController {

    private static final String LOGIN = "login";
    @Autowired
    private MemberServiceFegin memberServiceFegin;
    //跳转到登录页面
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String loginGet(){
        return LOGIN;
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String loginPost(UserEntity userEntity, HttpServletRequest request, HttpServletResponse response){
        //1.验证参数‘
        //2.调用登录接口，获取token
        ResponseBase login = memberServiceFegin.login(userEntity);
        if (!login.equals(Constants.HTTP_RES_CODE_200)){
            request.setAttribute("error","账号或密码错误");
            return LOGIN;
        }
        //3.将token信息存储在cookie中
        LinkedHashMap data = (LinkedHashMap) login.getData();
        String memberToken = (String) data.get("memberToken");
        if (StringUtils.isEmpty(memberToken)){
            request.setAttribute("error","会话已经失效");
            return LOGIN;
        }
        CookieUtil.addCookie(response,Constants.COOKIE_MEMEBER_TOKEN,memberToken,Constants.COOKIE_TOKEN_MEMBER_TIME);
        return "";
    }
}
