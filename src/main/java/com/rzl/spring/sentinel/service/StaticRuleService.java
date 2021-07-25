package com.rzl.spring.sentinel.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.rzl.spring.sentinel.blockHandler.DegrateRuleBlockHandler;
import com.rzl.spring.sentinel.blockHandler.LimitRuleBlockHandler;
import com.rzl.spring.sentinel.rules.DymaticSentinelRuleser;
import com.rzl.spring.sentinel.rules.StaticeSentinelRuleser;
import org.springframework.stereotype.Service;

@Service
public class StaticRuleService {
    @SentinelResource(value = StaticeSentinelRuleser.RESOURCE_LOGIN,
            blockHandlerClass = LimitRuleBlockHandler.class,
            blockHandler = "blockHandlerLimitError",
            fallbackClass = DegrateRuleBlockHandler.class,
            fallback = "handlerDegrateError")
    public String login(String userName, String psw) {
        // 测试降级的代码
        if (psw.equals("123456")) {
            throw new RuntimeException();
        }
        return "login successful";
    }

    @SentinelResource(value = StaticeSentinelRuleser.RESOURCE_LOGOUT,
            blockHandlerClass = LimitRuleBlockHandler.class,
            blockHandler = "blockHandlerLimitError",
            fallbackClass = DegrateRuleBlockHandler.class,
            fallback = "handlerDegrateError")
    public String logout() {
        return "logout successful";
    }
}
