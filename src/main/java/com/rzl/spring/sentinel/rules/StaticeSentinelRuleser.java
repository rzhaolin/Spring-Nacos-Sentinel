package com.rzl.spring.sentinel.rules;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

// 定义为Component，自动初始化规则
@Component
public class StaticeSentinelRuleser {
    // 资源key
    public static final String RESOURCE_LOGIN = "login"; // 登录
    public static final String RESOURCE_LOGOUT = "logout"; // 登出

    @Bean
    @PostConstruct // 注解不可少
    public static void initFlowQpsRule() {
        List<FlowRule> rules = new ArrayList<FlowRule>();

        // login ruls
        FlowRule flowRule = new FlowRule();
        setLimitFlowRule(rules, flowRule, RESOURCE_LOGIN);

        // logout ruls
        flowRule = new FlowRule();
        setLimitFlowRule(rules, flowRule, RESOURCE_LOGOUT);

        FlowRuleManager.loadRules(rules);
    }

    @Bean
    @PostConstruct // 注解不可少
    public static void initDegrateRule() {
        List<DegradeRule> rules = new ArrayList<DegradeRule>();

        // login ruls
        DegradeRule degradeRule = new DegradeRule();
        setDegradeRule(rules, degradeRule, RESOURCE_LOGIN);

        // logout ruls
        degradeRule = new DegradeRule();
        setDegradeRule(rules, degradeRule, RESOURCE_LOGOUT);

        DegradeRuleManager.loadRules(rules);
    }

    /**
     * 初始限流rule配置
     * {
     *         "resource":"your resource name",
     *         "limitAPP":"default",
     *         "grade":1,
     *         "count":20,
     *         "clusterMode":false
     *         "strategy":0,
     *         "controlBehavior":0,
     *  }
     */
    private static void setLimitFlowRule(List<FlowRule> rules, FlowRule flowRule, String resourceLogout) {
        flowRule.setResource(resourceLogout); // 资源名
        flowRule.setLimitApp(RuleConstant.LIMIT_APP_DEFAULT); // 针对来源，默认
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS); // 阈值类型，QPS限流
        flowRule.setCount(2); // 单机阈值，QPS控制在2以内
        flowRule.setClusterMode(false); // 是否是集群
        flowRule.setStrategy(RuleConstant.STRATEGY_DIRECT); // 流控模式
        flowRule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT); // 流控效果
        rules.add(flowRule);
    }

    /**
     * 初始降级rule配置
     * {
     *         "resource":"your resource name",
     *         "grade":1,
     *         "count":20,
     *         "slowRatioThreshold":0.5
     *         "timeWindow":1000,
     *         "minRequestAmount ":5,
     *         "statIntervalMs": 1000
     *  }
     */
    private static void setDegradeRule(List<DegradeRule> rules, DegradeRule degradeRule, String resourceLogin) {
        degradeRule.setResource(resourceLogin); // 资源名
        degradeRule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT); // 熔断策略
        degradeRule.setCount(2); // 域值，最大RT
        degradeRule.setSlowRatioThreshold(0.5);  // 比例阈值
        degradeRule.setTimeWindow(10); // 熔断时长
        degradeRule.setMinRequestAmount(5); // 最小请求数
        degradeRule.setStatIntervalMs(1000); // 统计时长
        rules.add(degradeRule);
    }
}
