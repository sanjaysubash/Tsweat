package com.darkdream.tsweat.Activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.darkdream.tsweat.Models.BluetoothDeviceModel;
import com.darkdream.tsweat.R;
import com.google.android.material.card.MaterialCardView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DashboardActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 200;
    MaterialCardView btn_pairing;
    RecyclerView bluetooth_recycler;
    RelativeLayout rel_bluetooth_devices;
    LottieAnimationView img_close_popup;
    Button btn_scan;
    SwipeRefreshLayout  refresh;
    InputStream inputStream;
    Handler handler;
    TextView stepTextView;
    BluetoothSocket bluetoothSocket;
    private BluetoothAdapter bluetoothAdapter;
    boolean IsBluetoothDeviceConnected = false;
    BluetoothDeviceAdapter deviceAdapter;
    boolean doublebackExitpressonce = false;
   List<BluetoothDeviceModel> bluetoothDeviceList = new ArrayList<>();

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btn_pairing = (MaterialCardView) findViewById(R.id.btn_pairing);
        img_close_popup = (LottieAnimationView) findViewById(R.id.img_close_popup);
        bluetooth_recycler = (RecyclerView) findViewById(R.id.bluetooth_recycler);
        rel_bluetooth_devices = (RelativeLayout) findViewById(R.id.rel_bluetooth_devices);
        btn_scan = (Button) findViewById(R.id.btn_scan);
        stepTextView = (TextView) findViewById(R.id.stepTextView);
        refresh =(SwipeRefreshLayout) findViewById(R.id.refresh);
        btnOpenProfile =(Button) findViewById(R.id.btn_open_profile);


        bluetooth_recycler.setLayoutManager(new LinearLayoutManager(this));

        bluetoothDeviceList = new ArrayList<>();

        btn_pairing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                containsarray   = new ArrayList<>();
                checkBluetoothPermissions();
               //}
            }
        });

        img_close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rel_bluetooth_devices.setVisibility(View.GONE);
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BluetoothDevice device = bluetoothAdapter.getRemoteDevice("CC:DB:A7:2E:30:E2");// ESP32's MAC address
                if (handler!=null){
                    handler.removeCallbacksAndMessages(null);
                }
                handler = new Handler(Looper.getMainLooper());

                new Thread(() -> {
                    try {
                        @SuppressLint("MissingPermission") BluetoothSocket bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                        bluetoothSocket.connect();
                        inputStream = bluetoothSocket.getInputStream();
                        listenForData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

                refresh.setRefreshing(false);
            }
        });

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScanning(true);
            }
        });
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice("CC:DB:A7:2E:30:E2"); // ESP32's MAC address
        handler = new Handler(Looper.getMainLooper());
        if (handler!=null){
            handler.removeCallbacksAndMessages(null);
        }

        new Thread(() -> {
            try {
                @SuppressLint("MissingPermission") BluetoothSocket bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                bluetoothSocket.connect();
                inputStream = bluetoothSocket.getInputStream();
                listenForData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


    }

    @SuppressLint("MissingPermission")
    private void connectToDevice(String deviceAddress) {
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
        try {
            String deviceId = "";

            @SuppressLint("MissingPermission") BluetoothSocket socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));  // Replace MY_UUID with your UUID
            socket.connect();
            BluetoothDevice devicer = bluetoothAdapter.getRemoteDevice("CC:DB:A7:2E:30:E2"); // ESP32's MAC address
            handler = new Handler(Looper.getMainLooper());
            if (handler!=null){
                handler.removeCallbacksAndMessages(null);
            }

            new Thread(() -> {
                try {
                    @SuppressLint("MissingPermission") BluetoothSocket bluetoothSocket = devicer.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                    bluetoothSocket.connect();
                    inputStream = bluetoothSocket.getInputStream();
                    listenForData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();


            rel_bluetooth_devices.setVisibility(View.GONE);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to connect to devicer", Toast.LENGTH_SHORT).show();
        }
    }



    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 1;
    private static final int REQUEST_BLUETOOTH_CONNECT = 2;
    private void checkBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {  // Android 12+
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.ACCESS_FINE_LOCATION // This is required for scanning in older versions
                }, REQUEST_BLUETOOTH_PERMISSIONS);
            } else {
                startBluetooth();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  // Android 6.0 to 11
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, REQUEST_BLUETOOTH_PERMISSIONS);
            } else {
                startBluetooth();
            }
        } else {
            startBluetooth();  // Older devices, no runtime permission needed
        }
    }


    @SuppressLint("MissingPermission")
    private void startBluetooth() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported", Toast.LENGTH_SHORT).show();
            return;
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        this.registerReceiver(BTReceiver, filter);
        rel_bluetooth_devices.setVisibility(View.VISIBLE);
        
        deviceAdapter = new BluetoothDeviceAdapter(this, bluetoothDeviceList, device -> {
            // Handle device click
            connectToDevice(device.getDeviceAddress());
        });
        bluetooth_recycler.setAdapter(deviceAdapter);

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            startScanning(false);
        }
    }

    public boolean isConnected(BluetoothSocket socket) {
        return socket != null && socket.isConnected();
    }

    @SuppressLint("MissingPermission")
    private void startScanning(boolean isScanning) {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cancel ongoing discovery if necessary
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        // Start Bluetooth discovery
        bluetoothAdapter.startDiscovery();
        Toast.makeText(this, "Scanning for Bluetooth devices...", Toast.LENGTH_SHORT).show();

        if(!isScanning){
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    String deviceName = device.getName();
                    String deviceAddress = device.getAddress();
                    Log.d("PairedDevice", "Paired device: " + deviceName + " [" + deviceAddress + "]");
                    // bluetoothDeviceList.add(new BluetoothDeviceModel(deviceName, deviceAddress));
                    if(!containsarray.contains(deviceName)){
                        bluetoothDeviceList.add(new BluetoothDeviceModel(deviceName, deviceAddress));
                        deviceAdapter.notifyDataSetChanged();
                        containsarray.add(deviceName);
                    }
                }
            }
        }else{
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(receiver, filter);

         /*   Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    String deviceName = device.getName();
                    String deviceAddress = device.getAddress();
                    Log.d("PairedDevice", "Paired device: " + deviceName + " [" + deviceAddress + "]");
                    // bluetoothDeviceList.add(new BluetoothDeviceModel(deviceName, deviceAddress));
                    if(!containsarray.contains(deviceName)){
                        bluetoothDeviceList.add(new BluetoothDeviceModel(deviceName, deviceAddress));
                        deviceAdapter.notifyDataSetChanged();
                        containsarray.add(deviceName);
                    }
                }
            }*/
        }

    }

    private final BroadcastReceiver BTReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
             IsBluetoothDeviceConnected = true;

                btn_pairing.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#36CA36")));
            }else{
                IsBluetoothDeviceConnected = false;
                btn_pairing.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e30427")));
            }

        }
    };
    List<String> containsarray   = new ArrayList<>();
    List<String> containsarray1   = new ArrayList<>();
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the Bluetooth device from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                @SuppressLint("MissingPermission") String deviceName = device.getName();
                String deviceAddress = device.getAddress();
                if (deviceName == null || deviceName.isEmpty()) {
                    deviceName = "Unknown Device";
                }

                if(!containsarray1.contains(deviceName)){
                    bluetoothDeviceList.add(0, new BluetoothDeviceModel(deviceName, deviceAddress));
                    deviceAdapter.notifyDataSetChanged();
                    containsarray1.add(deviceName);
                }else{
                    for(int i = 0;i< bluetoothDeviceList.size() ; i++){
                        if(bluetoothDeviceList.get(i).getDeviceName().equals(deviceName))
                                bluetoothDeviceList.remove(i);
                    }
                    bluetoothDeviceList.add(0, new BluetoothDeviceModel(deviceName, deviceAddress));
                }

                Log.d("BluetoothDevice", "Device found: " + deviceName + " [" + deviceAddress + "]");
                Toast.makeText(context, "Found device: " + deviceName, Toast.LENGTH_SHORT).show();
            }
        }
    };
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startBluetooth();  // Permissions granted
            } else {
                // Permission denied, handle appropriately
                Toast.makeText(this, "Bluetooth permissions are required", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_BLUETOOTH_CONNECT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);// Permissions granted
            } else {
                // Permission denied, handle appropriately
                Toast.makeText(this, "Bluetooth connect permissions are required", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private List<BluetoothDeviceModel> getVirtualDevices() {
        List<BluetoothDeviceModel> virtualDevices = new ArrayList<>();

        // Add some virtual devices with mock names and addresses
        virtualDevices.add(new BluetoothDeviceModel("Virtual Device 1", "00:11:22:33:44:55"));
        virtualDevices.add(new BluetoothDeviceModel("Virtual Device 2", "66:77:88:99:AA:BB"));
        virtualDevices.add(new BluetoothDeviceModel("Virtual Device 3", "CC:DD:EE:FF:00:11"));
        virtualDevices.add(new BluetoothDeviceModel("Virtual Device 4", "22:33:44:55:66:77"));

        return virtualDevices;
    }

    public static class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.DeviceViewHolder> {
        private List<BluetoothDeviceModel> deviceList;
        private Context context;

        private OnDeviceClickListener onDeviceClickListener;

        // Constructor
        public BluetoothDeviceAdapter(Context context, List<BluetoothDeviceModel> deviceList, OnDeviceClickListener listener) {
            this.context = context;
            this.deviceList = deviceList;
            this.onDeviceClickListener = listener;
        }


        @NonNull
        @Override
        public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.device_item, parent, false);
            return new DeviceViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
            BluetoothDeviceModel device = deviceList.get(position);
            holder.deviceName.setText(device.getDeviceName());
            holder.deviceAddress.setText(device.getDeviceAddress());

            holder.itemView.setOnClickListener(v -> onDeviceClickListener.onDeviceClick(device));
        }

        @Override
        public int getItemCount() {
            return deviceList.size();
        }

        public interface OnDeviceClickListener {
            void onDeviceClick(BluetoothDeviceModel device);
        }

        public class DeviceViewHolder extends RecyclerView.ViewHolder {
            TextView deviceName, deviceAddress;

            public DeviceViewHolder(@NonNull View itemView) {
                super(itemView);
                deviceName = itemView.findViewById(R.id.device_name);
                deviceAddress = itemView.findViewById(R.id.device_address);
            }
        }
    }

    @SuppressLint("NewApi")
    public void onBackPressed() {

        if (!doublebackExitpressonce) {
            this.doublebackExitpressonce = true;
            Toast.makeText(this, "Press BACK again to exit application.",Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doublebackExitpressonce = false;
                }
            }, 2000);

        } else {
            super.onBackPressed();
            this.finishAffinity();
        }
    }
    private void listenForData() {
        byte[] buffer = new byte[1024];
        int bytes;

        while (true) {
            try {
                if ((bytes = inputStream.read(buffer)) > 0) {
                    String stepData = new String(buffer, 0, bytes);
                    handler.post(() -> updateStepUI(stepData));
                }
                Thread.sleep(1000); // Read data every second
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void updateStepUI(String stepData) {
        stepTextView.setText(stepData);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Button btnOpenProfile = findViewById(R.id.btn_open_profile);
    btnOpenProfile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(android.view.View view) {
            Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
    });

}
