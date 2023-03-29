package com.stan.app.mp;


import android.graphics.Bitmap;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.view.Surface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Vp9Encoder {
    private MediaCodec codec;

    private ByteBuffer[] inputBuffers;
    private ByteBuffer[] outputBuffers;
    private VncClient vncClient;
    private int frameWidth;
    private int frameHeight;

    public Vp9Encoder(VncClient client, int width, int height) {
        this.vncClient = client;
        initCodec();
        frameWidth = width;
        frameHeight = height;
        try {
            codec = MediaCodec.createEncoderByType("video/VP9");
            MediaFormat format = MediaFormat.createVideoFormat("video/VP9", frameWidth, frameHeight);
            format.setInteger(MediaFormat.KEY_BIT_RATE, 1000000);
            format.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);

            codec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            Surface inputSurface = codec.createInputSurface();
            codec.start();

            inputBuffers = codec.getInputBuffers();
            outputBuffers = codec.getOutputBuffers();
        } catch (IOException e) {
            // handle error initializing codec
            e.printStackTrace();
        }
    }

    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public void encodeFrame(Bitmap frame) {
        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();
        int frameSize = frameWidth * frameHeight * 4;

        byte[] frameData = convertBitmapToByteArray(frame);

        int inputBufferIndex = codec.dequeueInputBuffer(-1);
        if (inputBufferIndex >= 0) {
            ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
            inputBuffer.clear();
            inputBuffer.put(frameData);
            codec.queueInputBuffer(inputBufferIndex, 0, frameSize, 0, 0);
        }

        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        int outputBufferIndex = codec.dequeueOutputBuffer(bufferInfo, 0);
        while (outputBufferIndex >= 0) {
            ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];
            byte[] outputData = new byte[bufferInfo.size];
            outputBuffer.get(outputData);

            vncClient.sendFrame(outputData);

            codec.releaseOutputBuffer(outputBufferIndex, false);
            outputBufferIndex = codec.dequeueOutputBuffer(bufferInfo, 0);
        }
    }

    private void initCodec() {
        try {
            codec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_VP9);

            MediaFormat format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_VP9, frameWidth, frameHeight);
            format.setInteger(MediaFormat.KEY_BIT_RATE, 1000000);
            format.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
            codec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            Surface inputSurface = codec.createInputSurface();
            codec.start();
            inputBuffers = codec.getInputBuffers();
            outputBuffers = codec.getOutputBuffers();
        } catch (IOException e) {
            // handle error initializing codec
            e.printStackTrace();
        }
    }



}

