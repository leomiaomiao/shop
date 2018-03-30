package com.lm.util;

import com.lm.constant.Constants;

import java.util.UUID;

public class TokenUtils {

    public static String getMemberToken(){
        return Constants.TOKEN_MEMBER+"-"+ UUID.randomUUID();
    }

    public static String getPayToken(){
        return Constants.TOKEN_PAY + "-" +UUID.randomUUID();
    }
}
