package io.github.fushuwei.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
        return http.authorizeHttpRequests((requests) ->
                requests.requestMatchers("/", "/resource/roles").permitAll().anyRequest().authenticated())
            .formLogin(withDefaults())
            .httpBasic(AbstractHttpConfigurer::disable)  // 关闭弹窗
            .csrf(AbstractHttpConfigurer::disable)  // 禁用 CSRF
            .build();
    }

    /**
     * 基于内存的用户认证管理器
     */
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder) throws Exception {
        // 创建登录用户，这里的用户会覆盖 application.yml 中配置的用户和密码
        UserDetails user1 = User.withUsername("admin1").password("{noop}admin1").build();  // NoOpPasswordEncoder，密码不加密
        UserDetails user2 = User.withUsername("admin2").password(passwordEncoder.encode("admin2")).build();  // BCryptPasswordEncoder, BCrypt加密（推荐）

        // 将用户放入内存用户信息管理器中
        return new InMemoryUserDetailsManager(user1, user2);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 只能 BCrypt 登录
        // return new BCryptPasswordEncoder(12);

        // 即可以 NoOp 登录，也可以 BCrypt 登录
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
