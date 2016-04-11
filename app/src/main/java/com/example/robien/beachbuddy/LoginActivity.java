package com.example.robien.beachbuddy;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.facebook.FacebookSdk;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


// ADD SEARCH TO MENU NEXT TO SETTINGS DOTS LATER
public class LoginActivity extends AppCompatActivity {


    private CallbackManager callbackManager;

    static TextView info;
    private LoginButton loginButton;

    public static TextView email;
    //private TextView gender;
    public static TextView facebookName;
    private static LinearLayout infoLayout;
    private static LinearLayout relLayout;
    static ProfilePictureView profilePictureView;
    private static Button classButt;
    private static Button searchButt;
    private static Button viewInvites;
    private static Button viewMessages;
    private static Button viewGroups;
    private String sName, sFbId;
    public static String inviteName, inviteID, responseString, sEmail;

    static boolean isLoggedIn;
    static JSONObject logger;

    InviteAdapter inviteAdapter;
/*
    //check logged in state
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        Log.v("login", "login is: " +  isLoggedIn);
        Log.v("object","login is:" + logger);

        setContentView(R.layout.login_layout);
        if(isLoggedIn == true){
            loginButton = (LoginButton) findViewById(R.id.login_button);

            email = (TextView)findViewById(R.id.email);
            facebookName = (TextView)findViewById(R.id.name);


            infoLayout = (LinearLayout)findViewById(R.id.layout_info);
            relLayout = (LinearLayout)findViewById(R.id.layout_info1);
            profilePictureView = (ProfilePictureView)findViewById(R.id.image);
            classButt = (Button)findViewById(R.id.classButton);
            searchButt = (Button)findViewById(R.id.searchButton);
            viewInvites = (Button)findViewById(R.id.viewInvites);
            viewMessages = (Button)findViewById(R.id.viewMsg);
            viewGroups = (Button)findViewById(R.id.viewGroups);
            setProfileToView(logger);

        }
        else if(isLoggedIn == false){
            setContentView(R.layout.login_layout);
            email = (TextView)findViewById(R.id.email);
            facebookName = (TextView)findViewById(R.id.name);


            infoLayout = (LinearLayout)findViewById(R.id.layout_info);
            relLayout = (LinearLayout)findViewById(R.id.layout_info1);
            profilePictureView = (ProfilePictureView)findViewById(R.id.image);
            classButt = (Button)findViewById(R.id.classButton);
            searchButt = (Button)findViewById(R.id.searchButton);
            viewInvites = (Button)findViewById(R.id.viewInvites);
            viewMessages = (Button)findViewById(R.id.viewMsg);
            viewGroups = (Button)findViewById(R.id.viewGroups);
        }

        info = (TextView) findViewById(R.id.info);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.robien.beachbuddy",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        loginButton = (LoginButton) findViewById(R.id.login_button);

/*
        email = (TextView)findViewById(R.id.email);
        facebookName = (TextView)findViewById(R.id.name);


        infoLayout = (LinearLayout)findViewById(R.id.layout_info);
        relLayout = (LinearLayout)findViewById(R.id.layout_info1);
        profilePictureView = (ProfilePictureView)findViewById(R.id.image);
        classButt = (Button)findViewById(R.id.classButton);
        searchButt = (Button)findViewById(R.id.searchButton);
        viewInvites = (Button)findViewById(R.id.viewInvites);
        //loginButton.setReadPermissions(Arrays.asList("public_profile"));

 */
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                isLoggedIn = true;
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("Main", response.toString());
                                logger = object;
                                setProfileToView(object);
                                registerAccount(object);

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {

                info.setText("Login attempt failed.");
            }

        });


        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
        public void onClick(View v){
                if(isLoggedIn == true){
                isLoggedIn = false;
                Log.v("login", "login is 2: " +  isLoggedIn);
                LoginManager.getInstance().logOut();
                startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                }

            }
        });

        viewInvites = (Button)findViewById(R.id.viewInvites);
        // email, returns classes
        viewInvites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJSONInvites(v);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }



    private void setProfileToView(JSONObject jsonObject) {
        try {
            email.setText(jsonObject.getString("email"));
            sEmail = email.getText().toString();
            facebookName.setText(jsonObject.getString("name"));

            profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
            profilePictureView.setProfileId(jsonObject.getString("id"));
            relLayout.setVisibility(View.INVISIBLE);
            infoLayout.setVisibility(View.VISIBLE);
            classButt.setVisibility(View.VISIBLE);
            searchButt.setVisibility(View.VISIBLE);
            viewInvites.setVisibility(View.VISIBLE);
            viewMessages.setVisibility(View.VISIBLE);
            viewGroups.setVisibility(View.VISIBLE);

            //check messages in background
            getMessageNotification();

        } catch (JSONException e) {
            e.printStackTrace();
        }}


    public void registerAccount(JSONObject object) {
        sName = object.optString("name");
        sEmail = email.getText().toString();
        sFbId = object.optString("id");
        String method = "register";
        BackgroundTask bt = new BackgroundTask(this);
        bt.execute(method, sName, sEmail, sFbId);
    }


    public void userReg(View view) {

        startActivity(new Intent(this, RegPersonInfoActivity.class));
    }

    public void classReg(View view){
        Log.v("sEmail", "sEmail is: " +  sEmail);
        BackgroundTask bt = new BackgroundTask(this);
        bt.execute(sEmail);

        startActivity(new Intent(this, RegClassActivity.class));
    }

    public void classNav(View view) {
        Intent goToMainPageIntent = new Intent(this,NavigationActivity.class);
        startActivity(goToMainPageIntent);
    }

    public void getMsgs(View view){
        Intent goToMainPageIntent = new Intent(this,MessageView.class);
        startActivity(goToMainPageIntent);
    }

    public void getGroups(View view){
        Intent goToGroupsIntent = new Intent(this, GroupsView.class);
        startActivity(goToGroupsIntent);
    }

    class GetJSONInvites extends AsyncTask<Void, Void, String> {
        String fetchInvite_url;
        String studentEmail = email.getText().toString();

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
                while ((responseString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(responseString + "\n");
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
            responseString = result;
            try {
                JSONObject jsonObject = new JSONObject(responseString);
                JSONArray jsonArray = jsonObject.getJSONArray("invites");
                int count = 0;
                ;
                while(count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    inviteName = JO.getString("cName");
                    inviteID = JO.getString("cID");
                    count++;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Toast.makeText(getApplicationContext(), responseString, Toast.LENGTH_LONG).show();
            Intent viewInvitesIntent = new Intent(getApplicationContext(), InviteActivity.class);
            startActivity(viewInvitesIntent);
        }
    }

    public void getJSONInvites(View v) {
        new GetJSONInvites().execute();
    }

    class GetMessageNotification extends AsyncTask<Void, Void, String> {
        String fetchMsg;
        String studentEmail = LoginActivity.email.getText().toString();

        @Override
        protected void onPreExecute() {
            fetchMsg = "http://52.25.144.228/msgCheck.php";
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(fetchMsg);
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
                while ((responseString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(responseString + "\n");
                }
                Log.v("response", "response is:" + responseString);
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
            responseString = result;
            Log.v("responseString", "responseString is:" + responseString);
            if (responseString.equals("You have unread messages in your inbox")){
            Toast.makeText(getApplicationContext(), responseString, Toast.LENGTH_LONG).show();
        }
        }
    }

    public void getMessageNotification(){
        new GetMessageNotification().execute();}


}
