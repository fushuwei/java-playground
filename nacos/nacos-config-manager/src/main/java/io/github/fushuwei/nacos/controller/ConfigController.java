package io.github.fushuwei.nacos.controller;

import io.github.fushuwei.nacos.entity.ConfigMetadata;
import io.github.fushuwei.nacos.service.ConfigBackupService;
import io.github.fushuwei.nacos.service.ConfigFileService;
import io.github.fushuwei.nacos.service.ConfigSyncService;
import io.github.fushuwei.nacos.service.NacosConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置管理控制器
 *
 * @author example
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/config")
public class ConfigController {

    @Autowired
    private ConfigSyncService configSyncService;

    @Autowired
    private ConfigFileService configFileService;

    @Autowired
    private NacosConfigService nacosConfigService;

    @Autowired
    private ConfigBackupService configBackupService;

    /**
     * 手动触发配置同步
     */
    @PostMapping("/sync")
    public ResponseEntity<Map<String, Object>> manualSync() {
        Map<String, Object> result = new HashMap<>();

        try {
            String syncResult = configSyncService.manualSync();
            result.put("success", true);
            result.put("message", syncResult);
            result.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("手动同步失败", e);
            result.put("success", false);
            result.put("message", "同步失败: " + e.getMessage());

            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 同步指定配置
     */
    @PostMapping("/sync/specific")
    public ResponseEntity<Map<String, Object>> syncSpecific(
        @RequestParam String environment,
        @RequestParam String appName,
        @RequestParam String configName) {

        Map<String, Object> result = new HashMap<>();

        try {
            boolean success = configSyncService.syncSpecificConfig(environment, appName, configName);
            result.put("success", success);
            result.put("message", success ? "指定配置同步成功" : "指定配置同步失败");
            result.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("同步指定配置失败", e);
            result.put("success", false);
            result.put("message", "同步失败: " + e.getMessage());

            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 扫描配置文件列表
     */
    @GetMapping("/scan")
    public ResponseEntity<Map<String, Object>> scanConfigs() {
        Map<String, Object> result = new HashMap<>();

        try {
            List<ConfigMetadata> configList = configFileService.scanConfigFiles();

            result.put("success", true);
            result.put("data", configList);
            result.put("total", configList.size());
            result.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("扫描配置文件失败", e);
            result.put("success", false);
            result.put("message", "扫描失败: " + e.getMessage());

            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取Nacos中的配置内容
     */
    @GetMapping("/nacos")
    public ResponseEntity<Map<String, Object>> getNacosConfig(
        @RequestParam String dataId,
        @RequestParam(defaultValue = "DEFAULT_GROUP") String group) {

        Map<String, Object> result = new HashMap<>();

        try {
            String content = nacosConfigService.getConfig(dataId, group, 5000);

            result.put("success", true);
            result.put("dataId", dataId);
            result.put("group", group);
            result.put("content", content);
            result.put("exists", content != null);
            result.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("获取Nacos配置失败", e);
            result.put("success", false);
            result.put("message", "获取配置失败: " + e.getMessage());

            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 删除Nacos中的配置
     */
    @DeleteMapping("/nacos")
    public ResponseEntity<Map<String, Object>> removeNacosConfig(
        @RequestParam String dataId,
        @RequestParam(defaultValue = "DEFAULT_GROUP") String group) {

        Map<String, Object> result = new HashMap<>();

        try {
            boolean success = nacosConfigService.removeConfig(dataId, group);

            result.put("success", success);
            result.put("dataId", dataId);
            result.put("group", group);
            result.put("message", success ? "配置删除成功" : "配置删除失败");
            result.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("删除Nacos配置失败", e);
            result.put("success", false);
            result.put("message", "删除配置失败: " + e.getMessage());

            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 清理过期备份文件
     */
    @PostMapping("/backup/cleanup")
    public ResponseEntity<Map<String, Object>> cleanupBackups(
        @RequestParam(defaultValue = "7") int retentionDays) {

        Map<String, Object> result = new HashMap<>();

        try {
            configBackupService.cleanupExpiredBackups(retentionDays);

            result.put("success", true);
            result.put("message", "备份文件清理完成");
            result.put("retentionDays", retentionDays);
            result.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("清理备份文件失败", e);
            result.put("success", false);
            result.put("message", "清理失败: " + e.getMessage());

            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("service", "config-manager");
        result.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(result);
    }
}
