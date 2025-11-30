package io.github.fushuwei.springsecurity.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("user")
public class UserDO {
    @TableId
    private Long id;
    private String username;
    private String nickname;
    private String realname;
    private String password;
    private Integer age;
    private String email;

    /**
     * 序列化测试使用
     */
    private Date createTime;
    private LocalDateTime updateTime;
    private Instant deleteTime;
}
