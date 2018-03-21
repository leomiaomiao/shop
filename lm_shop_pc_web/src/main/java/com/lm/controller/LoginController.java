package com.lm.controller;

import com.lm.base.ResponseBase;
import com.lm.constant.Constants;
import com.lm.entity.UserEntity;
import com.lm.feign.MemberServiceFegin;
import com.lm.util.CookieUtil;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.oauth.Oauth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;

@Controller
@Slf4j
public class LoginController {

    private static final String LOGIN = "login";
    private static final String INDEX = "redirect:/";
    private static final String QQRELATION = "qqrelation";
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

        if (!login.getCode().equals(Constants.HTTP_RES_CODE_200)){
            request.setAttribute("error","账号或密码错误");
            return LOGIN;
        }

        LinkedHashMap data = (LinkedHashMap) login.getData();
        String memberToken = (String) data.get("memberToken");
        if (StringUtils.isEmpty(memberToken)){
            request.setAttribute("error","会话已经失效");
            return LOGIN;
        }
        //3.将token信息存储在cookie中
       setCookie(memberToken,response);
        return INDEX;
    }

    //将token信息存储在cookie中
    public void setCookie(String memberToken,HttpServletResponse response){
        CookieUtil.addCookie(response,Constants.COOKIE_MEMEBER_TOKEN,memberToken,Constants.COOKIE_TOKEN_MEMBER_TIME);
    }
    //生成 QQ授权链接,相当于点击QQ按钮，准备授权
    @RequestMapping("/localQQLogin")
    public String localQQLogin(HttpServletRequest request) throws QQConnectException {
        //生成QQ授权链接
        String authorizeURL = new Oauth().getAuthorizeURL(request);
        return "redirect:" + authorizeURL;
    }

    //生成授权码链接
    @RequestMapping("/qqLoginCallback")
    public String qqLoginCallback(HttpServletRequest request,HttpServletResponse response, HttpSession httpSession) throws QQConnectException {
        //1.获取授权码code
        //2.使用授权码获取accessToken
        AccessToken accessTokenOj = new Oauth().getAccessTokenByRequest(request);
        if (accessTokenOj == null){
            request.setAttribute("error","QQ授权失败");
            return "error";
        }
        String accessToken = accessTokenOj.getAccessToken();
        //3.使用accessToken获取openID
        OpenID openIdOj = new OpenID(accessToken);
        String userOpenID = openIdOj.getUserOpenID();
        log.info("*****openid是:{}******",userOpenID);
        //4.调用会员服务接口，使用userOpenId查找是否已经关联过用户
        ResponseBase userByOpenid = memberServiceFegin.findUserByOpenid(userOpenID);
        if (userByOpenid.getCode().equals(Constants.HTTP_RES_CODE_201)){
            //5.如果没有关联账号，跳转到关联账号。
            httpSession.setAttribute("qqOpenid",userOpenID);
            return QQRELATION;
        }
        //6.已经绑定账号，跳转到index页面,将用户token存储在本地
        LinkedHashMap dataTokenMap = (LinkedHashMap) userByOpenid.getData();
        String memberToken = (String) dataTokenMap.get("memberToken");
        setCookie(memberToken,response);
        return INDEX;
    }


    @RequestMapping(value = "/qqRelation",method = RequestMethod.POST)
    public String qqRelation(UserEntity userEntity, HttpServletRequest request, HttpServletResponse response,HttpSession httpSession){
        //1.获取openid
        String qqOpenid = (String) httpSession.getAttribute("qqOpenid");
        if (StringUtils.isEmpty(qqOpenid)){
            request.setAttribute("error","未获取到openID");
            return "error";
        }

        //2.调用登录接口，获取token
        userEntity.setOpenid(qqOpenid);
        ResponseBase login = memberServiceFegin.qqLogin(userEntity);

        if (!login.getCode().equals(Constants.HTTP_RES_CODE_200)){
            request.setAttribute("error","账号或密码错误");
            return LOGIN;
        }

        LinkedHashMap data = (LinkedHashMap) login.getData();
        String memberToken = (String) data.get("memberToken");
        if (StringUtils.isEmpty(memberToken)){
            request.setAttribute("error","会话已经失效");
            return LOGIN;
        }
        //3.将token信息存储在cookie中
        setCookie(memberToken,response);
        return INDEX;
    }
}
