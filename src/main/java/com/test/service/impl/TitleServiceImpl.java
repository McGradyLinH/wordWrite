package com.test.service.impl;

import com.test.dao.TitleDao;
import com.test.domain.Title;
import com.test.service.TitleService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lx
 * @version 1.0
 * @date 2020/7/26 9:51
 */
@Service
public class TitleServiceImpl implements TitleService {
    @Resource
    private TitleDao titleDao;


    @Override
//    @Cacheable("titles")
    public List<Title> queryTitles() {
        return titleDao.queryTitles();
    }
}
