package com.nativeappstudio.milewski_10529136.todolist;

import java.io.Serializable;

/**
 * Created by victo on 18-11-2015.
 */
public class Task implements Serializable {

    private String title;
    private String description;
    private String priority;

    public Task(String nTitle) {
        title = nTitle;
        description = "";
        priority = "";
    }

    public Task(String nTitle, String nDesc, String nPrior) {
        title = nTitle;
        description = nDesc;
        priority = nPrior;
    }

    public void setTitle(String nTitle) {
        title = nTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String nDesc) {
        description = nDesc;
    }

    public String getDescription() {
        return description;
    }

    public void setPriority(String nPrior) {
        priority = nPrior;
    }

    public String getPriority() {
        return priority;
    }
}
