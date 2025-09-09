package io.github.fushuwei.nacos.service;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.exception.NacosException;
import io.github.fushuwei.nacos.config.ConfigManagerProperties;
import io.github.fushuwei.nacos.entity.ConfigMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * Nacos配置服务类
 *
 * @author example
 * @version 1.0.0
 */
@Slf4j
@Service
public class NacosConfigService {

    @Autowired
    private ConfigManagerProperties configManagerProperties;

    private ConfigService configService;

    public NacosConfigService() {
        try {
            Properties properties = new Properties();
            properties.put("serverAddr", configManagerProperties.getNacos().getServerAddr());
            properties.put("username", configManagerProperties.getNacos().getUsername());
            properties.put("password", configManagerProperties.getNacos().getPassword());

            this.configService = NacosFactory.createConfigService(properties);
            log.info("Nacos配置服务初始化成功, serverAddr: {}",
                configManagerProperties.getNacos().getServerAddr());
        } catch (NacosException e) {
            log.error("Nacos配置服务初始化失败", e);
            throw new RuntimeException("Nacos配置服务初始化失败", e);
        }
    }

    /**
     * 发布配置
     *
     * @param configMetadata 配置元数据
     * @return 发布结果
     */
    public boolean publishConfig(ConfigMetadata configMetadata) {
        try {
            String dataId = configMetadata.getDataId();
            String group = StringUtils.hasText(configMetadata.getGroup()) ?
                configMetadata.getGroup() : "DEFAULT_GROUP";
            String content = configMetadata.getContent();

            log.info("开始发布配置: dataId={}, group={}, namespace={}",
                dataId, group, configMetadata.getNamespace());

            boolean result = configService.publishConfig(dataId, group, content, getConfigType(configMetadata.getType()));

            if (result) {
                log.info("配置发布成功: dataId={}, group={}", dataId, group);
            } else {
                log.warn("配置发布失败: dataId={}, group={}", dataId, group);
            }

            return result;
        } catch (NacosException e) {
            log.error("发布配置异常: dataId={}, group={}",
                configMetadata.getDataId(), configMetadata.getGroup(), e);
            return false;
        }
    }

    /**
     * 获取配置
     *
     * @param dataId    数据ID
     * @param group     分组
     * @param timeoutMs 超时时间
     * @return 配置内容
     */
    public String getConfig(String dataId, String group, long timeoutMs) {
        try {
            return configService.getConfig(dataId, group, timeoutMs);
        } catch (NacosException e) {
            log.error("获取配置异常: dataId={}, group={}", dataId, group, e);
            return null;
        }
    }

    /**
     * 删除配置
     *
     * @param dataId 数据ID
     * @param group  分组
     * @return 删除结果
     */
    public boolean removeConfig(String dataId, String group) {
        try {
            return configService.removeConfig(dataId, group);
        } catch (NacosException e) {
            log.error("删除配置异常: dataId={}, group={}", dataId, group, e);
            return false;
        }
    }

    /**
     * 检查配置是否存在
     *
     * @param dataId 数据ID
     * @param group  分组
     * @return 是否存在
     */
    public boolean configExists(String dataId, String group) {
        String content = getConfig(dataId, group, 3000);
        return StringUtils.hasText(content);
    }

    /**
     * 根据文件类型获取ConfigType
     *
     * @param type 文件类型
     * @return ConfigType
     */
    private String getConfigType(String type) {
        if (!StringUtils.hasText(type)) {
            return ConfigType.YAML.getType();
        }

        switch (type.toLowerCase()) {
            case "yml":
            case "yaml":
                return ConfigType.YAML.getType();
            case "properties":
                return ConfigType.PROPERTIES.getType();
            case "json":
                return ConfigType.JSON.getType();
            case "xml":
                return ConfigType.XML.getType();
            case "html":
                return ConfigType.HTML.getType();
            case "text":
            case "txt":
                return ConfigType.TEXT.getType();
            default:
                return ConfigType.TEXT.getType();
        }
    }
}
