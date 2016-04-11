package com.example.robien.beachbuddy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Profile;

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
 * Created by Robien on 3/24/2016.
 */
public class InviteActivity extends AppCompatActivity {

    ListView inviteList;
    InviteAdapter inviteAdapter;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String response;

    public static String inviteID,inviteName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_layout);

        inviteList = (ListView)findViewById(R.id.inviteList);

        getFormattedInvites();
    }


    class GetFormattedInvites extends AsyncTask<Void, Void, String> {
        String fetchInvite_url;
        String studentEmail = LoginActivity.email.getText().toString();

        @Override
        protected void onPreExecute() {
            fetchInvite_url = "http://52.25.144.228/invites.php";
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(fetchInvite_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String sData = URLEncoder.encode("studentEmail", "UTF-8") + "=" + URLEncoder.encode(studentEmail, "UTF-8");
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
                Log.v("response", "response is:" + response);
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
                inviteAdapter = new InviteAdapter(getBaseContext(), R.layout.invite_row_layout);
                inviteList.setAdapter(null);
                jsonObject = new JSONObject(response);
                jsonArray = jsonObject.getJSONArray("invites");
                int count = 0;
                while(count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    inviteName = JO.getString("cName");
                    inviteID = JO.getString("cID");
                    Invite myInvite = new Invite(inviteName, inviteID);
                    inviteList.setAdapter(inviteAdapter);
                    inviteAdapter.add(myInvite);
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


    class acceptInvite extends AsyncTask<Void, Void, String> {
        String accept_url;
        String invite_Name = LoginActivity.inviteName;
        String studentEmail = LoginActivity.email.getText().toString();
        String response = "";

        @Override
        protected void onPreExecute() {
            accept_url = "http://52.25.144.228/groupReg2.php";
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(accept_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String sData = URLEncoder.encode("cName", "UTF-8") + "=" + URLEncoder.encode(invite_Name, "UTF-8") + "&" +
                        URLEncoder.encode("sEmail", "UTF-8") + "=" + URLEncoder.encode(studentEmail, "UTF-8");
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

        }
    }

    public void acceptInvite(View v) {
        new acceptInvite().execute();
    }
}

