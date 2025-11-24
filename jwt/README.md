# Java JWT 演示项目：JJWT 与 Nimbus JOSE + JWT

这是一个完整的 Java JWT 演示项目，旨在帮助您学习和掌握 Java 中 JWT 的生成、验证和刷新（续期）机制。项目使用了两个最流行的
Java JWT 库：**JJWT** 和 **Nimbus JOSE + JWT**。

## 注意

### 1. 生产环境使用 RS256 或者 EC256，不建议使用 HS256

### 2. 生成环境不要使用`1234567890abcdefghijklmnopqrstuvwxyz0987654321`做秘钥，使用`openssl rand -base64 64`生成一个复杂的秘钥

## 生产项目最佳实践：一个完整示例

假设我们有一个微服务系统，包含一个 **service-1**、一个 **service-2** 和一个 **认证服务**。

**场景**：用户通过认证服务登录，获取一个 JWT，然后用它来访问订单服务。

### 1. 认证服务签发 JWT

当用户 `id: "1001"` 登录成功后，认证服务 (`service-auth`) 会生成如下的 JWT Payload：

```
{
    "iss": "io.github.fushuwei.jwt",               // 签发者
    "sub": "1001",                                 // 主题：用户的唯一ID
    "aud": ["service-1", "service-2"],             // 用户：此令牌只能被 service-1 和 service-2 两个服务使用
    "iat": 1678886400,                             // 签发时间：当前时间戳
    "exp": 1678890000,                             // 过期时间：1小时后
    "jti": "a1b2c3d4-e5f6-a7b8-c9d0-e1f2a3b4c5d6", // JWT ID：唯一ID，用于防重放
    "username": "admin",                           // 自定义声明：用户名
    "roles": ["USER", "ADMIN"]                     // 自定义声明：用户角色
}
```

### 2. 订单服务验证 JWT

当用户携带此 JWT 访问 service-1 时，service-1 服务会进行以下验证：

1. **验证签名**：使用 `iss` (`io.github.fushuwei.jwt`) 对应的密钥来验证签名是否有效。
2. **验证 `aud`**：检查 `aud` 数组中是否包含 `"service-1"`。如果不在，直接拒绝。
3. **验证 `exp`**：检查当前时间是否早于 `exp`。如果已过期，拒绝。
4. **验证 `jti`（可选）**：检查 `jti` 是否存在于“已使用令牌”的缓存（如 Redis）中。如果存在，说明是重放攻击，拒绝。
5. **提取信息**：所有验证通过后，从 `sub` 中获取用户ID (`1001`)，用于后续的业务逻辑处理。

通过这种方式，JWT 的各个声明协同工作，构建了一个安全、可靠且可扩展的认证授权体系。
