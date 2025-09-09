package io.github.fushuwei.nacos.service;

import io.github.fushuwei.nacos.config.ConfigManagerProperties;
import io.github.fushuwei.nacos.entity.ConfigMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 配置同步服务类
 *
 * @author example
 * @version 1.0.0
 */
@Slf4j
@Service
public class ConfigSyncService {

    @Autowired
    private ConfigManagerProperties configManagerProperties;

    @Autowired
    private ConfigFileService configFileService;

    @Autowired
    private NacosConfigService nacosConfigService;

    @Autowired
    private ConfigBackupService configBackupService;

    /**
     * 应用启动完成后执行配置同步
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        if (!configManagerProperties.isSyncEnabled()) {
            log.info("配置同步已禁用，跳过启动时同步");
            return;
        }

        ConfigManagerProperties.SyncMode syncMode = configManagerProperties.getSyncMode();

        if (syncMode == ConfigManagerProperties.SyncMode.STARTUP ||
            syncMode == ConfigManagerProperties.SyncMode.BOTH) {
            log.info("应用启动完成，开始执行配置同步...");
            syncAllConfigs();
        }
    }

    /**
     * 定时同步配置
     */
    @Scheduled(fixedRateString = "#{${config.manager.sync-interval:30} * 60 * 1000}")
    public void scheduledSync() {
        if (!configManagerProperties.isSyncEnabled()) {
            return;
        }

        ConfigManagerProperties.SyncMode syncMode = configManagerProperties.getSyncMode();

        if (syncMode == ConfigManagerProperties.SyncMode.SCHEDULE ||
            syncMode == ConfigManagerProperties.SyncMode.BOTH) {
            log.info("开始执行定时配置同步...");
            syncAllConfigs();
        }
    }

    /**
     * 同步所有配置
     */
    public void syncAllConfigs() {
        try {
            log.info("==================== 开始配置同步 ====================");

            // 扫描配置文件
            List<ConfigMetadata> configList = configFileService.scanConfigFiles();

            if (configList.isEmpty()) {
                log.warn("未发现任何配置文件，同步结束");
                return;
            }

            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger skipCount = new AtomicInteger(0);
            AtomicInteger failCount = new AtomicInteger(0);

            // 遍历同步配置
            for (ConfigMetadata config : configList) {
                try {
                    SyncResult result = syncSingleConfig(config);

                    switch (result) {
                        case SUCCESS:
                            successCount.incrementAndGet();
                            break;
                        case SKIPPED:
                            skipCount.incrementAndGet();
                            break;
                        case FAILED:
                            failCount.incrementAndGet();
                            break;
                    }

                } catch (Exception e) {
                    log.error("同步配置异常: {}", config.getDataId(), e);
                    failCount.incrementAndGet();
                }
            }

            log.info("==================== 配置同步完成 ====================");
            log.info("同步结果统计 - 成功: {}, 跳过: {}, 失败: {}, 总计: {}",
                successCount.get(), skipCount.get(), failCount.get(), configList.size());

        } catch (Exception e) {
            log.error("配置同步过程异常", e);
        }
    }

    /**
     * 同步单个配置
     *
     * @param config 配置元数据
     * @return 同步结果
     */
    private SyncResult syncSingleConfig(ConfigMetadata config) {
        String dataId = config.getDataId();
        String group = config.getGroup();
        String namespace = config.getNamespace();

        log.debug("开始同步配置: dataId={}, group={}, namespace={}", dataId, group, namespace);

        try {
            // 检查配置是否已存在
            boolean exists = nacosConfigService.configExists(dataId, group);

            if (exists && !configManagerProperties.isOverrideExisting()) {
                log.info("配置已存在且不允许覆盖，跳过: dataId={}, group={}", dataId, group);
                return SyncResult.SKIPPED;
            }

            // 备份现有配置（如果存在且启用备份）
            if (exists && configManagerProperties.isBackupEnabled()) {
                String existingContent = nacosConfigService.getConfig(dataId, group, 5000);
                if (StringUtils.hasText(existingContent)) {
                    configBackupService.backupConfig(config, existingContent);
                }
            }

            // 发布配置到Nacos
            boolean success = nacosConfigService.publishConfig(config);

            if (success) {
                log.info("配置同步成功: dataId={}, group={}", dataId, group);
                return SyncResult.SUCCESS;
            } else {
                log.warn("配置同步失败: dataId={}, group={}", dataId, group);
                return SyncResult.FAILED;
            }

        } catch (Exception e) {
            log.error("同步配置异常: dataId={}, group={}", dataId, group, e);
            return SyncResult.FAILED;
        }
    }

    /**
     * 手动触发配置同步
     *
     * @return 同步结果摘要
     */
    public String manualSync() {
        log.info("收到手动同步请求");

        long startTime = System.currentTimeMillis();
        syncAllConfigs();
        long duration = System.currentTimeMillis() - startTime;

        String result = String.format("手动同步完成，耗时: %d ms", duration);
        log.info(result);

        return result;
    }

    /**
     * 同步指定配置文件
     *
     * @param environment 环境
     * @param appName     应用名
     * @param configName  配置名
     * @return 同步结果
     */
    public boolean syncSpecificConfig(String environment, String appName, String configName) {
        log.info("开始同步指定配置: env={}, app={}, config={}", environment, appName, configName);

        List<ConfigMetadata> configList = configFileService.scanConfigFiles();

        for (ConfigMetadata config : configList) {
            if (environment.equals(config.getEnvironment()) &&
                appName.equals(config.getAppName()) &&
                config.getDataId().contains(configName)) {

                SyncResult result = syncSingleConfig(config);
                boolean success = result == SyncResult.SUCCESS;

                log.info("指定配置同步{}: dataId={}, group={}",
                    success ? "成功" : "失败", config.getDataId(), config.getGroup());

                return success;
            }
        }

        log.warn("未找到指定的配置文件: env={}, app={}, config={}", environment, appName, configName);
        return false;
    }

    /**
     * 同步结果枚举
     */
    private enum SyncResult {
        SUCCESS,  // 同步成功
        SKIPPED,  // 跳过同步
        FAILED    // 同步失败
    }
}
