package com.example.adiptodey.gamepad;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.view.MotionEvent;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private Button upbutton,downbutton,leftbutton,rightbutton;
    private TextView textv;
    boolean buttonpressed=false;
    private String serverip="0.0.0.0",serverport="0000",windowtitle="Unknown",keylayout="Unknown";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textv=(TextView)findViewById(R.id.textView);
        textv.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        ArrayList<String> conf= intent.getStringArrayListExtra("conf");
        if(conf != null)
        {serverip=conf.get(0);
            serverport=conf.get(1);
            windowtitle=conf.get(2);
            keylayout=conf.get(3);}
        textv.append(System.getProperty("line.separator"));
        textv.append("serverip:"+serverip+System.getProperty("line.separator"));
        textv.append("server port:"+serverport+System.getProperty("line.separator"));
        textv.append("windowtitle:"+windowtitle+System.getProperty("line.separator"));
        textv.append("keyboard layout:"+keylayout+System.getProperty("line.separator"));

        upbutton=(Button)findViewById(R.id.up_btn);
        upbutton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction()==MotionEvent.ACTION_DOWN) {
                    upbutton.setBackgroundResource(R.drawable.buttonshape);
                    buttonpressed=true;
                    ClientAsyncTask clientAST = new ClientAsyncTask();
                    textv.append("UP"+"\n");
                    clientAST.execute(new String[]{serverip, serverport, "UP"});
                }
                else{
                    buttonpressed=false;
                    upbutton.setBackgroundResource(R.drawable.buttonshapeunpressed);}
                return true;
            }
        });
        downbutton=(Button)findViewById(R.id.down_btn);
        downbutton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction()==MotionEvent.ACTION_DOWN) {
                    downbutton.setBackgroundResource(R.drawable.buttonshape);
                    buttonpressed=true;
                    ClientAsyncTask clientAST = new ClientAsyncTask();
                    textv.append("DOWN"+"\n");
                    clientAST.execute(new String[]{serverip, serverport, "DOWN"});
                }
                else{
                    buttonpressed=false;
                    downbutton.setBackgroundResource(R.drawable.buttonshapeunpressed);}
                return true;
            }
        });
        leftbutton=(Button)findViewById(R.id.left_btn);
        leftbutton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction()==MotionEvent.ACTION_DOWN) {
                    leftbutton.setBackgroundResource(R.drawable.buttonshape);
                    buttonpressed=true;
                    ClientAsyncTask clientAST = new ClientAsyncTask();
                    textv.append("LEFT"+"\n");
                    clientAST.execute(new String[]{serverip, serverport, "LEFT"});
                }
                else{
                    buttonpressed=false;
                    leftbutton.setBackgroundResource(R.drawable.buttonshapeunpressed);}
                return true;
            }
        });
        rightbutton=(Button)findViewById(R.id.right_btn);
        rightbutton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction()==MotionEvent.ACTION_DOWN) {
                    rightbutton.setBackgroundResource(R.drawable.buttonshape);
                    buttonpressed=true;
                    ClientAsyncTask clientAST = new ClientAsyncTask();
                    textv.append("RIGHT"+"\n");
                    clientAST.execute(new String[]{serverip, serverport, "RIGHT"});
                }
                else{
                    buttonpressed=false;
                    rightbutton.setBackgroundResource(R.drawable.buttonshapeunpressed);}
                return true;
            }
        });


    }

    /**
     * AsyncTask which handles the communication with the server
     */
    class ClientAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = params[2];
            String readresult=null;
            byte[] lenBytes = new byte[4];
            try {
                //Create a client socket and define internet address and the port of the server
                Socket socket = new Socket(params[0], Integer.parseInt(params[1]));
                //Get the input stream of the client socket
                InputStream is = socket.getInputStream();
                //Get the output stream of the client socket
                OutputStream out = socket.getOutputStream();
                //Write data to the data output stream
                String toSend = result;
                byte[] toSendBytes = toSend.getBytes();
                int toSendLen = toSendBytes.length;
                byte[] toSendLenBytes = new byte[4];
                toSendLenBytes[0] = (byte)(toSendLen & 0xff);
                toSendLenBytes[1] = (byte)((toSendLen >> 8) & 0xff);
                toSendLenBytes[2] = (byte)((toSendLen >> 16) & 0xff);
                toSendLenBytes[3] = (byte)((toSendLen >> 24) & 0xff);
                while(buttonpressed==true){
                out.write(toSendLenBytes);
                    out.flush();
                out.write(toSendBytes);
                    out.flush();
                //Read the input stream
                is.read(lenBytes, 0, 4);
                int len = (((lenBytes[3] & 0xff) << 24) | ((lenBytes[2] & 0xff) << 16) |
                        ((lenBytes[1] & 0xff) << 8) | (lenBytes[0] & 0xff));
                byte[] receivedBytes = new byte[len];
                is.read(receivedBytes, 0, len);
                readresult = new String(receivedBytes, 0, len);
                    Thread.sleep(100);
                }
                //Close the client socket
                socket.close();
            }
           catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return readresult;
        }
        @Override
        protected void onPostExecute(String s) {
            //Write server message to the text view
            if(s!=null)
            textv.append(s+"\n");
            else
                textv.append("Failure to receive server feedback! Please check serverIP and port.\n");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent myIntent = new Intent(MainActivity.this, Settings.class);
            //myIntent.putExtra("key", value); //Optional parameters
            MainActivity.this.startActivity(myIntent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume(){
        super.onResume();

    }
}
