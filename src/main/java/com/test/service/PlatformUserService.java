package com.test.service;

import com.test.domain.PlatformUser;

import java.util.List;

public interface PlatformUserService {
    List<PlatformUser> queryUsers();

    int registerUser(PlatformUser user);

    PlatformUser checkUser(PlatformUser user);
}
