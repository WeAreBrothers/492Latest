package com.example.robien.beachbuddy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

/**
 * Created by DarthMerl on 4/7/2016.
 */
public class MessageView extends AppCompatActivity {
    ListView listView2;

    JSONObject jsonObject;
    JSONArray jsonArray;
    //StudentAdapter studentAdapter;
    MsgAdapter msgAdapter;
    Message msgSelect;
    String sEmail;
    String response;
    TextView emailFrom, recipView;
    EditText msgBox;
    Button hideSend, profileRtn;

    public static String msgID, messageEmail,sender, msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_view);
        listView2 = (ListView) findViewById(R.id.listView2);
        //sEmail = LoginActivity.sEmail;

        //inviteList = (ListView) findViewById(R.id.inviteList);

        getFormattedInvites();

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Get name from the row (position) clicked and pass it to search for the facebook profile
                msgSelect = (Message) msgAdapter.getItem(position);

                //Extract name from row
                msgID = msgSelect.getMsgID();
                messageEmail = msgSelect.getRecipient();
                sender = msgSelect.getSender();
                msg = msgSelect.getMsg();
                setContentView(R.layout.messenger);
                emailFrom = (TextView)findViewById(R.id.textView2);
                emailFrom.setText("Email from: ");
                recipView = (TextView)findViewById(R.id.recipient);
                recipView.setText(sender);
                msgBox = (EditText)findViewById(R.id.msgbody);
                msgBox.setText(msg);
                msgBox.setKeyListener(null);
                hideSend = (Button)findViewById(R.id.sender);
                hideSend.setVisibility(View.INVISIBLE);
                profileRtn = (Button)findViewById(R.id.profilertn);
                profileRtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MessageView.this, LoginActivity.class));

                    }
                });

            }
        });
    }


    class GetFormattedInvites extends AsyncTask<Void, Void, String> {
        String fetchInvite_url;
        String recipient = LoginActivity.email.getText().toString();


        @Override
        protected void onPreExecute() {
            fetchInvite_url = "http://52.25.144.228/messageRetrieve.php";
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.v("recipient", "recipient is: " +  recipient);
            try {
                URL url = new URL(fetchInvite_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String sData = URLEncoder.encode("recipient", "UTF-8") + "=" + URLEncoder.encode(recipient, "UTF-8");
                bufferedWriter.write(sData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
                StringBuilder stringBuilder = new StringBuilder();
                while ((response = bufferedReader.readLine()) != null) {
                    stringBuilder.append(response + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected void onPostExecute(String result) {
            response = result;
            try {
                msgAdapter = new MsgAdapter(getBaseContext(), R.layout.message_view);
                //listView2.setAdapter(null);
                jsonObject = new JSONObject(response);
                jsonArray = jsonObject.getJSONArray("messenger");
                int count = 0;
                while (count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    messageEmail = JO.getString("recipient");
                    msg = JO.getString("msg");
                    sender = JO.getString("sender");
                    msgID = JO.getString("msgID");
                    //inviteID = JO.getString("cID");
                    Message myMsg = new Message(sender, messageEmail, msg, msgID);
                    listView2.setAdapter(msgAdapter);
                    msgAdapter.add(myMsg);
                    count++;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    public void getFormattedInvites() {//view v?
        new GetFormattedInvites().execute();
    }
}