package com.lm.base;

import com.lm.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseApiService {
    @Autowired
    protected BaseRedisService baseRedisService;
    //返回失败，传入msg
    public ResponseBase setResultError(String msg){
        return new ResponseBase(Constants.HTTP_RES_CODE_500,msg,null);
    }
    //验证QQ账号没有绑定用户，提示绑定
    public ResponseBase setResultError(Integer code,String msg){
        return new ResponseBase(code,msg,null);
    }
    //返回成功，有data值
    public ResponseBase setResultSuccess(Object data){
        return new ResponseBase(Constants.HTTP_RES_CODE_200,Constants.HTTP_RES_CODE_200_VALUE,data);
    }
    //返回成功，没有data值
    public ResponseBase setResultSuccess(){
        return new ResponseBase(Constants.HTTP_RES_CODE_200,Constants.HTTP_RES_CODE_200_VALUE,null);
    }

    //返回成功，传入msg
    public ResponseBase setResultSuccess(String msg){
        return new ResponseBase(Constants.HTTP_RES_CODE_200,msg,null);
    }
    //通用封装
    public ResponseBase setResult(Integer code,String msg,Object data){
        return new ResponseBase(code,msg,data);
    }
}
