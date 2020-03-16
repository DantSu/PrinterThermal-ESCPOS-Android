package com.dantsu.printerthermal_escpos_bluetooth.textparser;

import java.util.Arrays;

import com.dantsu.printerthermal_escpos_bluetooth.PosPrinter;
import com.dantsu.printerthermal_escpos_bluetooth.PrinterCommands;

public class PrinterTextParserString implements PrinterTextParserElement {
    private String text;
    private byte[] textSize;
    private byte[] textBold;
    private byte[] textUnderline;

    public PrinterTextParserString(String text, byte[] textSize, byte[] textBold, byte[] textUnderline) {
        this.text = text;
        this.textSize = textSize;
        this.textBold = textBold;
        this.textUnderline = textUnderline;
    }

    @Override
    public int length() {
        int coef = 1;

        if (Arrays.equals(this.textSize, PrinterCommands.TEXT_SIZE_DOUBLE_WIDTH) || Arrays.equals(this.textSize, PrinterCommands.TEXT_SIZE_BIG)) {
            coef = 2;
        }

        return this.text.length() * coef;
    }

    /**
     * Print text
     *
     * @param posPrinter Bluetooth printer socket connection
     * @return this Fluent method
     */
    @Override
    public PrinterTextParserString print(PosPrinter posPrinter) {
        posPrinter.printText(this.text, this.textSize, this.textBold, this.textUnderline);
        return this;
    }
}
