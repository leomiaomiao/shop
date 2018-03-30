package com.lm.feign;

import com.lm.api.service.PayService;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient("pay")
public interface PayServiceFeign extends PayService{
}
