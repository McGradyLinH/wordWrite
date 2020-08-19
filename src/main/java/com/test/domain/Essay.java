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
    //文章号数
    private Integer essayNumber;
    //1-小作文，2-大作文
    private Integer essayType;
    //题目图片的路径
    private String titlePath;

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

    public Integer getEssayNumber() {
        return essayNumber;
    }

    public void setEssayNumber(Integer essayNumber) {
        this.essayNumber = essayNumber;
    }

    @Override
    public String toString() {
        return "Essay{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", student=" + student +
                ", enTeacher=" + enTeacher +
                ", CNTeacher=" + CNTeacher +
                ", titleName='" + titleName + '\'' +
                ", versions=" + versions +
                ", essayContent='" + essayContent + '\'' +
                ", essayNumber=" + essayNumber +
                '}';
    }

    public Integer getEssayType() {
        return essayType;
    }

    public void setEssayType(Integer essayType) {
        this.essayType = essayType;
    }

    public String getTitlePath() {
        return titlePath;
    }

    public void setTitlePath(String titlePath) {
        this.titlePath = titlePath;
    }
}
