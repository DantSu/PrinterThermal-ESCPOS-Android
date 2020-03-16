package com.dantsu.printerthermal_escpos_bluetooth;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.EnumMap;

public class PrinterCommands {
    public static final int TIME_BETWEEN_TWO_PRINT = 150;

    public static final byte[] WESTERN_EUROPE_ENCODING = new byte[]{0x1B, 0x74, 0x06};

    public static final byte LF = 0x0A;

    public static final byte[] TEXT_ALIGN_LEFT = new byte[]{0x1B, 0x61, 0x00};
    public static final byte[] TEXT_ALIGN_CENTER = new byte[]{0x1B, 0x61, 0x01};
    public static final byte[] TEXT_ALIGN_RIGHT = new byte[]{0x1B, 0x61, 0x02};

    public static final byte[] TEXT_WEIGHT_NORMAL = new byte[]{0x1B, 0x45, 0x00};
    public static final byte[] TEXT_WEIGHT_BOLD = new byte[]{0x1B, 0x45, 0x01};

    public static final byte[] TEXT_SIZE_NORMAL = new byte[]{0x1B, 0x21, 0x03};
    public static final byte[] TEXT_SIZE_MEDIUM = new byte[]{0x1B, 0x21, 0x08};
    public static final byte[] TEXT_SIZE_DOUBLE_HEIGHT = new byte[]{0x1B, 0x21, 0x10};
    public static final byte[] TEXT_SIZE_DOUBLE_WIDTH = new byte[]{0x1B, 0x21, 0x20};
    public static final byte[] TEXT_SIZE_BIG = new byte[]{0x1B, 0x21, 0x30};

    public static final byte[] TEXT_UNDERLINE_OFF = new byte[]{0x1B, 0x2D, 0x00};
    public static final byte[] TEXT_UNDERLINE_ON = new byte[]{0x1B, 0x2D, 0x01};
    public static final byte[] TEXT_UNDERLINE_LARGE = new byte[]{0x1B, 0x2D, 0x02};

    public static final byte[] TEXT_DOUBLE_STRIKE_OFF = new byte[]{0x1B, 0x47, 0x00};
    public static final byte[] TEXT_DOUBLE_STRIKE_ON = new byte[]{0x1B, 0x47, 0x01};


    public static final int BARCODE_UPCA = 0;
    public static final int BARCODE_UPCE = 1;
    public static final int BARCODE_EAN13 = 2;
    public static final int BARCODE_EAN8 = 3;
    public static final int BARCODE_ITF = 5;


    public static final int QRCODE_1 = 49;
    public static final int QRCODE_2 = 50;


    private static byte[] initImageCommand(int bytesByLine, int bitmapHeight) {
        byte[] imageBytes = new byte[8 + bytesByLine * bitmapHeight];
        System.arraycopy(new byte[]{0x1D, 0x76, 0x30, 0x00, (byte) bytesByLine, 0x00, (byte) bitmapHeight, 0x00}, 0, imageBytes, 0, 8);
        return imageBytes;
    }

    /**
     * Convert Bitmap instance to a byte array compatible with ESC/POS printer.
     *
     * @param bitmap Bitmap to be convert
     * @return Bytes contain the image in ESC/POS command
     */
    public static byte[] bitmapToBytes(Bitmap bitmap) {
        int
                bitmapWidth = bitmap.getWidth(),
                bitmapHeight = bitmap.getHeight(),
                bytesByLine = (int) Math.ceil(((float) bitmapWidth) / 8f);

        byte[] imageBytes = PrinterCommands.initImageCommand(bytesByLine, bitmapHeight);

        int i = 8;
        for (int posY = 0; posY < bitmapHeight; posY++) {
            for (int j = 0; j < bitmapWidth; j += 8) {
                StringBuilder stringBinary = new StringBuilder();
                for (int k = 0; k < 8; k++) {
                    int posX = j + k;
                    if (posX < bitmapWidth) {
                        int color = bitmap.getPixel(posX, posY),
                                r = (color >> 16) & 0xff,
                                g = (color >> 8) & 0xff,
                                b = color & 0xff;

                        if (r > 160 && g > 160 && b > 160) {
                            stringBinary.append("0");
                        } else {
                            stringBinary.append("1");
                        }
                    } else {
                        stringBinary.append("0");
                    }
                }
                imageBytes[i++] = (byte) Integer.parseInt(stringBinary.toString(), 2);
            }
        }

        return imageBytes;
    }
    /**
     * Convert Bitmap instance to a byte array compatible with ESC/POS printer.
     *
     * @param data String data to convert in QR Code
     * @return Bytes contain the image in ESC/POS command
     */
    public static byte[] QRCodeDataToBytes(String data, int size) {

        BitMatrix bitMatrix = null;

        try {
            EnumMap<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, -1);

            QRCodeWriter writer = new QRCodeWriter();
            bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, size, size, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        if(bitMatrix == null) {
            return new byte[0];
        }

        int
                width = bitMatrix.getWidth(),
                height = bitMatrix.getHeight(),
                bytesByLine = (int) Math.ceil(((float) width) / 8f),
                i = 8;

        byte[] imageBytes = PrinterCommands.initImageCommand(bytesByLine, height);

        for (int y = 0; y < height; y++) {
            for (int refX = 0; refX < width; refX += 8) {
                StringBuilder stringBinary = new StringBuilder();
                for (int k = 0; k < 8; k++) {
                    int x = refX + k;
                    if (x < width) {
                        if (bitMatrix.get(x, y)) {
                            stringBinary.append("1");
                        } else {
                            stringBinary.append("0");
                        }
                    } else {
                        stringBinary.append("0");
                    }
                }
                imageBytes[i++] = (byte) Integer.parseInt(stringBinary.toString(), 2);
            }
        }

        return imageBytes;
    }
}
