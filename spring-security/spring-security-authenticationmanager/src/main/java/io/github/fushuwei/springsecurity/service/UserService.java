package io.github.fushuwei.springsecurity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.fushuwei.springsecurity.model.dto.UserDTO;
import io.github.fushuwei.springsecurity.model.entity.UserDO;

public interface UserService extends IService<UserDO> {

    String login(UserDTO userDto);
}
