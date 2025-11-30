-- 创建数据库
CREATE DATABASE IF NOT EXISTS `spring_security` DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_0900_ai_ci;

-- 选择数据库
use spring_security;

-- 用户表
CREATE TABLE IF NOT EXISTS `user`
(
    `id`          VARCHAR(50)  NOT NULL COMMENT '主键ID',
    `username`    VARCHAR(255) NULL DEFAULT NULL COMMENT '用户名',
    `nickname`    VARCHAR(255) NULL DEFAULT NULL COMMENT '昵称',
    `realname`    VARCHAR(255) NULL DEFAULT NULL COMMENT '真实姓名',
    `password`    VARCHAR(255) NULL DEFAULT NULL COMMENT '登录密码',
    `age`         INT          NULL DEFAULT NULL COMMENT '年龄',
    `email`       VARCHAR(255) NULL DEFAULT NULL COMMENT '邮箱',
    `create_time` DATETIME     NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME     NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
);
