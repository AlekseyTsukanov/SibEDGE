package com.acukanov.sibedge.data.remote.model;


public class ServiceData {
    private String id;
    private String date;
    private String text;

    public ServiceData(String id, String date, String text) {
        this.id = id;
        this.date = date;
        this.text = text;
    }

    // region getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    // endregion


    @Override
    public String toString() {
        return "ServiceData{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
