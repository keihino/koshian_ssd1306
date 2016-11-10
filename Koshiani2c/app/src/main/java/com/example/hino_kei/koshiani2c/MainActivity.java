package com.example.hino_kei.koshiani2c;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiListener;
import com.uxxu.konashi.lib.KonashiManager;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import info.izumin.android.bletia.BletiaException;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    private KonashiManager mKonashiManager;
    private final MainActivity self = this;

    private TextView mResultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mKonashiManager = new KonashiManager(getApplicationContext());

        ((ToggleButton)findViewById(R.id.tgl_blink)).setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                mKonashiManager.digitalWrite(Konashi.PIO4, isChecked ? Konashi.HIGH : Konashi.LOW);
            }
        });
        // for i2c start / stop
        ((ToggleButton)findViewById(R.id.tgl_i2c)).setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton w, boolean isChecked) {
//                if (isChecked) {

                    startDisplay();
                    setPixel();

//                testI2C(self);

//                mKonashiManager.i2cMode(Konashi.I2C_ENABLE_100K)
//                        .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStartConditionPipe())
//                        .then(mKonashiManager.<BluetoothGattCharacteristic>i2cReadPipe(Konashi.I2C_DATA_MAX_LENGTH, Constants_Ssd1306.SSD_I2C_ADDRESS))
//                        .then(new DonePipe<byte[], BluetoothGattCharacteristic, BletiaException, Void>() {
//                            @Override
//                            public Promise<BluetoothGattCharacteristic, BletiaException, Void> pipeDone(final byte[] result) {
//                                StringBuilder builder = new StringBuilder();
//                                for (byte b : result) {
//                                    builder.append(b).append(",");
//                                }
//                                mResultText.setText(builder.toString().substring(0, builder.length() - 1));
//                                return mKonashiManager.i2cStopCondition();
//                            }
//                        })
//                        .fail(new FailCallback<BletiaException>() {
//                            @Override
//                            public void onFail(BletiaException result) {
//                                Toast.makeText(self, result.toString(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                }
//               else {
//                    mKonashiManager.i2cStopCondition();
//                }
            }
        });
        findViewById(R.id.btn_find).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mKonashiManager.find(self);
            }
        });
        mKonashiManager.addListener(new KonashiListener() {
            @Override
            public void onConnect(KonashiManager manager) {
                refreshViews();
                if (mKonashiManager.isReady()) {
                    mKonashiManager.pinMode(Konashi.PIO4, Konashi.OUTPUT);
                }
            }

            @Override
            public void onDisconnect(KonashiManager manager) {
                refreshViews();
            }

            @Override
            public void onError(KonashiManager manager, BletiaException e) {}

            @Override
            public void onUpdatePioOutput(KonashiManager manager, int value) {}

            @Override
            public void onUpdateUartRx(KonashiManager manager, byte[] value) {}

            @Override
            public void onUpdateBatteryLevel(KonashiManager manager, int level) {}

            @Override
            public void onUpdateSpiMiso(KonashiManager manager, byte[] value) {}
        });
    }

    @Override
    protected void onDestroy() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(mKonashiManager.isConnected()){
                    mKonashiManager.reset()
                            .then(new DoneCallback<BluetoothGattCharacteristic>() {
                                @Override
                                public void onDone(BluetoothGattCharacteristic result) {
                                    mKonashiManager.disconnect();
                                }
                            });
                }
            }
        }).start();
        super.onDestroy();
    }

    private void refreshViews() {
        boolean isReady = mKonashiManager.isReady();
        findViewById(R.id.btn_find).setVisibility(isReady ? View.GONE : View.VISIBLE);
        //findViewById(R.id.tgl_blink).setVisibility(isReady ? View.VISIBLE : View.GONE);
        findViewById(R.id.tgl_i2c).setVisibility(isReady ? View.VISIBLE : View.GONE);
    }

    private void i2cCommand(byte cmd) {
//        mKonashiManager.i2cStartCondition();
//        mKonashiManager.i2cWrite(2, new byte[]{(byte)0x00, cmd}, Constants_Ssd1306.SSD_I2C_ADDRESS);
//        mKonashiManager.i2cStopCondition();
        byte[] value = new byte[]{(byte)0x00, cmd};
        mKonashiManager.i2cMode(Konashi.I2C_ENABLE_100K)
                .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStartConditionPipe())
                .then(mKonashiManager.<BluetoothGattCharacteristic>i2cWritePipe(value.length, value, Constants_Ssd1306.SSD_I2C_ADDRESS))
                .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStopConditionPipe())
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        Toast.makeText(self, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void i2cCommandP1(byte cmd, byte p1) {
//        mKonashiManager.i2cStartCondition();
//        mKonashiManager.i2cWrite(3, new byte[]{(byte)0x00, cmd, p1}, Constants_Ssd1306.SSD_I2C_ADDRESS);
//        mKonashiManager.i2cStopCondition();
        byte[] value = new byte[]{(byte)0x00, cmd, p1};
        mKonashiManager.i2cMode(Konashi.I2C_ENABLE_100K)
                .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStartConditionPipe())
                .then(mKonashiManager.<BluetoothGattCharacteristic>i2cWritePipe(value.length, value, Constants_Ssd1306.SSD_I2C_ADDRESS))
                .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStopConditionPipe())
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        Toast.makeText(self, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void i2cCommandP2(byte cmd, byte p1, byte p2) {
//        mKonashiManager.i2cStartCondition();
//        mKonashiManager.i2cWrite(4, new byte[]{(byte)0x00, cmd, p1, p2}, Constants_Ssd1306.SSD_I2C_ADDRESS);
//        mKonashiManager.i2cStopCondition();
        byte[] value = new byte[]{(byte)0x00, cmd, p1, p2};
        mKonashiManager.i2cMode(Konashi.I2C_ENABLE_100K)
                .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStartConditionPipe())
                .then(mKonashiManager.<BluetoothGattCharacteristic>i2cWritePipe(value.length, value, Constants_Ssd1306.SSD_I2C_ADDRESS))
                .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStopConditionPipe())
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        Toast.makeText(self, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void i2cData(byte data) {
//        mKonashiManager.i2cStopCondition();
//        mKonashiManager.i2cWrite(2, new byte[]{(byte)0x40, data}, Constants_Ssd1306.SSD_I2C_ADDRESS);
//        mKonashiManager.i2cStopCondition();
        byte[] value = new byte[]{(byte)0x40, data};
        mKonashiManager.i2cMode(Konashi.I2C_ENABLE_100K)
                .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStartConditionPipe())
                .then(mKonashiManager.<BluetoothGattCharacteristic>i2cWritePipe(value.length, value, Constants_Ssd1306.SSD_I2C_ADDRESS))
                .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStopConditionPipe())
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        Toast.makeText(self, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void testI2C(final MainActivity self) {
        // RES# pin control
        mKonashiManager.digitalWrite(Konashi.PIO4, Konashi.HIGH);
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mKonashiManager.digitalWrite(Konashi.PIO4, Konashi.LOW);
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mKonashiManager.digitalWrite(Konashi.PIO4, Konashi.HIGH);
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        byte[] value = new byte[]{(byte)0x00,(byte)0x8D,(byte)0x14};
        mKonashiManager.i2cMode(Konashi.I2C_ENABLE_100K)
                .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStartConditionPipe())
                .then(mKonashiManager.<BluetoothGattCharacteristic>i2cWritePipe(value.length, value, Constants_Ssd1306.SSD_I2C_ADDRESS))
                .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStopConditionPipe())
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        Toast.makeText(self, result.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void startDisplay() {
        // RES# pin control
        mKonashiManager.digitalWrite(Konashi.PIO4, Konashi.LOW);
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mKonashiManager.digitalWrite(Konashi.PIO4, Konashi.HIGH);
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mKonashiManager.digitalWrite(Konashi.PIO4, Konashi.LOW);
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mKonashiManager.digitalWrite(Konashi.PIO4, Konashi.HIGH);
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // i2c start
        byte[] value = new byte[]{(byte)0x00, Constants_Ssd1306.SSD1306_CHARGEPUMP, Constants_Ssd1306.SSD1306_INTERNALVCC, Constants_Ssd1306.SSD1306_SEGREMAP,
                Constants_Ssd1306.SSD1306_DISPLAYALLON_RESUME, Constants_Ssd1306.SSD1306_NORMALDISPLAY, Constants_Ssd1306.SSD1306_DISPLAYON};
        mKonashiManager.i2cMode(Konashi.I2C_ENABLE_100K)
                .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStartConditionPipe())
                .then(mKonashiManager.<BluetoothGattCharacteristic>i2cWritePipe(value.length, value, Constants_Ssd1306.SSD_I2C_ADDRESS))
                .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStopConditionPipe())
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        Toast.makeText(self, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        // display off
//        i2cCommand(Constants_Ssd1306.SSD1306_DISPLAYOFF);
        //
//        i2cCommandP1(Constants_Ssd1306.SSD1306_SETDISPLAYCLOCKDIV, (byte)0x80);
        //
//        i2cCommandP1(Constants_Ssd1306.SSD1306_SETMULTIPLEX, Constants_Ssd1306._rawHeight);
        //
//        i2cCommandP1(Constants_Ssd1306.SSD1306_SETDISPLAYOFFSET, Constants_Ssd1306.SSD1306_SETLOWCOLUMN);
        //
//        i2cCommand(Constants_Ssd1306.SSD1306_SETSTARTLINE);
        //
//        i2cCommandP1(Constants_Ssd1306.SSD1306_CHARGEPUMP, Constants_Ssd1306.SSD1306_INTERNALVCC);
        //
//        i2cCommand(Constants_Ssd1306.SSD1306_SEGREMAP);
        //
//        i2cCommand(Constants_Ssd1306.SSD1306_COMSCANDEC);
        //
//        i2cCommandP1(Constants_Ssd1306.SSD1306_SETCOMPINS, (byte)0x12);
        //
//        i2cCommandP1(Constants_Ssd1306.SSD1306_SETCONTRAST, (byte)0xCF);
        //
//        i2cCommandP1(Constants_Ssd1306.SSD1306_SETPRECHARGE, (byte)0x22);
        //
//        i2cCommandP1(Constants_Ssd1306.SSD1306_SETVCOMDETECT, (byte)0x00);
        //
//        i2cCommand(Constants_Ssd1306.SSD1306_DISPLAYALLON);
        //
//        i2cCommand(Constants_Ssd1306.SSD1306_NORMALDISPLAY);
        //
//        i2cCommandP1((byte)0x20, (byte)0b00);
        //
//        i2cCommand(Constants_Ssd1306.SSD1306_DISPLAYON);
    }

    private void setPixel() {

        // setting display (row 64 x col 48)
//        i2cCommandP2((byte)0x21, (byte)0x01, (byte)0x3e);
//        i2cCommandP2((byte)0x22, (byte)0x01, (byte)0x03);

        byte[] value = new byte[]{(byte)0x00, (byte)0x21, (byte)0x01, (byte)0x3e, (byte)0x22, (byte)0x01, (byte)0x03};
        mKonashiManager.i2cMode(Konashi.I2C_ENABLE_100K)
                .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStartConditionPipe())
                .then(mKonashiManager.<BluetoothGattCharacteristic>i2cWritePipe(value.length, value, Constants_Ssd1306.SSD_I2C_ADDRESS))
                .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStopConditionPipe())
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        Toast.makeText(self, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // put dots
        mKonashiManager.i2cStartCondition();
        mKonashiManager.i2cWrite(1, new byte[]{(byte)0x40,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, }, Constants_Ssd1306.SSD_I2C_ADDRESS);
        mKonashiManager.i2cStopCondition();
    }
}
