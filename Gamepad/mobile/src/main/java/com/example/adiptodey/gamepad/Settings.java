package com.example.adiptodey.gamepad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Button;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {

    EditText serverIP,serverPort,windowtitle;
    RadioGroup rg;
    Button submit,cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        serverIP = (EditText)findViewById(R.id.editText);
        serverPort = (EditText)findViewById(R.id.editText2);
        windowtitle = (EditText)findViewById(R.id.editText3);
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        rg.check(R.id.radioButton3);
        submit = (Button) findViewById(R.id.button3);
        cancel = (Button) findViewById(R.id.button4);

    }
    public void sendmessagesubmit(View view)
    {
        String serverIPs = serverIP.getText().toString();
        String serverPorts = serverPort.getText().toString();
        String windowtitles = windowtitle.getText().toString();
        String rbs = ((RadioButton)findViewById(rg.getCheckedRadioButtonId() )).getText().toString();
        ArrayList<String> conf=new ArrayList<>(4);
        conf.add(0,serverIPs);
        conf.add(1,serverPorts);
        conf.add(2,windowtitles);
        conf.add(3,rbs);
        Intent myIntent = new Intent(Settings.this, MainActivity.class);
        myIntent.putExtra("conf", conf); //Optional parameters
        Settings.this.startActivity(myIntent);
        finish();
    }
    public void sendmessagecancel(View view)
    {
        Intent myIntent = new Intent(Settings.this, MainActivity.class);
        Settings.this.startActivity(myIntent);
        finish();
    }
}
