package com.risenb.witness.beans;

import java.util.List;

public class TaskProblemBean {
    /**
     * testId : 1
     * title : 你多大了？
     * type : 1
     * problem : ["15岁","16岁","17岁"]
     */

    private String testId;
    private String title;
    private String type;
    private List<String> problem;

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getProblem() {
        return problem;
    }

    public void setProblem(List<String> problem) {
        this.problem = problem;
    }

    @Override
    public String toString() {
        return "TaskProblemBean{" +
                "testId='" + testId + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", problem=" + problem +
                '}';
    }
}
