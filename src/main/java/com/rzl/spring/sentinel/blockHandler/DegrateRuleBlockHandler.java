package com.rzl.spring.sentinel.blockHandler;


public class DegrateRuleBlockHandler {
    public static String handlerDegrateError(String userName, String psw, Throwable e) {
        return "服务异常，降级处理，请等候。";
    }

    public static String handlerDegrateError(Throwable e) {
        return "服务异常，降级处理，请等候。";
    }

}
