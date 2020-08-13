package com.test.service;

import com.test.domain.PlatformUser;
import com.test.domain.UserDto;

import java.util.List;

public interface PlatformUserService {
    List<PlatformUser> queryUsers();
    
    PlatformUser queryUserById(Integer id);

    int registerUser(PlatformUser user);

    PlatformUser checkUser(PlatformUser user);

    List<PlatformUser> queryUsersByDto(UserDto userDto);
}
