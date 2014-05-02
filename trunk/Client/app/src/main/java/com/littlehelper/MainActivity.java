package com.littlehelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends Activity {

    private static final UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //UUID for serial connection
    private static final String MAC = "00:12:12:04:08:90"; //linvor mac adress
    private static final int REQUEST_ENABLE_BT = 123;
    private OutputStream out;
    private BluetoothAdapter adapter;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private CommandSender forwardCmd = new CommandSender(Command.STRAIGHT);
    private CommandSender backwardCmd = new CommandSender(Command.STOP);
    private CommandSender leftCmd = new CommandSender(Command.LEFT);
    private CommandSender rightCmd = new CommandSender(Command.RIGHT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBluetooth();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (out == null) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setCancelable(false);
            alert.setTitle("Littler Helper");
            alert.setMessage("Bluetooth is not initialized");
            alert.setPositiveButton("Reconnect", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    initBluetooth();
                }
            });
            alert.show();
        }
    }

    public void onForwardClicked(View view) {
        try {
            out.write(Command.STRAIGHT);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        if (forwardCmd.isStarted()) {
//            return;
//        }
//        new Thread(forwardCmd).start();
//        executor.submit(new CommandSender(Command.STRAIGHT));
    }

    public void onBackwardClicked(View view) {
        try {
            out.write(Command.STOP);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        if (backwardCmd.isStarted()) {
//            return;
//        }
//        new Thread(backwardCmd).start();
//        new Thread(new CommandSender(Command.STOP)).start();
//        executor.execute(new CommandSender(Command.STOP));
    }

    public void onRightClicked(View view) {
        try {
            out.write(Command.RIGHT);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        if (rightCmd.isStarted()) {
//            return;
//        }
//        new Thread(rightCmd).start();
//        new Thread(new CommandSender(Command.RIGHT)).start();
//        executor.execute(new CommandSender(Command.RIGHT));
    }

    public void onLeftClicked(View view) {
        try {
            out.write(Command.LEFT);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        if (leftCmd.isStarted()) {
//            return;
//        }
//        new Thread(leftCmd).start();
//        new Thread(new CommandSender(Command.LEFT)).start();
//        executor.execute(new CommandSender(Command.LEFT));
    }

    public void onReconnectClicked(View view) {
        reconnect();
//        final ProgressDialog pd = new ProgressDialog(this);
//        pd.show();
//        new AsyncTask<Void, Void, Void>() {
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                reconnect();
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                pd.dismiss();
//            }
//        };
    }

    public void onDisconnectClicked(View view) {
        try {
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        final ProgressDialog pd = new ProgressDialog(this);
//        pd.show();
//        new AsyncTask<Void, Void, Void>() {
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                try {
//                    out.close();
//                    socket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                pd.dismiss();
//            }
//        };
    }

    private void initBluetooth() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            // Device does not support Bluetooth
            finish(); //exit
        }

        if (!adapter.isEnabled()) {
            //make sure the device's bluetooth is enabled
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, REQUEST_ENABLE_BT);
        }

        device = adapter.getRemoteDevice(MAC); //get remote device by mac, we assume these two devices are already paired

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            socket = device.createRfcommSocketToServiceRecord(SERIAL_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            socket.connect();
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reconnect() {

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            if (out != null && socket != null) {
                out.close();
                socket.close();
            }
            socket = device.createRfcommSocketToServiceRecord(SERIAL_UUID);
            socket.connect();
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class CommandSender implements Runnable {

        private final byte[] command;
        private volatile boolean started;

        private CommandSender(byte[] command) {
            this.command = command;
        }

        @Override
        public void run() {
            if (out == null) {
                return;
            }

            started = true;
            try {
                out.write(command);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            started = false;
        }

        public synchronized boolean isStarted() {
            return started;
        }
    }

    private static class Command {
        public static final byte[] STRAIGHT = new byte[]{0x01, 0, 0, 0, 0, 0};
        public static final byte[] RIGHT = new byte[]{0x02, 0, 0, 0, 0, 0};
        public static final byte[] LEFT = new byte[]{0x03, 0, 0, 0, 0, 0};
        public static final byte[] STOP = new byte[]{0x04, 0, 0, 0, 0, 0};
        public static final byte[] EMPTY = new byte[0];

    }
}
