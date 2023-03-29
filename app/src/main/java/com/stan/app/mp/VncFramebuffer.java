package com.stan.app.mp;

import android.graphics.Bitmap;

public class VncFramebuffer extends Thread {
    private boolean running;
    private Bitmap frame;
    private Vp9Encoder vp9Encoder;

    public VncFramebuffer(int frameWidth, int frameHeight){
        vp9Encoder = new Vp9Encoder(new VncClient(), frameWidth, frameHeight);
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            // capture the screen here
            // ...

            vp9Encoder.encodeFrame(frame);
        }
    }

    public void stopThread() {
        running = false;
    }

// other methods for capturing the screen, handling errors, etc.
}