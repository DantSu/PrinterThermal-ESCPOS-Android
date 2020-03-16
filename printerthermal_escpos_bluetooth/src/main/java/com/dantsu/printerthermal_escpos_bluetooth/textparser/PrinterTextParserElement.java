package com.dantsu.printerthermal_escpos_bluetooth.textparser;

import com.dantsu.printerthermal_escpos_bluetooth.PosPrinter;

public interface PrinterTextParserElement {
    int length();
    PrinterTextParserElement print(PosPrinter posPrinter);
}
