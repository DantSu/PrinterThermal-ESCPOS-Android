package lib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.dantsu.printerthermal_escpos_bluetooth.connection_types.BluetoothSocketConnection;

import java.util.Set;

import com.dantsu.printerthermal_escpos_bluetooth.connection_types.BluetoothSocketConnection;

public class BluetoothDevices {
    protected BluetoothAdapter bluetoothAdapter;

    /**
     * Create a new instance of BluetoothDevices
     */
    public BluetoothDevices() {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * Get a list of bluetooth devices available.
     *
     * @return Return an array of BluetoothSocketConnection instance
     */
    public BluetoothSocketConnection[] getList() {
        if (this.bluetoothAdapter == null) {
            return null;
        }

        if (!this.bluetoothAdapter.isEnabled()) {
            return null;
        }

        Set<BluetoothDevice> bluetoothDevicesList = this.bluetoothAdapter.getBondedDevices();
        BluetoothSocketConnection[] bluetoothDevices = new BluetoothSocketConnection[bluetoothDevicesList.size()];

        if (bluetoothDevicesList.size() > 0) {
            int i = 0;
            for (BluetoothDevice device : bluetoothDevicesList) {
                bluetoothDevices[i++] = new BluetoothSocketConnection(device);
            }
        }

        return bluetoothDevices;
    }
}
