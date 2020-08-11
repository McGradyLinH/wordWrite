package com.test.domain;

/**
 * @author LX
 */
public class Essay {
    private Integer id;
    //文件名称
    private String name;
    //状态，修改到哪一步了1-英文教师批改，2-中文教师批改，3-已完成
    private Integer status;
    //是属于哪一个学生的
    private PlatformUser student;
    //英语教师批改
    private PlatformUser enTeacher;
    //中文教师批改
    private PlatformUser CNTeacher;
    //文章的题目
    private String titleName;
    //第几次修改1-表示第一次，2-表示第二次，3-表示不再修改
    private Integer versions;
    //文章内容
    private String essayContent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public PlatformUser getStudent() {
        return student;
    }

    public void setStudent(PlatformUser student) {
        this.student = student;
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

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public Integer getVersions() {
        return versions;
    }

    public void setVersions(Integer versions) {
        this.versions = versions;
    }

    public String getEssayContent() {
        return essayContent;
    }

    public void setEssayContent(String essayContent) {
        this.essayContent = essayContent;
    }
}
