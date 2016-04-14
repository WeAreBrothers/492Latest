package com.example.robien.beachbuddy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

/**
 * Created by DarthMerl on 4/7/2016.
 */
public class messenger extends AppCompatActivity {
    Context ctx;
    AlertDialog alertDialog;
    TextView email2;
    Button sendbtn, returnProfile;
    EditText msgbody;
    String msg, recipient, sender, msgID;
    Random rand = new Random();
    int msgRandom;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.messenger);
        sendbtn = (Button)findViewById(R.id.sender);
        returnProfile = (Button)findViewById(R.id.profilertn);
        email2 = (TextView)findViewById(R.id.recipient);


        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setTitle("Send Message to: ");

        email2.setText(NavigationActivity.studentEmail);
        msgbody = (EditText)findViewById(R.id.msgbody);
        msgRandom = rand.nextInt(1000)+1;
        //Get MsgID




        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = msgbody.getText().toString();
                recipient = email2.getText().toString();
                sender = LoginActivity.email.getText().toString();
                msgID = Double.toString(msgRandom);

                String sendMsgURL = "http://52.25.144.228/sendmsg1.php";

                try {
                    URL url = new URL(sendMsgURL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("msg", "UTF-8") + "=" + URLEncoder.encode(msg, "UTF-8") + "&" +
                            URLEncoder.encode("recipient", "UTF-8") + "=" + URLEncoder.encode(recipient, "UTF-8") + "&" +
                            URLEncoder.encode("sender", "UTF-8") + "=" + URLEncoder.encode(sender, "UTF-8")+ "&" +
                            URLEncoder.encode("msgID", "UTF-8") + "=" + URLEncoder.encode(msgID, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream IS = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS,"iso-8859-1"));
                    String response = "";
                    String line  = "";
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        response+= line;
                    }
                    bufferedReader.close();
                    IS.close();
                    httpURLConnection.disconnect();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(),"Message Sent!",Toast.LENGTH_LONG).show();// Set your own toast  message
                startActivity(new Intent(messenger.this, LoginActivity.class));
            }





        });


        returnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(messenger.this, LoginActivity.class));

            }
        });


    }





}
