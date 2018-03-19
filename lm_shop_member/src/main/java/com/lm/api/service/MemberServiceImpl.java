package com.lm.api.service;

import com.alibaba.fastjson.JSONObject;
import com.lm.base.BaseApiService;
import com.lm.base.ResponseBase;
import com.lm.constant.Constants;
import com.lm.dao.MemberDao;
import com.lm.entity.UserEntity;
import com.lm.mq.RegisterMailboxProducer;
import com.lm.util.MD5Util;
import com.lm.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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
        userEntity.setCreated(new Date());
        userEntity.setUpdated(new Date());
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

    @Override
    public ResponseBase login(@RequestBody UserEntity userEntity) {
        //1.验证参数
        String username = userEntity.getUsername();
        if (StringUtils.isEmpty(username)){
            return setResultError("用户名不能为空");
        }
        String password = userEntity.getPassword();
        if (StringUtils.isEmpty(password)){
            return setResultError("密码不能为空");
        }
        //2.数据库查找账号密码是否正确
        String newpassword = MD5Util.MD5(password);
        UserEntity user = memberDao.login(username, newpassword);
        if (user == null){
            return setResultError("账号或者密码不正确");
        }
        //3.如果账号密码正确，生成token
        String memberToken = TokenUtils.getMemberToken();
        //4.存放在redis中，key为token，value为userID；
        Integer userId = user.getId();
        log.info("******用户信息token存放在redis中*******key:{},value:{}",memberToken,userId);
        baseRedisService.setString(memberToken,userId+"",Constants.TOKEN_MEMBER_TIME);
        //5.返回token
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("memberToken",memberToken);
        return setResultSuccess(jsonObject);
    }

    @Override
    public ResponseBase findUserByToken(@RequestParam("token") String token) {
        //1.验证参数
        if (StringUtils.isEmpty(token)){
            return setResultError("token不能为空");
        }
        //2.从redis中根据token查找userid
        String  userid = (String) baseRedisService.getString(token);
        if (StringUtils.isEmpty(userid)){
            return setResultError("Token无效或者已经过期");
        }
        //3.根据userid从数据库中查询用户信息返回给客户端
        long userId = Long.parseLong(userid);
        UserEntity userEntity = memberDao.findByID(userId);
        userEntity.setPassword(""); //返回用户信息时需要将密码重置，或者重新返回一个新的对象返回给客户端
        if (userEntity==null){
            return setResultError("该用户信息不存在");
        }
        return setResultSuccess(userEntity);
    }

    /*
* 邮件发送，将邮件地址封装为json类型
* */
    private String emailJson(String email){
        //1.先new一个json对象rootJson
        JSONObject rootJson = new JSONObject();
        //2.new 一个json对象 header
        JSONObject header = new JSONObject();
        //3.在json对象header 中设置接口类型为email
        header.put("interfaceType", Constants.MSG_EMAIL);
        //4.new一个json对象 content 从来存储 固定接口类型的内容，例如邮件地址
        JSONObject content = new JSONObject();
        content.put("email",email);  //将邮件地址存进content
        //5.将json对象 header 和 content 都存进 json对象 rootJson中
        rootJson.put("header",header);
        rootJson.put("content",content);
        return rootJson.toString();
    }

    //将json内容存储在activeMQ中
    private void sendMsg(String json){
        //创建一个activeMQ队列
        ActiveMQQueue activeMQQueue = new ActiveMQQueue(MESSAGESQUEUE);
        registerMailboxProducer.sendMsg(activeMQQueue,json);
    }
}
