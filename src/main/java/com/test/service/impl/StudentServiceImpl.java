package com.test.service.impl;

import com.test.dao.StudentDao;
import com.test.domain.Essay;
import com.test.domain.PlatformUser;
import com.test.service.StudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lx
 * @version 1.0
 * @date 2020/7/24 22:53
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Resource
    private StudentDao studentDao;

    @Override
    public PlatformUser queryByPhone(String phone) {
        return studentDao.queryByPhone(phone);
    }

    @Override
    public List<Essay> queryEssayList(Integer status) {
        return studentDao.queryEssayList(status);
    }

    @Override
    public Essay insertEssay(Essay essay) {
        int i = studentDao.insertEssay(essay);
        if (i > 0) {
            return essay;
        }
        return null;
    }

    @Override
    public int decrementSurplus(PlatformUser student) {
        return studentDao.decrementSurplus(student);
    }

    @Override
    public int incrementSurplus(PlatformUser student) {
        return studentDao.incrementSurplus(student);
    }

    @Override
    public int updateEssay(Essay essay) {
        return studentDao.updateEssay(essay);
    }
}
