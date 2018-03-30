package com.lm.controller;

import com.lm.base.ResponseBase;
import com.lm.constant.Constants;
import com.lm.feign.PayServiceFeign;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;

@Controller
public class PayController {

    @Autowired
    private PayServiceFeign payServiceFeign;

    @RequestMapping("/alipay")
    public void alipay(String payToken ,HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer =  response.getWriter();
        //1.验证参数
        if (StringUtils.isEmpty(payToken)){
            return;
        }
        //2.调用支付服务接口，获取支付服务的HTML元素
        ResponseBase responseBase = payServiceFeign.findPayToken(payToken);
        if (!responseBase.getCode().equals(Constants.HTTP_RES_CODE_200)){
            String msg = responseBase.getMsg();
            writer.println(msg);
            return;
        }
        //3.返回可以执行的HTML元素给客户端
        LinkedHashMap data = (LinkedHashMap) responseBase.getData();
        String payHtml = (String) data.get("payHtml");
        //4.页面上渲染
        writer.println(payHtml);
        writer.close();
    }
}
