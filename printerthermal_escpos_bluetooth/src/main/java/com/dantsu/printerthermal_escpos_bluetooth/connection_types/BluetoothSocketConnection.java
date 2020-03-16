package com.dantsu.printerthermal_escpos_bluetooth.connection_types;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.OutputStream;

public class BluetoothSocketConnection implements ISocketConnection {

    protected BluetoothDevice device;
    protected BluetoothSocket bluetoothSocket = null;

    /**
     * Create new instance of BluetoothSocketConnection.
     *
     * @param device an instance of android.bluetooth.BluetoothDevice
     */
    public BluetoothSocketConnection(BluetoothDevice device) {
        this.device = device;
    }

    /**
     * Get the instance android.bluetooth.BluetoothDevice connected.
     *
     * @return an instance of android.bluetooth.BluetoothDevice
     */
    public BluetoothDevice getDevice() {
        return this.device;
    }

    /**
     * Check if the bluetooth device is connected by socket.
     *
     * @return true if is connected
     */
    public boolean isConnected() {
        return (this.bluetoothSocket != null);
    }

    /**
     * Start socket connection and open stream with the bluetooth device.
     *
     * @return return true if success
     */
    public boolean connect() {
        try {
            this.bluetoothSocket = this.device.createRfcommSocketToServiceRecord(this.device.getUuids()[0].getUuid());
            this.bluetoothSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                this.bluetoothSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            this.bluetoothSocket = null;
        }
        return false;
    }

    /**
     * Close the socket connection and stream with the bluetooth device.
     *
     * @return return true if success
     */
    public boolean disconnect() {

        try {
            if (this.isConnected()) {
                this.bluetoothSocket.close();
                this.bluetoothSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Obtain the output stream to send data to
     */

    public OutputStream getOutputStream() {
        try {
            return this.bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
