package com.dantsu.printerthermal_escpos_bluetooth.connection_types;

import java.io.OutputStream;

public interface ISocketConnection {
    boolean isConnected();
    boolean connect();
    boolean disconnect();
    OutputStream getOutputStream();
}
