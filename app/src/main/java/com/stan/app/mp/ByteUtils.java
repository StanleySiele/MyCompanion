package com.stan.app.mp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

class ByteUtils {
    public static void sendByteArray(SocketChannel socketChannel, ByteBuffer sendBuffer, byte[] messageBytes) throws IOException {
        sendBuffer.clear();
        sendBuffer.put(messageBytes);
        sendBuffer.flip();
        while (sendBuffer.hasRemaining()) {
            socketChannel.write(sendBuffer);
        }
    }

    public static void receiveByteArray(SocketChannel socketChannel, ByteBuffer receiveBuffer, byte[] messageBytes) throws IOException {
        receiveBuffer.clear();
        receiveBuffer.limit(messageBytes.length);
        while (receiveBuffer.hasRemaining()) {
            socketChannel.read(receiveBuffer);
        }
        receiveBuffer.flip();
        receiveBuffer.get(messageBytes);
    }
}