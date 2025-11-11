package io.github.fushuwei.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

    /**
     * 自定义 SecurityFilterChain
     * <p>
     * 1、如果不自定义，则默认走 SpringBootWebSecurityConfiguration 类中定义的 SecurityFilterChain。
     * 2、官方默认 SecurityFilterChainConfiguration 配置类中的 @ConditionalOnDefaultWebSecurity 注解决定是否加载默认的 SecurityFilterChain Bean。
     * 3、@ConditionalOnDefaultWebSecurity 注解的实现类是 DefaultWebSecurityCondition 类。
     * 4、DefaultWebSecurityCondition 类继承了 AllNestedConditions 类。
     * 5、AllNestedConditions 类表示：所有内部嵌套条件都必须满足，才算匹配。
     * 6、DefaultWebSecurityCondition 类包含两个嵌套条件：
     * - @ConditionalOnClass({ SecurityFilterChain.class, HttpSecurity.class })   → 确保 Spring Security 在 classpath
     * - @ConditionalOnMissingBean({ SecurityFilterChain.class })                 → 确保用户没有自定义 SecurityFilterChain Bean
     * 7、所以，满足以上所有条件，才会初始化默认的 SecurityFilterChain Bean。
     * 8、当前 SecurityFilterChain 所配置的所有过滤器，都存储在对应的 DefaultSecurityFilterChain 实例的 filters 字段中。
     * 9、可以自定义多个 SecurityFilterChain Bean，每个 SecurityFilterChain 配置的过滤器都存储在独立的 DefaultSecurityFilterChain 实例中，由 FilterChainProxy 按顺序路由执行，互不干扰。
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated())
            .formLogin(withDefaults())
            .httpBasic(AbstractHttpConfigurer::disable)  // 关闭弹窗
            .csrf(AbstractHttpConfigurer::disable)  // 禁用 CSRF
            .build();
    }
}
