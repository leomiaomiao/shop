package com.lm.feign;

import com.lm.api.service.MemberService;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

@FeignClient("member")
@Component
public interface MemberServiceFegin extends MemberService{
}
