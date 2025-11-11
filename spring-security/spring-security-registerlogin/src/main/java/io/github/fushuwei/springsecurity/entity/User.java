package io.github.fushuwei.springsecurity.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {
    @TableId
    private Long id;
    private String username;
    private String nickname;
    private String realname;
    private String password;
    private Integer age;
    private String email;
}
