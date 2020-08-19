package com.test.service;

import com.test.domain.Comment;

import java.util.List;

/**
 * @author lx
 * @version 1.0
 * @date 2020/8/11 22:19
 */
public interface CommentsService {
    List<Comment> queryComments(String essayCode, Integer index);

    int insertComment(Comment comment);

    int deleteComment(Comment comment);
}
