package com.test.dao;

import com.test.domain.PlatformUser;
import com.test.domain.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlatformUserDao {
    List<PlatformUser> queryUsers();

    int registerUser(PlatformUser user);

    PlatformUser checkUser(PlatformUser user);

    List<PlatformUser> queryUsersByDto(UserDto userDto);
}
