package com.lm.api.service.impl;

import com.lm.base.ResponseBase;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/member")
public interface TestApiService {
    @RequestMapping("/test")
    public Map<String,Object> test(Integer id,String name);
    @RequestMapping("/testResponseBase")
    public ResponseBase testResponseBase();
    @RequestMapping("/testRedis")
    public ResponseBase setTestRedis(String key,String value);
}
