package com.example.robien.beachbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
/**
 * Created by Robien on 2/17/2016.
 */
public class RegClassActivity extends AppCompatActivity {

    TextView CLASSREG_TEXTVIEW;
    EditText CLASS_NAME, CLASS_ID, YEAR_TAKEN, INST_FNAME, INST_LNAME;
    //Spinner CLASS_NAME;
    String cname, cid, cyear, iFname, iLname;
    private String sEmail;
    Button ADD_CLASS, DONE_ADDING_GOTO_MAIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regclass_layout);

        CLASSREG_TEXTVIEW = (TextView)findViewById(R.id.creg_tview);
        CLASS_NAME = (EditText)findViewById(R.id.classnameEText);
        CLASS_ID = (EditText)findViewById(R.id.courseNumEText);
        YEAR_TAKEN = (EditText)findViewById(R.id.yearEText);
        INST_FNAME = (EditText)findViewById(R.id.inst_fname);
        INST_LNAME = (EditText)findViewById(R.id.inst_lname);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void classReg(View v) {
        // edit text and button initalizations
        cname = CLASS_NAME.getText().toString();
        cid = CLASS_ID.getText().toString();
        cyear = YEAR_TAKEN.getText().toString();
        //until we can figure out storing current user info this will act as email key for sprint 1
        iFname = INST_FNAME.getText().toString();
        iLname = INST_LNAME.getText().toString();
        String method = "classregister";
        BackgroundTask bt = new BackgroundTask(this);
        bt.execute(method, cid, cname, cyear, iLname, iFname);
        finish();
    }
    public void goToMainPage(View view) {
        Intent goToMainPageIntent = new Intent(this,LoginActivity.class);
        startActivity(goToMainPageIntent);
    }
}