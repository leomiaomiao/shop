package com.lm.api.service.impl;

import com.lm.base.BaseApiService;
import com.lm.base.ResponseBase;
import org.springframework.web.bind.annotation.RestController;

import java.util.Hashtable;
import java.util.Map;

@RestController
public class TestApiServiceImpl extends BaseApiService implements TestApiService{

    @Override
    public Map<String, java.lang.Object> test(Integer id, String name) {
        Map<String,Object> result = new Hashtable<>();
        result.put("errorCode","200");
        result.put("messeage","success");
        result.put("data","id="+id+", name="+name);
        return result;
    }

    @Override
    public ResponseBase testResponseBase() {
        return setResultSuccess();
    }

    @Override
    public ResponseBase setTestRedis(String key, String value) {
        System.out.println(key +"++++++++++"+value);
        baseRedisService.setString(key,value,null);
        return setResultSuccess();
    }
}
