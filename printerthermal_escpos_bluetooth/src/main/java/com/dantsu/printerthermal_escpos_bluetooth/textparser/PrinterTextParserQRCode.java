package com.dantsu.printerthermal_escpos_bluetooth.textparser;

import java.util.Hashtable;

import com.dantsu.printerthermal_escpos_bluetooth.PosPrinter;
import com.dantsu.printerthermal_escpos_bluetooth.PrinterCommands;

public class PrinterTextParserQRCode extends PrinterTextParserImg {

    private static byte[] initConstructor(PrinterTextParserColumn printerTextParserColumn, Hashtable<String, String> qrCodeAttributes, String data) {
        PosPrinter posPrinter = printerTextParserColumn.getLine().getTextParser().getPosPrinter();
        data = data.trim();

        int size = posPrinter.mmToPx(20f);
        try {
            if (qrCodeAttributes.containsKey(PrinterTextParser.ATTR_QRCODE_SIZE)) {
                size = posPrinter.mmToPx(Float.parseFloat(qrCodeAttributes.get(PrinterTextParser.ATTR_QRCODE_SIZE)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return PrinterCommands.QRCodeDataToBytes(data, size);
    }

    public PrinterTextParserQRCode(PrinterTextParserColumn printerTextParserColumn, String textAlign, Hashtable<String, String> qrCodeAttributes, String data) {
        super(
                printerTextParserColumn,
                textAlign,
                PrinterTextParserQRCode.initConstructor(printerTextParserColumn, qrCodeAttributes, data)
        );
    }
}
