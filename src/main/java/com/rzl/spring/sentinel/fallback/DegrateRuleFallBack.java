package com.rzl.spring.sentinel.fallback;

public class DegrateRuleFallBack {
    public static String degratFallback(Throwable e) {
        return "服务异常，降级处理中，请稍侯。";
    }
}
