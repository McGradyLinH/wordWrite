package com.test.dao;

import com.test.domain.Comment;

import java.util.List;

/**
 * @author lx
 * @version 1.0
 * @date 2020/8/11 22:18
 */
public interface CommentsDao {
    List<Comment> queryComments(String essayCode);

    int insertComment(Comment comment);
}
