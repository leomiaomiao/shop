package com.lm.api.service;

import com.lm.api.entity.PaymentInfo;
import com.lm.base.ResponseBase;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/pay")
public interface PayService {
    //创建支付令牌
    @RequestMapping("/createPayToken")
    public ResponseBase createPayToken(@RequestBody PaymentInfo paymentInfo);
    //使用支付令牌查询支付信息
    @RequestMapping("/findPayToken")
    public ResponseBase findPayToken(@RequestParam("payToken")String payToken);
}
