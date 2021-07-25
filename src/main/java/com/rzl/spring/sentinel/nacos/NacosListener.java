package com.rzl.spring.sentinel.nacos;


import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.greenrobot.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.Executor;

@Component
class NacosListener {
    @Bean
    public static void  lisenNacosChanged() throws NacosException {
        String serverAddr = "127.0.0.1:8848";
        String dataId = "sentinel-dev.yaml";
        String group = "DEFAULT_GROUP";
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);

        // 主动拉取nacos配置中心的内容
        ConfigService configService = NacosFactory.createConfigService(properties);
        String content = configService.getConfig(dataId, group, 3000);
        EventBus.getDefault().post(new NacosConfigEvent(content)); // 通过EventBus发送配置内容给订阅者

        System.out.println("主动加载nacos配置中心的规则，内容是:\n");
        System.out.println(content);

        // 注册监听nacos配置中心内容的变化
        configService.addListener(dataId, group, new Listener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                EventBus.getDefault().post(new NacosConfigEvent(configInfo)); // 通过EventBus发送配置内容给订阅者
                System.out.println("nacos配置中心规则发生变化，内容是:\n");
                System.out.println(configInfo);
            }

            @Override
            public Executor getExecutor() {
                return null;
            }
        });
    }
}

