package com.test.service;

import com.test.domain.Comment;
import com.test.domain.Essay;
import com.test.domain.EssayDto;
import com.test.domain.PlatformUser;

import java.util.List;

/**
 * @author lx
 * @version 1.0
 * @date 2020/7/24 22:52
 */
public interface StudentService {
    PlatformUser queryByPhone(String phone);

    List<Essay> queryEssayList(EssayDto essayDto);

    Essay insertEssay(Essay essay);

    /**
     * 减少学生作文数
     *
     * @param student
     * @return
     */
    int decrementSurplus(PlatformUser student);

    /**
     * 增加学生作文数
     *
     * @param student
     * @return
     */
    int incrementSurplus(PlatformUser student);

    /**
     * 修改文章的状态
     *
     * @param essay
     * @return
     */
    int updateEssay(Essay essay);

    /**
     * 批改文章
     * @param essay
     * @param addCommentList
     * @param delCommentList
     * @return
     */
    int correctEssay(Essay essay, List<Comment> addCommentList, List<Comment> delCommentList);
}
