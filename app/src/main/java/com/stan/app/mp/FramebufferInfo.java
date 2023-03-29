package com.stan.app.mp;

import android.graphics.PixelFormat;

class FramebufferInfo {
    public final int width;
    public final int height;
    public final PixelFormat pixelFormat;

    public FramebufferInfo(int width, int height, PixelFormat pixelFormat) {
        this.width = width;
        this.height = height;
        this.pixelFormat = pixelFormat;
    }
}