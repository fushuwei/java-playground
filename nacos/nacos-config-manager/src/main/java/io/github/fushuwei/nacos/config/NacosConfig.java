package io.github.fushuwei.nacos.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Nacos配置类
 *
 * @author example
 * @version 1.0.0
 */
@Slf4j
@Configuration
public class NacosConfig {

    @Autowired
    private ConfigManagerProperties configManagerProperties;

    /**
     * 创建Nacos ConfigService Bean
     *
     * @return ConfigService实例
     * @throws NacosException Nacos异常
     */
    @Bean
    public ConfigService configService() throws NacosException {
        Properties properties = new Properties();
        properties.put("serverAddr", configManagerProperties.getNacos().getServerAddr());
        properties.put("username", configManagerProperties.getNacos().getUsername());
        properties.put("password", configManagerProperties.getNacos().getPassword());

        ConfigService configService = NacosFactory.createConfigService(properties);
        log.info("Nacos配置服务初始化成功, serverAddr: {}",
            configManagerProperties.getNacos().getServerAddr());

        return configService;
    }
}
