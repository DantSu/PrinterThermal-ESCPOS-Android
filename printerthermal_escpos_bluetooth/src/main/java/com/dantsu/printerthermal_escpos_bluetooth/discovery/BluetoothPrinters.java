package com.dantsu.printerthermal_escpos_bluetooth.discovery;

import android.bluetooth.BluetoothClass;

import apos.net.printer.bluetooth.*;
import com.dantsu.printerthermal_escpos_bluetooth.connection_types.BluetoothSocketConnection;


public class BluetoothPrinters extends BluetoothDevices {

    /**
     * Easy way to get the first bluetooth printer paired / connected.
     *
     * @return a BluetoothSocketConnection instance
     */
    public static BluetoothSocketConnection selectFirstPairedBluetoothPrinter() {
        BluetoothPrinters printers = new BluetoothPrinters();
        BluetoothSocketConnection[] bluetoothPrinters = printers.getList();

        if (bluetoothPrinters != null && bluetoothPrinters.length > 0) {
            for (BluetoothSocketConnection printer : bluetoothPrinters) {
                if (printer.connect()) {
                    return printer;
                }
            }
        }
        return null;
    }


    /**
     * Get a list of bluetooth printers.
     *
     * @return an array of BluetoothSocketConnection
     */
    public BluetoothSocketConnection[] getList() {
        BluetoothSocketConnection[] bluetoothDevicesList = super.getList();

        if(bluetoothDevicesList == null) {
            return null;
        }

        int i = 0, j = 0;
        BluetoothSocketConnection[] bluetoothPrintersTmp = new BluetoothSocketConnection[bluetoothDevicesList.length];
        for (BluetoothSocketConnection device : bluetoothDevicesList) {
            if (device.getDevice().getBluetoothClass().getMajorDeviceClass() == BluetoothClass.Device.Major.IMAGING && device.getDevice().getBluetoothClass().getDeviceClass() == 1664) {
                bluetoothPrintersTmp[i++] = new BluetoothSocketConnection(device.getDevice());
            }
        }
        BluetoothSocketConnection[] bluetoothPrinters = new BluetoothSocketConnection[i];
        for (BluetoothSocketConnection device : bluetoothPrintersTmp) {
            if (device != null) {
                bluetoothPrinters[j++] = device;
            }
        }
        return bluetoothPrinters;
    }

}
