package io.github.fushuwei.springsecurity.model.converter;

import io.github.fushuwei.springsecurity.model.entity.UserDO;
import io.github.fushuwei.springsecurity.model.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    List<UserVO> doList2VoList(List<UserDO> userDoList);
}
