# 安装

## Docker Compose

```shell
git clone https://github.com/nacos-group/nacos-docker.git
cd nacos-docker
```

修改以下三个属性：
NACOS_AUTH_IDENTITY_KEY
NACOS_AUTH_IDENTITY_VALUE
NACOS_AUTH_TOKEN

1、NACOS_AUTH_IDENTITY_KEY： 建议使用X-Nacos-Server
2、NACOS_AUTH_IDENTITY_VALUE： 使用 `openssl rand -base64 48` 或 `head -c 32 /dev/urandom | base64` 生成
3、NACOS_AUTH_TOKEN： 使用 `openssl rand -base64 48` 或 `head -c 32 /dev/urandom | base64` 生成


# 创建mysql数据库
https://raw.githubusercontent.com/alibaba/nacos/refs/tags/3.0.3/distribution/conf/mysql-schema.sql


# 查看日志
docker logs -f $container_id