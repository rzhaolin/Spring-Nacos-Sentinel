package com.rzl.spring.sentinel.controller;

import com.rzl.spring.sentinel.service.StaticRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class MyController {
    @Autowired
    private StaticRuleService service;

    @GetMapping(path = "/sentinel-rule/login")
    public String login(String userName, String password) {
        return service.login(userName, password);
    }

    @GetMapping(path = "/sentinel-rule/logout")
    public String logout() {
        return service.logout();
    }
}
