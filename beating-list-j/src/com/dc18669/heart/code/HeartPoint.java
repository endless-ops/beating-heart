package com.dc18669.heart.code;

public class HeartPoint {
    private int x;
    private int y;
    private int size;

    public HeartPoint(int x, int y, int size) {
        this.size = size;
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
