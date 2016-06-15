package com.usbcommunicator.thomas.bluecommunicator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import io.palaima.smoothbluetooth.SmoothBluetooth;

/**
 * Created by Thomas on 15.06.2016.
 */
public class Terminal extends AppCompatActivity {


    private final String TAG = "Terminal - ";
    private AppCompatActivity parentActivity;

    private Button btnPush;
    private TextView tvTerminal;
    public EditText etTerminalInput;
    private ScrollView scroller;
    private Toolbar mToolbar;

    private final String CONSOLEPREFIX = "console> ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terminallayout);


        btnPush = (Button)findViewById(R.id.btnPush);
        tvTerminal = (TextView)findViewById(R.id.tvConsoleAppearance);
        etTerminalInput = (EditText)findViewById(R.id.etTerminalInput);
        scroller = (ScrollView)findViewById(R.id.scroller);
        mToolbar = (Toolbar)findViewById(R.id.customToolbar);
        setSupportActionBar(mToolbar);
        parentActivity = (Startup)getParent();

        listenerSet();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_constraints, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btnSwitchPerspective) {
            //Switch perspektive
            Log.i(TAG, "Switching perspektive to monitor mode.");

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void listenerSet(){
        btnPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String command = String.valueOf(etTerminalInput.getText());

                if (!command.equals("") && Connection.connectionState) {
                    pushMessage(command);

                    tvTerminal.append(CONSOLEPREFIX + "" + command + "\n");

                    //Constantly scroll to bottom of view when command pushed
                    scroller.post(new Runnable() {
                        public void run() {
                            scroller.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });

                    etTerminalInput.setText("");
                }else{
                    tvTerminal.append(CONSOLEPREFIX + "" + "No connection to device!" + "\n");
                    etTerminalInput.setText("");
                }
            }
        });
    }

    public void pushMessage(String command){
        Log.i(TAG, "Command to push: " + command);
    }

    private void returnToParent(){
        finish();
    }
}
