package com.stan.app.mp;

class Rectangle {
    public int x;
    public int y;
    public int width;
    public int height;
    public int encodingType;
    public byte[] encodingData;

    public Rectangle(int x, int y, int width, int height, int encodingType, byte[] encodingData) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.encodingType = encodingType;
        this.encodingData = encodingData;
    }
}