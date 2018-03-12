package com.lm.api.service.impl;

import com.lm.api.service.TestApiService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Hashtable;
import java.util.Map;

@RestController
public class TestApiServiceImpl implements TestApiService{

    @Override
    public Map<String, java.lang.Object> test(Integer id, String name) {
        Map<String,Object> result = new Hashtable<>();
        result.put("errorCode","200");
        result.put("messeage","success");
        result.put("data","刘淼");
        return result;
    }
}
