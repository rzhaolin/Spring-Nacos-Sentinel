package com.rzl.spring.sentinel.blockHandler;

import com.alibaba.csp.sentinel.slots.block.BlockException;

public class LimitRuleBlockHandler {
    public static String blockHandlerLimitError(String userName, String psw, BlockException e) {
        return "流量过大，开始限流，请等候。";
    }

    public static String blockHandlerLimitError(BlockException e) {
        return "流量过大，开始限流，请等候。";
    }
}
