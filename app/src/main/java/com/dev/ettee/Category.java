package com.dev.ettee;

public class Category {
    int id;
    String name;
    String hexaColor = null;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return hexaColor;
    }

    public void setColor(String color) {
        this.hexaColor = color;
    }
}
