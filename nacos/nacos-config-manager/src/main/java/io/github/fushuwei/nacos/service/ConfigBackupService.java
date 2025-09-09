package io.github.fushuwei.nacos.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import io.github.fushuwei.nacos.config.ConfigManagerProperties;
import io.github.fushuwei.nacos.entity.ConfigMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 配置备份服务类
 *
 * @author example
 * @version 1.0.0
 */
@Slf4j
@Service
public class ConfigBackupService {

    @Autowired
    private ConfigManagerProperties configManagerProperties;

    /**
     * 备份配置
     *
     * @param config 配置元数据
     * @param existingContent 现有配置内容
     */
    public void backupConfig(ConfigMetadata config, String existingContent) {
        if (!configManagerProperties.isBackupEnabled()) {
            return;
        }

        try {
            String backupDir = configManagerProperties.getBackupPath();
            String timestamp = DateUtil.format(DateUtil.date(), "yyyyMMdd_HHmmss");

            // 构建备份文件路径
            String backupFileName = String.format("%s_%s_%s_%s.bak",
                config.getEnvironment(),
                config.getAppName(),
                config.getDataId(),
                timestamp);

            String backupFilePath = backupDir + File.separator +
                config.getEnvironment() + File.separator +
                config.getAppName() + File.separator +
                backupFileName;

            // 创建备份目录
            File backupFile = new File(backupFilePath);
            FileUtil.mkParentDirs(backupFile);

            // 写入备份内容
            FileUtil.writeString(existingContent, backupFile, StandardCharsets.UTF_8);

            log.info("配置备份成功: {} -> {}", config.getDataId(), backupFilePath);

        } catch (Exception e) {
            log.error("配置备份失败: {}", config.getDataId(), e);
        }
    }

    /**
     * 清理过期备份文件
     *
     * @param retentionDays 保留天数
     */
    public void cleanupExpiredBackups(int retentionDays) {
        if (!configManagerProperties.isBackupEnabled()) {
            return;
        }

        try {
            String backupDir = configManagerProperties.getBackupPath();
            File backupDirFile = new File(backupDir);

            if (!backupDirFile.exists() || !backupDirFile.isDirectory()) {
                return;
            }

            long cutoffTime = System.currentTimeMillis() - (retentionDays * 24L * 60 * 60 * 1000);
            int deletedCount = 0;

            deletedCount += cleanupDirectory(backupDirFile, cutoffTime);

            log.info("过期备份文件清理完成，共删除 {} 个文件", deletedCount);

        } catch (Exception e) {
            log.error("清理过期备份文件失败", e);
        }
    }

    /**
     * 递归清理目录中的过期文件
     *
     * @param directory 目录
     * @param cutoffTime 截止时间
     * @return 删除的文件数量
     */
    private int cleanupDirectory(File directory, long cutoffTime) {
        int deletedCount = 0;

        File[] files = directory.listFiles();
        if (files == null) {
            return 0;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                deletedCount += cleanupDirectory(file, cutoffTime);
                // 如果目录为空，删除目录
                if (file.list() != null && file.list().length == 0) {
                    file.delete();
                }
            } else if (file.isFile() && file.getName().endsWith(".bak")) {
                if (file.lastModified() < cutoffTime) {
                    if (file.delete()) {
                        deletedCount++;
                        log.debug("删除过期备份文件: {}", file.getAbsolutePath());
                    }
                }
            }
        }

        return deletedCount;
    }
}
