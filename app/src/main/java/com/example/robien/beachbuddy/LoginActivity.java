package com.example.robien.beachbuddy;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
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
import org.w3c.dom.Text;


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
public class LoginActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


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


    public static TextView beachbuddyTV,registerTV;
    InviteAdapter inviteAdapter;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        Log.v("login", "login is: " + isLoggedIn);
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
            registerTV.setVisibility(View.GONE);
            beachbuddyTV.setVisibility(View.GONE);
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

            beachbuddyTV = (TextView)findViewById(R.id.loginHeaderBeach);
            registerTV = (TextView)findViewById(R.id.registerToFB);
            registerTV.setVisibility(View.VISIBLE);
            beachbuddyTV.setVisibility(View.VISIBLE);
            TextView registerFB = (TextView)findViewById(R.id.registerToFB);
            registerFB.setTextColor(Color.WHITE);
            registerFB.setMovementMethod(LinkMovementMethod.getInstance());
        }

        loginButton = (LoginButton) findViewById(R.id.login_button);
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


        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isLoggedIn == true) {
                    isLoggedIn = false;
                    Log.v("login", "login is 2: " + isLoggedIn);
                    LoginManager.getInstance().logOut();
                    startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                }

            }
        });

        viewInvites = (Button)findViewById(R.id.viewInvites);
        viewInvites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJSONInvites();
                Intent viewInvitesIntent = new Intent(getApplicationContext(), InviteActivity.class);
                startActivity(viewInvitesIntent);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        //View headerLayout = navigationView.inflateHeaderView(R.layout.nav_profile_header);
        //navigationView.addHeaderView(headerLayout);
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
            registerTV.setVisibility(View.GONE);
            beachbuddyTV.setVisibility(View.GONE);

            //check messages in background
            getMessageNotification();

        } catch (JSONException e) {
            e.printStackTrace();
        }}


    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        switch(item.getItemId()){
            //uses drawer_menu.xml
            case R.id.addclass_id:
                Log.v("sEmail", "sEmail is: " +  sEmail);
                BackgroundTask bt = new BackgroundTask(this);
                bt.execute(sEmail);
                startActivity(new Intent(this, RegClassActivity.class));
                break;
            case R.id.search_id:
                startActivity(new Intent(this,NavigationActivity.class));
                break;
            case R.id.home_id:
                break;
            case R.id.profile_id:
                break;
            case R.id.invite_id:
                getJSONInvites();
                Intent viewInvitesIntent = new Intent(getApplicationContext(), InviteActivity.class);
                startActivity(viewInvitesIntent);
                break;
            case R.id.group_id:
                startActivity(new Intent(this,GroupsView.class));
                break;
            case R.id.message_id:
                startActivity(new Intent(this,MessageView.class));
                break;

            case R.id.help_id:
                startActivity(new Intent(this, ScreenslideTutorialActivity.class));
                break;
        }


        return true;
    }

    public void registerAccount(JSONObject object) {
        sName = object.optString("name");
        sEmail = email.getText().toString();
        sFbId = object.optString("id");
        String method = "register";
        BackgroundTask bt = new BackgroundTask(this);
        bt.execute(method, sName, sEmail, sFbId);
    }

    //going to be not used
    public void userReg(View view) {

        startActivity(new Intent(this, RegPersonInfoActivity.class));
    }

    //going to be not used
    public void classReg(View view){
        Log.v("sEmail", "sEmail is: " +  sEmail);
        BackgroundTask bt = new BackgroundTask(this);
        bt.execute(sEmail);

        startActivity(new Intent(this, RegClassActivity.class));
    }

        //going to be not used
    public void classNav(View view) {
        Intent goToMainPageIntent = new Intent(this,NavigationActivity.class);
        startActivity(goToMainPageIntent);
    }

    //going to be not used
    public void getMsgs(View view){
        Intent goToMainPageIntent = new Intent(this,MessageView.class);
        startActivity(goToMainPageIntent);
    }

        //going to be not used
    public void getGroups(View view){
        Intent goToGroupsIntent = new Intent(this, GroupsView.class);
        startActivity(goToGroupsIntent);
    }
    //sets the header, not yet used
    public void setNavigationHeader(){
        View header= LayoutInflater.from(this).inflate(R.layout.nav_profile_header,null);
        navigationView.addHeaderView(header);
    }

    // hamburger icon
     @Override
     protected void onPostCreate(Bundle savedInstanceState) {
         super.onPostCreate(savedInstanceState);
         actionBarDrawerToggle.syncState();
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
        }
    }

    public void getJSONInvites() {
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
