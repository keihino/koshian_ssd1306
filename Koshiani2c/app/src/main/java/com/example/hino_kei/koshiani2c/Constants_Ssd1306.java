package com.example.hino_kei.koshiani2c;

/**
 * Created by hino-kei on 2016/11/07.
 */

public final class Constants_Ssd1306 {
    private Constants_Ssd1306() {}

    public static final byte SSD1306_SETCONTRAST = (byte)0x81;
    public static final byte SSD1306_DISPLAYALLON_RESUME = (byte)0xA4;
    public static final byte SSD1306_DISPLAYALLON = (byte)0xA5;
    public static final byte SSD1306_NORMALDISPLAY =(byte)0xA6;
    public static final byte SSD1306_INVERTDISPLAY =(byte)0xA7;
    public static final byte SSD1306_DISPLAYOFF =(byte)0xAE;
    public static final byte SSD1306_DISPLAYON =(byte)0xAF;
    public static final byte SSD1306_SETDISPLAYOFFSET =(byte)0xD3;
    public static final byte SSD1306_SETCOMPINS =(byte)0xDA;
    public static final byte SSD1306_SETVCOMDETECT =(byte)0xDB;
    public static final byte SSD1306_SETDISPLAYCLOCKDIV =(byte)0xD5;
    public static final byte SSD1306_SETPRECHARGE =(byte)0xD9;
    public static final byte SSD1306_SETMULTIPLEX =(byte)0xA8;
    public static final byte SSD1306_SETLOWCOLUMN =(byte)0x00;
    public static final byte SSD1306_SETHIGHCOLUMN=(byte)0x10;
    public static final byte SSD1306_SETSTARTLINE=(byte)0x40;
    public static final byte SSD1306_MEMORYMODE=(byte)0x20;
    public static final byte SSD1306_COMSCANINC=(byte)0xC0;
    public static final byte SSD1306_COMSCANDEC=(byte)0xC8;
    public static final byte SSD1306_SEGREMAP=(byte)0xA1;
    public static final byte SSD1306_CHARGEPUMP=(byte)0x8D;

    public static final byte SSD1306_EXTERNALVCC=(byte)0x10;
    public static final byte SSD1306_INTERNALVCC=(byte)0x14;

    public static final byte BLACK = (byte)0;
    public static final byte WHITE = (byte)1;

    public static final byte SSD_I2C_ADDRESS = (byte)0x3C;

    public static final byte _rawWidth = (byte)0x3F;
    public static final byte _rawHeight = (byte)0x2F;
}
