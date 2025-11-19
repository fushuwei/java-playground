package io.github.fushuwei.springsecurity.model.vo;

import lombok.Data;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String nickname;
    private String realname;
    private Integer age;
    private String email;
}
