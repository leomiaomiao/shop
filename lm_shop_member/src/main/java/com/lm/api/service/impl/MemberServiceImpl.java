package com.lm.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lm.base.BaseApiService;
import com.lm.base.ResponseBase;
import com.lm.constant.Constants;
import com.lm.dao.MemberDao;
import com.lm.entity.UserEntity;
import com.lm.mq.RegisterMailboxProducer;
import com.lm.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MemberServiceImpl extends BaseApiService implements MemberService{
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private RegisterMailboxProducer registerMailboxProducer;
    @Value("${messages.queue}")
    private String MESSAGESQUEUE;

    @Override
    public ResponseBase findUserById(Long userId) {
        UserEntity user = memberDao.findByID(userId);
        if (user == null){
            return setResultError("未找到该人");
        }
        return setResultSuccess(user);
    }

    @Override
    public ResponseBase regUser(@RequestBody UserEntity userEntity) {
        String password = userEntity.getPassword();
        if (StringUtils.isEmpty(password)){
            return setResultError("密码不可以为空");
        }
        String newPassword = MD5Util.MD5(password);
        userEntity.setPassword(newPassword);
        Integer result = memberDao.insertUser(userEntity);
        if(result<=0){
            return setResultError("注册失败");
        }
        //采用异步方式发送消息
        String email = userEntity.getEmail();
        String json = emailJson(email);
        log.info("+++++++++++会员服务推送消息到消息服务平台+++++json:{}",json);
        sendMsg(json);
        return setResultSuccess("用户注册成功");
    }

    private String emailJson(String email){
        JSONObject rootJson = new JSONObject();
        JSONObject header = new JSONObject();
        header.put("interfaceType", Constants.MSG_EMAIL);
        JSONObject content = new JSONObject();
        content.put("email",email);
        rootJson.put("header",header);
        rootJson.put("content",content);
        return rootJson.toString();
    }

    private void sendMsg(String json){
        ActiveMQQueue activeMQQueue = new ActiveMQQueue(MESSAGESQUEUE);
        registerMailboxProducer.sendMsg(activeMQQueue,json);
    }
}
