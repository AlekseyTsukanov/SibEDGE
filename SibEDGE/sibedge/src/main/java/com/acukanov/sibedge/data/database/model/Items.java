package com.acukanov.sibedge.data.database.model;


public class Items {
    public long id;
    public String text;
    public int check;

    public Items() {}

    public Items(int id, String text, int check) {
        this.id = id;
        this.text = text;
        this.check = check;
    }

    // region getters and setters
    // Uses to collaps getters and setters block
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }
    // endregion


    @Override
    public String toString() {
        return "Items{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", check=" + check +
                '}';
    }
}
