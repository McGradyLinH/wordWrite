package com.test.domain;

import java.io.Serializable;

/**
 * @author lx
 * @version 1.0
 * @date 2020/7/25 23:04
 */
public class Title implements Serializable {
    private static final long serialVersionUID = 7450050213833451533L;
    private Integer id;
    private Integer titleCode;
    private String titleName;

    public Title(){

    }

    public Title(String titleName,Integer titleCode){
        this.titleCode = titleCode;
        this.titleName = titleName;
    }

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
