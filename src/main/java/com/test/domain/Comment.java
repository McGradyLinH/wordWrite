package com.test.domain;

/**
 * @author lx
 * @version 1.0
 * @date 2020/8/11 22:15
 */
public class Comment {
    private int id;
    private String essayCode;
    private Integer spanId;
    private String comment;
    private Integer essayNumber;

    public Comment() {
    }

    public Comment(Integer spanId, Integer essayNumber) {
        this.spanId = spanId;
        this.essayNumber = essayNumber;
    }

    public Comment(Integer spanId, String comment, Integer essayNumber) {
        this.spanId = spanId;
        this.comment = comment;
        this.essayNumber = essayNumber;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", essayCode='" + essayCode + '\'' +
                ", spanId=" + spanId +
                ", comment='" + comment + '\'' +
                ", essayNumber=" + essayNumber +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEssayCode() {
        return essayCode;
    }

    public void setEssayCode(String essayCode) {
        this.essayCode = essayCode;
    }

    public Integer getSpanId() {
        return spanId;
    }

    public void setSpanId(Integer spanId) {
        this.spanId = spanId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getEssayNumber() {
        return essayNumber;
    }

    public void setEssayNumber(Integer essayNumber) {
        this.essayNumber = essayNumber;
    }
}
