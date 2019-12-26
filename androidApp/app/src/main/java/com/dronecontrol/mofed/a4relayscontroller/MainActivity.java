package com.dronecontrol.mofed.a4relayscontroller;



import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {


    private Handler mHandler = new Handler();

    private Runnable mUpdateTaskup = new Runnable() {
        public void run() {

            String web = getIP();
            for (int i = 3; i < 5; ++i)
            {
                String x = status[i] == true ? "on" : "off";
                getContents(web + "/m" + x + "" + i);
                bStatus[i].setText(x);
            }


            mHandler.postAtTime(this, SystemClock.uptimeMillis() + 100);
        }//end run
    };// end runnable

    Button[] buttons;
    TextView[] bStatus;
    boolean[] status = { false, false, false, false , false};

    TextView connectionStatus;


    EditText ipText;

    String getIP()
    {
        return ipText.getText().toString();
    }

    public String getContents(String myurl) {
        try {
            String sss = new RetrieveHTMLTask().execute(myurl).get().HTML;

            return sss;
        }
        catch (Exception ex)
        {
        }

        return "";

    }


    boolean update()
    {
        try {
            String[] buttonsText = getContents(ipText.getText().toString()).split("\n");
            for (int i = 0; i < 5; ++i)
            {
                bStatus[i].setText(buttonsText[i]);
                status[i] = buttonsText[i].equals("on");
            }
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipText = (EditText)findViewById(R.id.ipText);
        connectionStatus = (TextView)findViewById(R.id.status);

        buttons = new Button[5];
        buttons[0] = (Button)findViewById(R.id.button1);
        buttons[1] = (Button)findViewById(R.id.button2);
        buttons[2] = (Button)findViewById(R.id.button3);
        buttons[3] = (Button)findViewById(R.id.button4);
        buttons[4] = (Button)findViewById(R.id.button5);

        bStatus = new TextView[5];
        bStatus[0] = (TextView)findViewById(R.id.FanStatus);
        bStatus[1] = (TextView)findViewById(R.id.LightStatus);
        bStatus[2] = (TextView)findViewById(R.id.WaterPumpStatus);
        bStatus[3] = (TextView)findViewById(R.id.OpenDoorStatus);
        bStatus[4] = (TextView)findViewById(R.id.CloseDoorStatus);



        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String web = getIP();
                String x = "on";
                int motorID = 0;

                if (status[motorID])
                {
                    status[motorID] = false;
                    x = "off";
                }
                else
                {
                    status[motorID] = true;
                    x = "on";
                }

                getContents(web + "/m" + x + "" + motorID);
                buttons[motorID].setText("Fan");
                update();
            }
        });


        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String web = getIP();
                String x = "on";
                int motorID = 1;

                if (status[motorID])
                {
                    status[motorID] = false;
                    x = "off";
                }
                else
                {
                    status[motorID] = true;
                    x = "on";
                }

                getContents(web + "/m" + x + "" + motorID);
                buttons[motorID].setText("Light");
                update();
            }
        });

        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String web = getIP();
                String x = "on";
                int motorID = 2;

                if (status[motorID])
                {
                    status[motorID] = false;
                    x = "off";
                }
                else
                {
                    status[motorID] = true;
                    x = "on";
                }

                getContents(web + "/m" + x + "" + motorID);
                buttons[motorID].setText("Water Pump");

                update();
            }
        });

        buttons[3].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                String web = getIP();
                String x = "on";
                int motorID = 3;

                if (action == MotionEvent.ACTION_DOWN)
                {
                    status[motorID] = true;
                    x = "on";
                    mHandler.removeCallbacks(mUpdateTaskup);
                    mHandler.postAtTime(mUpdateTaskup,
                            SystemClock.uptimeMillis() + 50);
                }
                if (action == MotionEvent.ACTION_UP)
                {
                    status[motorID] = false;
                    x = "off";
                    mHandler.removeCallbacks(mUpdateTaskup);
                }

                getContents(web + "/m" + x + "" + motorID);
                buttons[motorID].setText("Open Door");

                update();

                return false;
            }
        });


        buttons[4].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                String web = getIP();
                String x = "on";
                int motorID = 4;

                if (action == MotionEvent.ACTION_DOWN)
                {
                    status[motorID] = true;
                    x = "on";
                    mHandler.removeCallbacks(mUpdateTaskup);
                    mHandler.postAtTime(mUpdateTaskup,
                            SystemClock.uptimeMillis() + 50);
                }
                if (action == MotionEvent.ACTION_UP)
                {
                    status[motorID] = false;
                    x = "off";
                    mHandler.removeCallbacks(mUpdateTaskup);
                }
                getContents(web + "/m" + x + "" + motorID);

                buttons[motorID].setText("Close Door");

                update();

                return false;
            }
        });

        final Handler handler = new Handler();
        final int delay = 5000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){

                String web = getIP();

                try {
                    update();
//                    if (update())
//                        connectionStatus.setText("Connected");
//                    else
//                        connectionStatus.setText("Failed to connect");

                }
                catch (Exception ex)
                {
                    connectionStatus.setText("Failed to connect");
                }

                handler.postDelayed(this, delay);

            }
        }, delay);
    }
}