package com.test.domain;

/**
 * @author lx
 * @version 1.0
 * @date 2020/7/25 23:04
 */
public class Title {
    private Integer id;
    private Integer titleCode;
    private String titleName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public Integer getTitleCode() {
        return titleCode;
    }

    public void setTitleCode(Integer titleCode) {
        this.titleCode = titleCode;
    }
}
