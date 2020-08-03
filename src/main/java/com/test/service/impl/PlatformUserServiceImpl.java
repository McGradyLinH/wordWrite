package com.test.service.impl;

import com.test.dao.PlatformUserDao;
import com.test.domain.PlatformUser;
import com.test.service.PlatformUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PlatformUserServiceImpl implements PlatformUserService {
    @Resource
    private PlatformUserDao platformUserDao;

    @Override
    public List<PlatformUser> queryUsers() {
        return platformUserDao.queryUsers();
    }

    @Override
    public int registerUser(PlatformUser user) {
        return platformUserDao.registerUser(user);
    }

    @Override
    public PlatformUser checkUser(PlatformUser user) {
        return platformUserDao.checkUser(user);
    }
}
