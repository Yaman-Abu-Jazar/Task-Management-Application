package com.example.a1212199_1210352_courseproject;

public class Task {

    private String title;
    private String description;
    private String priority;
    private int completionStatus;
    private String dueDate;
    private String dueTime;
    private String reminderDate;
    private String reminderTime;
    private User user;


    public Task(){}

    public Task(String title, String description, String priority, int completionStatus, String dueDate, String dueTime, String reminderDate, String reminderTime, User user) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.completionStatus = completionStatus;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
        this.user = user;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(int completionStatus) {
        this.completionStatus = completionStatus;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(String reminderDate) {
        this.reminderDate = reminderDate;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public String toString() {
        return "Task{" +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority='" + priority + '\'' +
                ", completionStatus=" + completionStatus +
                ", dueData='" + dueDate + '\'' +
                ", dueTime='" + dueTime + '\'' +
                ", reminderDate='" + reminderDate + '\'' +
                ", reminderTime='" + reminderTime + '\'' +
                ", user=" + user +
                '}';
    }
}
