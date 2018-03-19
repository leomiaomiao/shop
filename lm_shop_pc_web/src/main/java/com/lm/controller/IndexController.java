package com.lm.controller;


import com.lm.base.ResponseBase;
import com.lm.constant.Constants;
import com.lm.feign.MemberServiceFegin;
import com.lm.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

@Controller
public class IndexController {

    private static final String INDEX = "index";
    @Autowired
    private MemberServiceFegin memberServiceFegin;
    //首页
    @RequestMapping("/")
    public String index(HttpServletRequest request){
        //1.从cookie中获取token信息
        String token = CookieUtil.getUid(request, Constants.COOKIE_MEMEBER_TOKEN);
        //2.如果cookie中存在token信息，调用会员服务接口，使用token查询用户信息
        if (!StringUtils.isEmpty(token)){
            ResponseBase userByToken = memberServiceFegin.findUserByToken(token);
            if (userByToken.getData().equals(Constants.HTTP_RES_CODE_200)){
                LinkedHashMap userData = (LinkedHashMap) userByToken.getData();
                String  username = (String) userData.get("username");
                request.setAttribute("username",username);
            }
        }
        return INDEX;
    }
}

