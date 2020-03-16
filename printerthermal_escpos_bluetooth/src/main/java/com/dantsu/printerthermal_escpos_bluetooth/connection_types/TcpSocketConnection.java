package com.dantsu.printerthermal_escpos_bluetooth.connection_types;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpSocketConnection implements ISocketConnection {
    private static final String TAG = "TcpSocketConnection";
    private Socket tcpSocket = null;
    private final String address;
    private final int port;

    public TcpSocketConnection(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public boolean isConnected() {
        boolean isConnectedBool = this.tcpSocket != null;

        Log.d(TAG, "#isConnected: "+ isConnectedBool);
        return isConnectedBool;
    }

    public boolean connect() {
        try {

            this.tcpSocket= new Socket();
            final InetAddress remoteInetAddress = InetAddress.getByName(this.address);


            this.tcpSocket.connect(new InetSocketAddress(remoteInetAddress, this.port));
            Log.d(TAG, "#connect details: "+ new InetSocketAddress(remoteInetAddress, this.port));
            Log.d(TAG, "#connect established: "+ this.tcpSocket.isConnected());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "#connect: did not establish socket connection");

        return false;
    }

    public boolean disconnect() {
        try {
            Log.d(TAG, "#disconnect: closing connection");
            this.tcpSocket.close();
            this.tcpSocket = null;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public OutputStream getOutputStream() {
        try {
            return tcpSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
