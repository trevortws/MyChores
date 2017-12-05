package com.example.mychores;

import java.util.List;

/**
 * Created by user on 19/11/2017.
 */

public class Task {
    private String db_ID;
    private String taskName;
    private String taskDescribtion;
    private int taskIcon;
    private String avgTime;
    private String creator;
    private String due_date;
    private String status;
    private String assigned_person;
    private List<Inventory_Item> items_needed;


    public Task() {

    }


    public Task(String id, String taskName, String taskDescribtion, int taskIcon, String avgTime, String creator, String due_date, String status, String assigned_person, List<Inventory_Item> items_needed) {
        this.taskName = taskName;
        this.taskDescribtion = taskDescribtion;
        this.taskIcon = taskIcon;
        this.avgTime = avgTime;
        this.creator = creator;
        this.due_date = due_date;
        this.status = status;
        this.assigned_person = assigned_person;
        this.db_ID = id;
        this.items_needed = items_needed;

    }

    public String getDb_ID() {
        return db_ID;
    }

    public void setDb_ID(String db_ID) {
        this.db_ID = db_ID;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getStatus() {
        return status;
    }

    public String getTaskDescribtion() {
        return taskDescribtion;
    }

    public int getTaskIcon() {
        return taskIcon;
    }

    public String getCreator() {
        return creator;
    }

    public String getDue_date() {
        return due_date;
    }

    public String getAvgTime() {
        return avgTime;
    }

    public String getAssigned_person() {
        return assigned_person;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDescribtion(String taskDescribtion) {
        this.taskDescribtion = taskDescribtion;
    }

    public void setTaskIcon(int taskIcon) {
        this.taskIcon = taskIcon;
    }

    public void setAvgTime(String avgTime) {
        this.avgTime = avgTime;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Inventory_Item> getItems_needed() {
        return items_needed;
    }

    public void setItems_needed(List<Inventory_Item> items_needed) {
        this.items_needed = items_needed;
    }

    public void setAssigned_person(String assigned_person) {
        this.assigned_person = assigned_person;
    }
}
