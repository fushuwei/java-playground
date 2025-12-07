package io.github.fushuwei.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

/**
 * Spring Security 配置类
 * 配置应用程序的安全策略
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * 配置安全过滤器链
     *
     * @param http HttpSecurity 对象，用于配置安全策略
     * @return SecurityFilterChain 安全过滤器链
     * @throws Exception 配置过程中可能抛出的异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 配置请求授权规则
                .authorizeHttpRequests(authz -> authz
                        // 允许所有人访问 Redis 示例接口
                        .requestMatchers("/redis/**").permitAll()
                        // 其他所有请求都需要身份验证
                        .anyRequest().authenticated()
                )
                // 禁用 CSRF 保护（仅用于简化示例）
                .csrf(AbstractHttpConfigurer::disable)
                // 禁用框架选项（防止点击劫持）
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                // 配置表单登录
                .formLogin(form -> form
                        // 允许所有人访问登录页面
                        .permitAll()
                )
                // 配置 HTTP 基本认证
                .httpBasic(AbstractHttpConfigurer::disable);

        // 返回构建好的安全过滤器链
        return http.build();
    }

    /**
     * 配置密码编码器
     *
     * @return PasswordEncoder 密码编码器实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用 BCrypt 算法进行密码编码
        return new BCryptPasswordEncoder(12);
    }

    /**
     * 配置基于数据库的用户详情服务
     *
     * @param dataSource 数据源
     * @return UserDetailsService 用户详情服务实例
     */
    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        // 创建 JDBC 用户详情管理器
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        // 设置查询用户信息的 SQL 语句
        manager.setUsersByUsernameQuery(
                "SELECT username, password, 1 as enabled FROM user WHERE username = ?"
        );

        // 设置查询用户权限的 SQL 语句
        manager.setAuthoritiesByUsernameQuery(
                "SELECT u.username, 'ROLE_USER' as authority FROM user u WHERE u.username = ?"
        );

        // 记录用户详情服务初始化日志
        log.info("✅ 基于数据库的用户详情服务初始化成功");

        // 返回 JDBC 用户详情管理器
        return manager;
    }
}