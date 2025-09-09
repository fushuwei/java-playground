package io.github.fushuwei.nacos.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import io.github.fushuwei.nacos.config.ConfigManagerProperties;
import io.github.fushuwei.nacos.entity.ConfigMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 配置文件服务类
 *
 * @author example
 * @version 1.0.0
 */
@Slf4j
@Service
public class ConfigFileService {

    @Autowired
    private ConfigManagerProperties configManagerProperties;

    private final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    /**
     * 扫描配置文件
     *
     * @return 配置元数据列表
     */
    public List<ConfigMetadata> scanConfigFiles() {
        List<ConfigMetadata> configList = new ArrayList<>();

        try {
            String configPath = configManagerProperties.getConfigRootPath();
            String pattern = "classpath:" + configPath + "/**/*.{yml,yaml,properties,json,xml}";

            log.info("开始扫描配置文件, 路径模式: {}", pattern);

            Resource[] resources = resourcePatternResolver.getResources(pattern);

            for (Resource resource : resources) {
                try {
                    ConfigMetadata metadata = parseConfigFile(resource);
                    if (metadata != null) {
                        configList.add(metadata);
                        log.debug("解析配置文件成功: {}", metadata.getFilePath());
                    }
                } catch (Exception e) {
                    log.warn("解析配置文件失败: {}, 错误: {}", resource.getFilename(), e.getMessage());
                }
            }

            log.info("配置文件扫描完成, 共发现 {} 个配置文件", configList.size());

        } catch (IOException e) {
            log.error("扫描配置文件失败", e);
        }

        return configList;
    }

    /**
     * 解析配置文件
     *
     * @param resource 资源文件
     * @return 配置元数据
     */
    private ConfigMetadata parseConfigFile(Resource resource) throws IOException {
        String filename = resource.getFilename();
        if (!StringUtils.hasText(filename)) {
            return null;
        }

        // 读取文件内容
        String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        // 获取文件路径
        String filePath = getRelativeFilePath(resource);

        // 解析文件路径，提取环境、应用名等信息
        PathInfo pathInfo = parseFilePath(filePath);

        // 构建配置元数据
        return ConfigMetadata.builder()
            .dataId(generateDataId(pathInfo))
            .group(generateGroup(pathInfo))
            .namespace(pathInfo.environment)
            .content(content)
            .type(getFileExtension(filename))
            .description(generateDescription(pathInfo))
            .appName(pathInfo.appName)
            .environment(pathInfo.environment)
            .filePath(filePath)
            .lastModified(System.currentTimeMillis())
            .contentMd5(DigestUtil.md5Hex(content))
            .build();
    }

    /**
     * 获取相对文件路径
     *
     * @param resource 资源文件
     * @return 相对路径
     */
    private String getRelativeFilePath(Resource resource) {
        try {
            String uri = resource.getURI().toString();
            String configRoot = configManagerProperties.getConfigRootPath();
            int index = uri.indexOf(configRoot);
            if (index != -1) {
                return uri.substring(index);
            }
            return resource.getFilename();
        } catch (IOException e) {
            return resource.getFilename();
        }
    }

    /**
     * 解析文件路径信息
     * 路径格式示例: configs/dev/user-service/application.yml
     * configs/test/order-service/database.yml
     *
     * @param filePath 文件路径
     * @return 路径信息
     */
    private PathInfo parseFilePath(String filePath) {
        PathInfo pathInfo = new PathInfo();

        // 移除配置根路径前缀
        String relativePath = filePath;
        String configRoot = configManagerProperties.getConfigRootPath();
        if (relativePath.startsWith(configRoot)) {
            relativePath = relativePath.substring(configRoot.length());
        }

        // 移除开头的斜杠
        if (relativePath.startsWith("/") || relativePath.startsWith("\\")) {
            relativePath = relativePath.substring(1);
        }

        String[] parts = relativePath.split("[/\\\\]");

        if (parts.length >= 1) {
            pathInfo.environment = parts[0];
        }
        if (parts.length >= 2) {
            pathInfo.appName = parts[1];
        }
        if (parts.length >= 3) {
            pathInfo.fileName = parts[parts.length - 1];
            pathInfo.configName = getFileNameWithoutExtension(pathInfo.fileName);
        }

        return pathInfo;
    }

    /**
     * 生成数据ID
     * 格式: 应用名-配置名.文件扩展名
     *
     * @param pathInfo 路径信息
     * @return 数据ID
     */
    private String generateDataId(PathInfo pathInfo) {
        if (StringUtils.hasText(pathInfo.appName) && StringUtils.hasText(pathInfo.fileName)) {
            return pathInfo.appName + "-" + pathInfo.fileName;
        }
        return pathInfo.fileName != null ? pathInfo.fileName : "unknown";
    }

    /**
     * 生成分组名
     *
     * @param pathInfo 路径信息
     * @return 分组名
     */
    private String generateGroup(PathInfo pathInfo) {
        return StringUtils.hasText(pathInfo.appName) ? pathInfo.appName.toUpperCase() + "_GROUP" : "DEFAULT_GROUP";
    }

    /**
     * 生成描述信息
     *
     * @param pathInfo 路径信息
     * @return 描述信息
     */
    private String generateDescription(PathInfo pathInfo) {
        return String.format("自动同步的配置文件 - 应用: %s, 环境: %s, 配置: %s",
            pathInfo.appName != null ? pathInfo.appName : "unknown",
            pathInfo.environment != null ? pathInfo.environment : "unknown",
            pathInfo.configName != null ? pathInfo.configName : "unknown");
    }

    /**
     * 获取文件扩展名
     *
     * @param filename 文件名
     * @return 扩展名
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 ? filename.substring(lastDotIndex + 1) : "txt";
    }

    /**
     * 获取不带扩展名的文件名
     *
     * @param filename 文件名
     * @return 不带扩展名的文件名
     */
    private String getFileNameWithoutExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 ? filename.substring(0, lastDotIndex) : filename;
    }

    /**
     * 路径信息内部类
     */
    private static class PathInfo {
        String environment;
        String appName;
        String fileName;
        String configName;
    }
}
