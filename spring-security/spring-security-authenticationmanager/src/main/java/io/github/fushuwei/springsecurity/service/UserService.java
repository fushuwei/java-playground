package io.github.fushuwei.springsecurity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.fushuwei.springsecurity.model.dto.UserDTO;
import io.github.fushuwei.springsecurity.model.entity.UserDO;
import io.github.fushuwei.springsecurity.model.vo.UserVO;

import java.util.List;

public interface UserService extends IService<UserDO> {

    void login(UserDTO userDto);

    List<UserVO> queryList(UserDTO userDto);
}
