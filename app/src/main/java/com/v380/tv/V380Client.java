
package com.v380.tv;

import java.net.Socket;
import java.io.IOException;

public class V380Client {

    private Socket socket;

    public boolean connect(String host, int port) {
        try {
            socket = new Socket();
            socket.connect(
                new java.net.InetSocketAddress(host, port),
                10000
            );
            return true;
        } catch (IOException e) {
            close();
            return false;
        }
    }

    public boolean isConnected() {
        return socket != null
                && socket.isConnected()
                && !socket.isClosed();
    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (IOException ignored) {
        }
    }
}
