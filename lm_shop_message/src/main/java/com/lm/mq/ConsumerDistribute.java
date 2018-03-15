package com.lm.mq;

import com.alibaba.fastjson.JSONObject;
import com.lm.adapter.MessageAdapter;
import com.lm.constant.Constants;
import com.lm.email.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConsumerDistribute {

    @Autowired
    private EmailService emailService;
    private MessageAdapter messageAdapter;
    //监听消息
    @JmsListener(destination = "messages_queue")
    public void distribute(String json){
        log.info("--------消息服务平台接受消息内容:{}---",json);
        if(StringUtils.isEmpty(json)){
            return;
        }
        JSONObject rootJSON = new JSONObject().parseObject(json);
        JSONObject header = rootJSON.getJSONObject("header");
        String interfaceType = header.getString("interfaceType");
        if (StringUtils.isEmpty(interfaceType)){
            return;
        }
        if (interfaceType.equals(Constants.MSG_EMAIL)){
            messageAdapter = emailService;
        }
        JSONObject content = rootJSON.getJSONObject("content");
        messageAdapter.sendMsg(content);
    }
}
