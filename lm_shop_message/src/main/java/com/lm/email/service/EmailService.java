package com.lm.email.service;

import com.alibaba.fastjson.JSONObject;
import com.lm.adapter.MessageAdapter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

//处理第三方接口
@Service
public class EmailService implements MessageAdapter{
    @Override
    public void sendMsg(JSONObject body) {
        //处理发送邮件
        String email = body.getString("email");
        if (StringUtils.isEmpty(email)){
            return;
        }
    }
}
