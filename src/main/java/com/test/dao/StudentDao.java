package com.test.dao;

import com.test.domain.Essay;
import com.test.domain.PlatformUser;

import java.util.List;

/**
 * @author lx
 * @version 1.0
 * @date 2020/7/24 22:35
 */
public interface StudentDao {
    PlatformUser queryByPhone(String phone);

    List<Essay> queryEssayList(Integer status);

    /**
     * 添加文章
     * @param essay
     * @return
     */
    int insertEssay(Essay essay);

    /**
     * 减少学生作文数
     * @param student
     * @return
     */
    int decrementSurplus(PlatformUser student);

    /**
     * 增加学生作文数
     * @param student
     * @return
     */
    int incrementSurplus(PlatformUser student);

    /**
     * 修改文章的状态
     * @param essay
     * @return
     */
    int updateEssay(Essay essay);
}
