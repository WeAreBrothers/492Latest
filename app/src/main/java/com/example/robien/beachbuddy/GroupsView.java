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
import android.widget.Toast;

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
public class GroupsView extends AppCompatActivity {

    ListView groupList;
    GroupAdapter groupAdapter;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String response;

    public static Group selectedGroup;
    public static String groupName, groupID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups_layout);

        groupList = (ListView)findViewById(R.id.groupList);

        getGroups();

        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedGroup = (Group)groupAdapter.getItem(position);

                Intent membersIntent = new Intent(GroupsView.this, GroupMembersActivity.class);
                startActivity(membersIntent);
            }
        });
    }

    class GetGroups extends AsyncTask<Void, Void, String> {
        String fetchgroups_url;
        String studentEmail = LoginActivity.sEmail;


        @Override
        protected void onPreExecute() {
            fetchgroups_url = "http://52.25.144.228/getgroups.php";
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(fetchgroups_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String sData = URLEncoder.encode("sEmail", "UTF-8") + "=" + URLEncoder.encode(studentEmail, "UTF-8");
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
                groupAdapter = new GroupAdapter(getBaseContext(), R.layout.group_row_layout);
                jsonObject = new JSONObject(response);
                jsonArray = jsonObject.getJSONArray("groups");
                int count = 0;
                while (count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    groupName = JO.getString("cName");
                    groupID = JO.getString("cID");
                    Group myGroup = new Group(groupName, groupID);
                    groupList.setAdapter(groupAdapter);
                    groupAdapter.add(myGroup);
                    count++;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getGroups() {//view v?
        new GetGroups().execute();
    }
}