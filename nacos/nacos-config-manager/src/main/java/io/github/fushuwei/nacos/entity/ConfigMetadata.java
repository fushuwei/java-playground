package io.github.fushuwei.nacos.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 配置元数据实体类
 *
 * @author example
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigMetadata {

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 分组
     */
    private String group;

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 配置内容
     */
    private String content;

    /**
     * 配置类型(yaml, properties, json等)
     */
    private String type;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 环境
     */
    private String environment;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件最后修改时间
     */
    private Long lastModified;

    /**
     * 内容MD5
     */
    private String contentMd5;
}
