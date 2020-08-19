package com.test.service.impl;

import com.test.dao.CommentsDao;
import com.test.domain.Comment;
import com.test.service.CommentsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lx
 * @version 1.0
 * @date 2020/8/11 22:19
 */
@Service
public class CommentsServiceImpl implements CommentsService {
    @Resource
    private CommentsDao commentsDao;

    @Override
    public List<Comment> queryComments(String essayCode, Integer index) {
        return commentsDao.queryComments(essayCode, index);
    }

    @Override
    public int insertComment(Comment comment) {
        return commentsDao.insertComment(comment);
    }

    @Override
    public int deleteComment(Comment comment) {
        return commentsDao.deleteComment(comment);
    }
}
