package com.test.controller;

import com.test.domain.Title;
import com.test.service.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lx
 * @version 1.0
 * @date 2020/7/26 9:52
 */
@RestController
public class TitleController {
    @Autowired
    private TitleService titleService;

    @GetMapping("/titles")
    public List<Title> titles(){
        return titleService.queryTitles();
    }
}
