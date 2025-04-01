package com.diegorezm.todolist;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Todo implements Serializable {
    private String title;
    private String createdAt;
    private boolean isCompleted;

    public Todo(String title, String createdAt, boolean isCompleted) {
        this.title = title;
        this.createdAt = createdAt;
        this.isCompleted = isCompleted;
    }

    public boolean isExpired() {
        var currentDate = new Date();
        var dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            var todoDate = dateFormat.parse(createdAt);
            if (todoDate == null) return false;
            return todoDate.before(currentDate);
        } catch (Exception e) {
            return false;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
