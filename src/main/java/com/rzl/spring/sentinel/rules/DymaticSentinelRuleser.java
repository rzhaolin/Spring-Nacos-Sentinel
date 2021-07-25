package com.rzl.spring.sentinel.rules;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.rzl.spring.sentinel.nacos.NacosConfigEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DymaticSentinelRuleser {
    /**
     * 注册EventBus监听发送的规则事件
     */
    public DymaticSentinelRuleser() {
        EventBus.getDefault().register(this);
    }

    /**
     * EventBus反注册
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        EventBus.getDefault().unregister(this);
    }

    /**
     *  订阅
     * @param eventnt
     */
    @Subscribe
    public void onReceive(NacosConfigEvent eventnt) {
        String content = eventnt.content;
        parseRules(content);
    }

    private void parseRules(String content) {
        JSONArray array = null; // 配置的内容格式为JSONArray
        try {
            array = JSON.parseArray(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (array == null || array.isEmpty()) {
            return;
        }

        List<FlowRule> flowRules = new ArrayList<FlowRule>();
        List<DegradeRule> degradeRules = new ArrayList<DegradeRule>();

        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            switch (object.getString("type")) {
                case "limit-flow": { // 解析限流规则
                    FlowRule limitFlowRule = new Gson().fromJson(object.toString(), FlowRule.class);
                    flowRules.add(limitFlowRule);
                    break;
                }

                case "degrade": {  // 解析降级规则
                    DegradeRule degrateRule = new Gson().fromJson(object.toString(), DegradeRule.class);
                    degradeRules.add(degrateRule);
                    break;
                }

                default:
                    break;
            }
        }

        reloadRules(flowRules, degradeRules);
    }

    private void reloadRules(List<FlowRule> flowRules, List<DegradeRule> degradeRules) {
        if (!flowRules.isEmpty()) { // 重新加载限流规则，会update原来的规则
            FlowRuleManager.loadRules(flowRules);
        }

        if (!degradeRules.isEmpty()) { // 重新加载降级规则，会update原来的规则
            DegradeRuleManager.loadRules(degradeRules);
        }
    }
}
