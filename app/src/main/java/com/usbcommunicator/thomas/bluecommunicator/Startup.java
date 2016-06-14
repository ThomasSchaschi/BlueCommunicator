package com.usbcommunicator.thomas.bluecommunicator;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.palaima.smoothbluetooth.Device;
import io.palaima.smoothbluetooth.SmoothBluetooth;

/**
 * This project was created by Thomas Schaschinger on 14th June 2016
 *
 * SmoothBluetooth
 * https://github.com/palaima/AndroidSmoothBluetooth
 */

public class Startup extends AppCompatActivity {

    private final String TAG = "Startup - ";

    public SmoothBluetooth smoothBluetooth;

    private Button btnConnect, btnDisconnect;
    private ArrayAdapter adaper;
    public ListView deviceView;
    String[] globalDeviceArrayList;

    public Context startUpContex;

    public HashMap<String, Device> deviceAssortment;


    //Selected item identification from listview
    private String lastItem = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        startUpContex = this;
        deviceView = (ListView)findViewById(R.id.list_item_deviceList);
        setViewItemListener();

        btnConnect = (Button)findViewById(R.id.btnConnect);
        btnDisconnect = (Button)findViewById(R.id.btnDisconnect);

        smoothBluetooth = new SmoothBluetooth(this);
        smoothBluetooth.setListener(mListener);
        smoothBluetooth.tryConnection();


        globalDeviceArrayList = new String[10];

        //Log.i(TAG, "" + globalDeviceArrayList.toString());
        //adaper = new ArrayAdapter<String>(this, R.layout.listitem, globalDeviceArrayList);
        //deviceView.setAdapter(adaper);


        //Finished Initialization


    }

    //Setup Listener for Item to choose from
    private void setViewItemListener(){
        deviceView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemFromList = (String)deviceView.getItemAtPosition(position);
                Log.i(TAG, "ReceivedItem : " +  selectedItemFromList);
                lastItem = selectedItemFromList;
            }
        });
    }


    private SmoothBluetooth.Listener mListener = new SmoothBluetooth.Listener() {
        @Override
        public void onBluetoothNotSupported() {
            //device does not support bluetooth
        }

        @Override
        public void onBluetoothNotEnabled() {
            //bluetooth is disabled, probably call Intent request to enable bluetooth
        }

        @Override
        public void onConnecting(Device device) {
            //called when connecting to particular device
            Log.i(TAG, "Connection attempt to device : " + device.getName());
        }

        @Override
        public void onConnected(Device device) {
            //called when connected to particular device
            Log.i(TAG, "Connection succeeded to device : " + device.getName());


        }

        @Override
        public void onDisconnected() {
            //called when disconnected from device
        }

        @Override
        public void onConnectionFailed(Device device) {
            //called when connection failed to particular device
            Log.i(TAG, "Connection failed to device : " + device.getName());
        }

        @Override
        public void onDiscoveryStarted() {
            //called when discovery is started
        }

        @Override
        public void onDiscoveryFinished() {
            //called when discovery is finished
        }

        @Override
        public void onNoDevicesFound() {
            //called when no devices found
        }

        @Override
        public void onDevicesFound(List<Device> deviceList, SmoothBluetooth.ConnectionCallback connectionCallback) {
            //receives discovered devices list and connection callback
            //you can filter devices list and connect to specific one
            //connectionCallback.connectTo(deviceList.get(position));


            globalDeviceArrayList = new String[deviceList.size()];
            deviceAssortment = new HashMap<>();

            //ArrayList<String> stringDeviceList = new ArrayList<String>();
            for(int i = 0; i < deviceList.size(); i++){
                Log.i(TAG, "Device : " + deviceList.get(i).getName());
                globalDeviceArrayList[i] = deviceList.get(i).getName();
                deviceAssortment.put(deviceList.get(i).getName(), deviceList.get(i));
            }

            //String[] mobileArray = (String[])stringDeviceList.toArray();
            //String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry","WebOS","Ubuntu","Windows7","Max OS X"};


            adaper = new ArrayAdapter<String>(startUpContex, R.layout.listitem, globalDeviceArrayList);
            deviceView.setAdapter(adaper);


            final SmoothBluetooth.ConnectionCallback finalConnectionCallback = connectionCallback;
            btnConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //OnClick of btnConnect
                    Log.i(TAG, "btnConnect");

                    if(!lastItem.equals("")){
                        Log.i(TAG, "Attempting connection to device : " + lastItem);
                        //connectionCallback.connectTo(deviceAssortment.get(lastItem));
                        connectToDevice(deviceAssortment.get(lastItem), finalConnectionCallback);
                        lastItem = "";
                    }else{
                        Log.i(TAG, "No item selected.");
                    }
                }
            });



            connectionCallback = connectionCallback;
        }

        public void connectToDevice(Device device, SmoothBluetooth.ConnectionCallback callback){
            callback.connectTo(device);
        }


        @Override
        public void onDataReceived(int data) {
            //receives all bytes

        }
    };

    /**
     * Button Listener to handle connect / disconnect commands
     */
    public View.OnClickListener genericButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            final String TAG = "GenericButtonListener - ";
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        smoothBluetooth.stop();
    }
}


