package com.flipkart.flap.thunderingrhino.utils;

import java.io.Closeable;

import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import com.flipkart.flap.thunderingrhino.utils.RedisInputStream;
import com.flipkart.flap.thunderingrhino.utils.RedisOutputStream;

/**
 * Created by pavan.t on 06/05/15.
 */


public class RedisConnection implements Closeable  {

    public static final byte DOLLAR_BYTE = '$';
    public static final byte ASTERISK_BYTE = '*';
    private Socket socket;
    public  int connectionTimeout = 2000;
    private int soTimeout = 2000;
    private boolean broken = false;
    private String host = "localhost";
    private int port = 6379;
    private RedisOutputStream outputStream;
    private RedisInputStream inputStream;

    public RedisConnection() {
    }

    public RedisConnection(final String host) {
        this.host = host;
    }

    public RedisConnection(final String host, final int port) {
        this.host = host;
        this.port = port;
    }

    public Socket getSocket() {
        return socket;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public void setTimeoutInfinite() throws Exception {
        try {
            if (!isConnected()) {
                connect();
            }
            socket.setSoTimeout(0);
        } catch (SocketException ex) {
            broken = true;
            throw new Exception(ex);
        }
    }

    public void rollbackTimeout() throws Exception {
        try {
            socket.setSoTimeout(soTimeout);
        } catch (SocketException ex) {
            broken = true;
            throw new Exception(ex);
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public void connect() throws Exception {
        if (!isConnected()) {
            try {
                socket = new Socket();
                // ->@wjw_add
                socket.setReuseAddress(true);
                socket.setKeepAlive(true); // Will monitor the TCP connection is
                // valid
                socket.setTcpNoDelay(true); // Socket buffer Whetherclosed, to
                // ensure timely delivery of data
                socket.setSoLinger(true, 0); // Control calls close () method,
                // the underlying socket is closed
                // immediately
                // <-@wjw_add

                socket.connect(new InetSocketAddress(host, port), getConnectionTimeout());
                socket.setSoTimeout(soTimeout);
                outputStream = new RedisOutputStream(socket.getOutputStream());
                inputStream = new RedisInputStream(socket.getInputStream());
            } catch (IOException ex) {
                broken = true;
                throw new Exception(ex);
            }
        }
    }

    @Override
    public void close() {
        try {
            disconnect();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void disconnect() throws Exception {
        if (isConnected()) {
            try {
                outputStream.flush();
                socket.close();
            } catch (IOException ex) {
                broken = true;
                throw new Exception(ex);
            } finally {
                IOUtils.closeQuietly(socket);
            }
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isBound() && !socket.isClosed() && socket.isConnected()
                && !socket.isInputShutdown() && !socket.isOutputShutdown();
    }

}

class IOUtils {
    private IOUtils() {
    }

    public static void closeQuietly(Socket sock) {
        // It's same thing as Apache Commons - IOUtils.closeQuietly()
        if (sock != null) {
            try {
                sock.close();
            } catch (IOException e) {
                // ignored
            }
        }
    }
}
