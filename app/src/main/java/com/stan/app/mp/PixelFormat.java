package com.stan.app.mp;

class PixelFormat {
    public final int bitsPerPixel;
    public final int depth;
    public final boolean bigEndian;
    public final boolean trueColor;
    public final int redMax;
    public final int greenMax;
    public final int blueMax;
    public final int redShift;
    public final int greenShift;
    public final int blueShift;

    public PixelFormat(int bitsPerPixel, int depth, boolean bigEndian, boolean trueColor, int redMax, int greenMax, int blueMax, int redShift, int greenShift, int blueShift) {
        this.bitsPerPixel = bitsPerPixel;
        this.depth = depth;
        this.bigEndian = bigEndian;
        this.trueColor = trueColor;
        this.redMax = redMax;
        this.greenMax = greenMax;
        this.blueMax = blueMax;
        this.redShift = redShift;
        this.greenShift = greenShift;
        this.blueShift = blueShift;
    }
}
