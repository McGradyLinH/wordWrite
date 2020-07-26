package com.test.dao;

import com.test.domain.Title;

import java.util.List;

/**
 * @author lx
 * @version 1.0
 * @date 2020/7/26 9:50
 */
public interface TitleDao {
    List<Title> queryTitles();
}
