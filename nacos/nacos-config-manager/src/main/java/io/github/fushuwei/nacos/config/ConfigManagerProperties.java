package io.github.fushuwei.nacos.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 配置管理属性类
 *
 * @author example
 * @version 1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "config.manager")
public class ConfigManagerProperties {

    /**
     * 配置文件根目录
     */
    private String configRootPath = "configs";

    /**
     * 是否启用配置同步
     */
    private boolean syncEnabled = true;

    /**
     * 同步模式
     */
    private SyncMode syncMode = SyncMode.STARTUP;

    /**
     * 定时同步间隔(分钟)
     */
    private int syncInterval = 30;

    /**
     * 是否覆盖已存在的配置
     */
    private boolean overrideExisting = true;

    /**
     * 配置备份
     */
    private boolean backupEnabled = true;

    /**
     * 备份路径
     */
    private String backupPath = "backup";

    /**
     * 支持的环境列表
     */
    private List<String> environments;

    /**
     * Nacos配置
     */
    private NacosConfig nacos = new NacosConfig();

    /**
     * 同步模式枚举
     */
    public enum SyncMode {
        STARTUP, SCHEDULE, BOTH
    }

    /**
     * Nacos连接配置
     */
    @Data
    public static class NacosConfig {
        private String serverAddr = "localhost:8848";
        private String username = "nacos";
        private String password = "nacos";
        private int timeout = 5000;
    }
}
