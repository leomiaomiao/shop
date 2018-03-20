package com.lm.controller;

import com.alibaba.fastjson.JSONObject;
import com.lm.base.TextMessage;
import com.lm.util.CheckUtil;
import com.lm.util.HttpClientUtil;
import com.lm.util.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

@Slf4j
@RestController
public class TestConnController {

        private static final String REQUEST_URL="http://api.qingyunke.com/api.php?key=free&appid=0&msg=";

        /*
        * signature	微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
           timestamp	时间戳
           nonce	随机数
           echostr	随机字符串
        * */
        //服务器验证接口地址
        @RequestMapping(value = "/testConn",method = RequestMethod.GET)
        public String testConn(String signature,String timestamp,String nonce,String echostr){
            //1.验证参数
            boolean checkSignature = CheckUtil.checkSignature(signature, timestamp, nonce);
            //2.验证参数成功后返回随机数
            if (!checkSignature){
                return null;
            }
            return echostr;
        }

    //服务器验证接口地址
    @RequestMapping(value = "/testConn",method = RequestMethod.POST)
    public void testConn(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        //1.将XML转换成map集合
        Map<String, String> stringStringMap = XmlUtils.parseXml(request);
        String msgType = stringStringMap.get("MsgType");
        //2.如果是文本消息，返回结果给微信端
        PrintWriter writer = response.getWriter();
        switch (msgType){
            case "text":
                //1.获取开发者微信号
                String toUserName = stringStringMap.get("ToUserName");
                //2.获取发送方账号
                String fromUserName = stringStringMap.get("FromUserName");
                //3.消息内容
                String content = stringStringMap.get("Content");
                log.info("_________s收到消息:{}__________"+content);
                String resultJson = HttpClientUtil.doGet(REQUEST_URL + content);
                JSONObject jsonObject = JSONObject.parseObject(resultJson);
                Integer resultCode = jsonObject.getInteger("result");
                String msg = null;
                if (resultCode == 0) {
                    String resultContent = jsonObject.getString("content");
                    msg = setText(resultContent, toUserName, fromUserName);
                }else{
                    msg = setText("爱丹丹", toUserName, fromUserName);
                }

                /*if (content.equals("刘淼")){
                    //返回相关信息
                    msg = setText("爱丹丹", toUserName, fromUserName);
                }else {
                    msg = setText("依然还是爱丹丹、爱丹丹", toUserName, fromUserName);
                }*/
                log.info("++++++++回复微信消息:{}+++++++++++++"+msg);
                writer.println(msg);
                break;
        }
        writer.close();
    }


    public String setText(String content,String fromUserName,String toUserName){
        TextMessage textMessage = new TextMessage();
        textMessage.setContent(content);
        textMessage.setCreateTime(new Date().getTime());
        textMessage.setFromUserName(fromUserName);
        textMessage.setToUserName(toUserName);
        textMessage.setMsgType("text");
        //将实体类转换成XML格式
        String msg = XmlUtils.messageToXml(textMessage);
        return msg;
    }
}
