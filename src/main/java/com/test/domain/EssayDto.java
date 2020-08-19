package com.test.domain;

public class EssayDto {
    private Integer status;
    private Integer stuId;
    private String essayName;
    //英语教师批改
    private PlatformUser enTeacher;
    //中文教师批改
    private PlatformUser CNTeacher;
    //第几篇文章1，2，3，4
    private Integer essayNumber;
    private Integer versions;

    public Integer getStuId() {
        return stuId;
    }

    public void setStuId(Integer stuId) {
        this.stuId = stuId;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEssayName() {
        return essayName;
    }

    public void setEssayName(String essayName) {
        this.essayName = essayName;
    }

    public PlatformUser getEnTeacher() {
        return enTeacher;
    }

    public void setEnTeacher(PlatformUser enTeacher) {
        this.enTeacher = enTeacher;
    }

    public PlatformUser getCNTeacher() {
        return CNTeacher;
    }

    public void setCNTeacher(PlatformUser CNTeacher) {
        this.CNTeacher = CNTeacher;
    }

    public Integer getEssayNumber() {
        return essayNumber;
    }

    public void setEssayNumber(Integer essayNumber) {
        this.essayNumber = essayNumber;
    }

    public Integer getVersions() {
        return versions;
    }

    public void setVersions(Integer versions) {
        this.versions = versions;
    }
}
