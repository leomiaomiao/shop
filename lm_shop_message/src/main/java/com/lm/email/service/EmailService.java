package com.lm.email.service;

import com.alibaba.fastjson.JSONObject;
import com.lm.adapter.MessageAdapter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

//处理第三方接口
@Slf4j
@Service
public class EmailService implements MessageAdapter{

    @Value("${spring.mail.username}")
    private String from;
    @Value("${msg.subject}")
    private String subject;   //标题配置在yml文件中
    @Value("${msg.text}")
    private String text; //邮件内容
    @Autowired
    private JavaMailSender javaMailSender;      //调用该接口发邮件
    @Override
    public void sendMsg(JSONObject body) {
        //处理发送邮件
        String email = body.getString("email");
        if (StringUtils.isEmpty(email)){
            return;
        }
        log.info("+++++++消息服务平台发送邮件:{}++++++++",email);

        //java 发送消息的封装类
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        //来自账号
        simpleMailMessage.setFrom(from);
        //发送至
        simpleMailMessage.setTo(email);
        //标题
        simpleMailMessage.setSubject(subject);
        //内容
        simpleMailMessage.setText(text.replace("{}",email));
        //发送邮件
        javaMailSender.send(simpleMailMessage);
    }
}
