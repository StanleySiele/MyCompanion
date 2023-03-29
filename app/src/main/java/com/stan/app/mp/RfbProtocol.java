package com.stan.app.mp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class RfbProtocol {

    private static final int CLIENT_INIT = 0;
    private static final int CLIENT_FRAME_BUFFER_UPDATE_REQUEST = 3;
    private static final int CLIENT_KEY_EVENT = 4;
    private static final int CLIENT_POINTER_EVENT = 5;

    private static final int SERVER_FRAMEBUFFER_UPDATE = 0;
    private static final int SERVER_SET_COLOUR_MAP_ENTRIES = 1;
    private static final int SERVER_BELL = 2;
    private static final int SERVER_CUT_TEXT = 3;

    private static final int PIXEL_FORMAT_DEPTH_SHIFT = 24;
    private static final int PIXEL_FORMAT_DEPTH_MASK = 0xff000000;
    private static final int PIXEL_FORMAT_TRUE_COLOUR = 1;
    private static final int PIXEL_FORMAT_RED_SHIFT = 16;
    private static final int PIXEL_FORMAT_GREEN_SHIFT = 8;
    private static final int PIXEL_FORMAT_BLUE_SHIFT = 0;

    private DataInputStream in;
    private DataOutputStream out;

    private int width;
    private int height;
    private int pixelFormat;
    private byte[] writeBuffer;
    private byte[] readBuffer;

    public RfbProtocol(InputStream inputStream, OutputStream outputStream) {
        this.in = new DataInputStream(inputStream);
        this.out = new DataOutputStream(outputStream);
        this.writeBuffer = new byte[24];
        this.readBuffer = new byte[24];
    }

    public int getPixelFormat() {
        return pixelFormat;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setPixelFormat() throws IOException {
        writeBuffer[0] = (byte) CLIENT_FRAME_BUFFER_UPDATE_REQUEST;
        writeBuffer[1] = 0; // incremental
        ByteBuffer.wrap(writeBuffer, 2, 2).putShort((short) 0); // x-position
        ByteBuffer.wrap(writeBuffer, 4, 2).putShort((short) 0); // y-position
        ByteBuffer.wrap(writeBuffer, 6, 2).putShort((short) width); // width
        ByteBuffer.wrap(writeBuffer, 8, 2).putShort((short) height); // height
        ByteBuffer.wrap(writeBuffer, 10, 4).putInt(pixelFormat); // pixel format
        out.write(writeBuffer, 0, 12);
        out.flush();
    }

    public void setVp9Encoding() throws IOException {
        writeBuffer[0] = (byte) CLIENT_FRAME_BUFFER_UPDATE_REQUEST;
        writeBuffer[1] = 1; // incremental
        ByteBuffer.wrap(writeBuffer, 2, 2).putShort((short) 0); // x-position
        ByteBuffer.wrap(writeBuffer, 4, 2).putShort((short) 0); // y-position
        ByteBuffer.wrap(writeBuffer, 6, 2).putShort((short) width); // width
        ByteBuffer.wrap(writeBuffer, 8, 2).putShort((short) height); // height
        ByteBuffer.wrap(writeBuffer, 10, 4).putInt(pixelFormat); // pixel format
        ByteBuffer.wrap(writeBuffer, 14, 4).putInt(574); // encoding type
        ByteBuffer.wrap(writeBuffer, 18, 4).putInt(1); // encoding sub-type
        out.write(writeBuffer, 0, 22);
        out.flush();
    }

    public void sendFrameBufferUpdateRequest() throws IOException {
        writeBuffer[0] = (byte) CLIENT_FRAME_BUFFER_UPDATE_REQUEST;
        writeBuffer[1] = 0; // incremental
        ByteBuffer.wrap(writeBuffer, 2, 2).putShort((short) 0); // x-position
        ByteBuffer.wrap(writeBuffer, 4, 2).putShort((short) 0); // y-position
        ByteBuffer.wrap(writeBuffer, 6, 2).putShort((short) width); // width
        ByteBuffer.wrap(writeBuffer, 8, 2).putShort((short) height); // height
        out.write(writeBuffer, 0, 10);
        out.flush();
    }

    public void sendKeyEvent(int key, boolean down) throws IOException {
        writeBuffer[0] = (byte) CLIENT_KEY_EVENT;
        writeBuffer[1] = down ? (byte) 1 : (byte) 0;
        ByteBuffer.wrap(writeBuffer, 2, 2).putShort((short) 0);
        ByteBuffer.wrap(writeBuffer, 4, 4).putInt(key);
        out.write(writeBuffer, 0, 8);
        out.flush();
    }

    public void sendPointerEvent(int buttonMask, int x, int y) throws IOException {
        writeBuffer[0] = (byte) CLIENT_POINTER_EVENT;
        writeBuffer[1] = (byte) buttonMask;
        ByteBuffer.wrap(writeBuffer, 2, 2).putShort((short) x);
        ByteBuffer.wrap(writeBuffer, 4, 2).putShort((short) y);
        out.write(writeBuffer, 0, 6);
        out.flush();
    }

    private void readServerInit() throws IOException {
        in.readFully(readBuffer, 0, 12);
        String versionMsg = new String(readBuffer, 0, 12);
        if (!versionMsg.startsWith("RFB")) {
            throw new IOException("Invalid protocol version: " + versionMsg);
        }
        byte[] version = new byte[3];
        version[0] = (byte) Integer.parseInt(versionMsg.substring(3, 4));
        version[1] = (byte) Integer.parseInt(versionMsg.substring(4, 5));
        version[2] = (byte) Integer.parseInt(versionMsg.substring(5, 6));
        if (version[0] != 3 || version[1] != 8) {
            throw new IOException("Invalid protocol version: " + versionMsg);
        }
        width = in.readUnsignedShort();
        height = in.readUnsignedShort();
        pixelFormat = in.readInt();
        in.readFully(readBuffer, 0, 16); // Name Length (ignored) + Name String (ignored)
    }

    public void sendClientInit() throws IOException {
        writeBuffer[0] = (byte) CLIENT_INIT;
        out.write(writeBuffer, 0, 1);
        out.flush();
    }

}

