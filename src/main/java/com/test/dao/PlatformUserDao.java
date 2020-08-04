package com.test.dao;

import com.test.domain.PlatformUser;
import com.test.domain.UserDto;

import java.util.List;

public interface PlatformUserDao {
    List<PlatformUser> queryUsers();

    int registerUser(PlatformUser user);

    PlatformUser checkUser(PlatformUser user);

    List<PlatformUser> queryUsersByDto(UserDto userDto);
}
