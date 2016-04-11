package com.example.robien.beachbuddy;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;

/**
 * Created by Calvin on 2/24/2016.
 */
public class NavigationActivity extends AppCompatActivity {

    Button search;
    EditText searchClass;
    String JSON_String, idString, json_url, selectedPreference;
    JSONObject jsonObject;
    JSONArray jsonArray;
    StudentAdapter studentAdapter;
    ListView listView;
    Student selectedStudent;
    Spinner searchPref;
    String[] split;
    AlertDialog.Builder builder;

    static String name, email, studentName, studentEmail, studentClassID, studentClassName,
            className, classNum, instructor, c_Name, c_ID;


    public static String ID; // to save the facebook ID (will need in another class)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_main);

        search = (Button)findViewById(R.id.search);
        searchClass = (EditText)findViewById(R.id.classSearch);
        listView = (ListView)findViewById(R.id.listView);

        searchPref = (Spinner)findViewById(R.id.searchPreference);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.search_preferences, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchPref.setAdapter(adapter);
        searchPref.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPreference = parent.getItemAtPosition(position).toString();
                /**
                if(selectedPreference.equalsIgnoreCase("Both Course Name and Course Number")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity.this);
                    LayoutInflater inflater = NavigationActivity.this.getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.dialog_twoinput_layout, null));
                    builder.setTitle("Search by Course Name and ID");

                    final EditText classNameInput = (EditText)


                    // Add the buttons
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            //c_Name = classNameInput.getText().toString();
                            //c_ID = classNumberInput.getText().toString();
                            Toast.makeText(getApplicationContext(), c_Name + " " + c_ID, Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                 **/

                if(selectedPreference.equalsIgnoreCase("Both Course Name and Course Number"))
                    searchClass.setHint("e.g. Math 210");
                if(selectedPreference.equalsIgnoreCase("Both Course Name and Instructor"))
                    searchClass.setHint("e.g. Math Jones");
                if(selectedPreference.equalsIgnoreCase("All of the Above"))
                    searchClass.setHint("e.g. Math 210 Jones");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                if(selectedPreference.equalsIgnoreCase("Course Name")) {
                    getJSONByNameOnly(v);
                }
                else if(selectedPreference.equalsIgnoreCase("Course Number")) {
                    getJSONByNumberOnly(v);
                }
                else if(selectedPreference.equalsIgnoreCase("Instructor")) {
                    getJSONByInstructorOnly(v);
                }
                else if(selectedPreference.equalsIgnoreCase("Both Course Name and Course Number")) {
                    String input = searchClass.getText().toString();
                    split = input.split("\\s+");
                    int size = split.length;
                    if(size != 2) {
                        builder = new AlertDialog.Builder(NavigationActivity.this);
                        builder.setTitle("Invalid Input");
                        if(size < 2)
                            builder.setMessage("You put too few parameters. Try again.");
                        else if(size > 2)
                            builder.setMessage("You put too many parameters. Try again.");
                        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else
                        getJSONByBothNameAndID(v);
                }
                else if(selectedPreference.equalsIgnoreCase("Both Course Name and Instructor")) {
                    String input = searchClass.getText().toString();
                    split = input.split("\\s+");
                    int size = split.length;
                    if(size != 2) {
                        builder = new AlertDialog.Builder(NavigationActivity.this);
                        builder.setTitle("Invalid Input");
                        if(size < 2)
                            builder.setMessage("You put too few parameters. Try again.");
                        else if(size > 2)
                            builder.setMessage("You put too many parameters. Try again.");
                        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else
                        getJSONByBothNameAndInstructor(v);
                }
                else if(selectedPreference.equalsIgnoreCase("All of the Above")) {
                    String input = searchClass.getText().toString();
                    split = input.split("\\s+");
                    int size = split.length;
                    if(size != 3) {
                        builder = new AlertDialog.Builder(NavigationActivity.this);
                        builder.setTitle("Invalid Input");
                        if(size < 3)
                            builder.setMessage("You put too few parameters. Try again.");
                        else if(size > 3)
                            builder.setMessage("You put too many parameters. Try again.");
                        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else
                        getJSONByAllOfTheAbove(v);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Get name from the row (position) clicked and pass it to search for the facebook profile
                selectedStudent = (Student) studentAdapter.getItem(position);

                //Extract name from row
                studentName = selectedStudent.getName();
                studentEmail = selectedStudent.getEmail();
                studentClassID = selectedStudent.getClassNum();
                studentClassName = selectedStudent.getClassName();

                getFbId(view);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Searching students by class name ONLY
     */
    class SearchByNameOnlyBackground extends AsyncTask<Void, Void, String> {
        String cName = searchClass.getText().toString();

        @Override
        protected void onPreExecute() {
            json_url = "http://52.25.144.228/searchbynameonly.php";
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String sData = URLEncoder.encode("cName", "UTF-8")+ "=" + URLEncoder.encode(cName, "UTF-8");
                bufferedWriter.write(sData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
                StringBuilder stringBuilder = new StringBuilder();
                while((JSON_String = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_String + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            }
            catch(MalformedURLException e) {
                e.printStackTrace();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected void onPostExecute(String result) {
            JSON_String = result;
            try {
                studentAdapter = new StudentAdapter(getBaseContext(), R.layout.student_row_layout);
                listView.setAdapter(null);
                jsonObject = new JSONObject(JSON_String);
                jsonArray = jsonObject.getJSONArray("students");
                int count = 0;
                ;
                while(count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    name = JO.getString("sName");
                    email = JO.getString("sEmail");
                    className = JO.getString("cName");
                    classNum = JO.getString("cID");
                    instructor = JO.getString("cInstructor");
                    Student student = new Student(name, email, className, classNum, instructor);
                    listView.setAdapter(studentAdapter);
                    studentAdapter.add(student);
                    count++;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getJSONByNameOnly(View v) {

        new SearchByNameOnlyBackground().execute();
    }

    /**
     * Searching students by class ID ONLY
     */
    class SearchByNumberOnlyBackground extends AsyncTask<Void, Void, String> {
        String cID = searchClass.getText().toString();

        @Override
        protected void onPreExecute() {
            json_url = "http://52.25.144.228/searchbynumberonly.php";
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String sData = URLEncoder.encode("cID", "UTF-8")+ "=" + URLEncoder.encode(cID, "UTF-8");
                bufferedWriter.write(sData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
                StringBuilder stringBuilder = new StringBuilder();
                while((JSON_String = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_String + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            }
            catch(MalformedURLException e) {
                e.printStackTrace();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected void onPostExecute(String result) {
            JSON_String = result;
            try {
                studentAdapter = new StudentAdapter(getBaseContext(), R.layout.student_row_layout);
                listView.setAdapter(null);
                jsonObject = new JSONObject(JSON_String);
                jsonArray = jsonObject.getJSONArray("students");
                int count = 0;
                ;
                while(count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    name = JO.getString("sName");
                    email = JO.getString("sEmail");
                    className = JO.getString("cName");
                    classNum = JO.getString("cID");
                    instructor = JO.getString("cInstructor");
                    Student student = new Student(name, email, className, classNum, instructor);
                    listView.setAdapter(studentAdapter);
                    studentAdapter.add(student);
                    count++;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getJSONByNumberOnly(View v) {

        new SearchByNumberOnlyBackground().execute();
    }


    /**
     * Searching students by instructor ONLY
     */
    class SearchByInstructorOnlyBackground extends AsyncTask<Void, Void, String> {
        String cInstructor = searchClass.getText().toString();

        @Override
        protected void onPreExecute() {
            json_url = "http://52.25.144.228/searchbyinstructoronly.php";
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String sData = URLEncoder.encode("cInstructor", "UTF-8")+ "=" + URLEncoder.encode(cInstructor, "UTF-8");
                bufferedWriter.write(sData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
                StringBuilder stringBuilder = new StringBuilder();
                while((JSON_String = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_String + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            }
            catch(MalformedURLException e) {
                e.printStackTrace();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected void onPostExecute(String result) {
            JSON_String = result;
            try {
                studentAdapter = new StudentAdapter(getBaseContext(), R.layout.student_row_layout);
                listView.setAdapter(null);
                jsonObject = new JSONObject(JSON_String);
                jsonArray = jsonObject.getJSONArray("students");
                int count = 0;
                ;
                while(count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    name = JO.getString("sName");
                    email = JO.getString("sEmail");
                    className = JO.getString("cName");
                    classNum = JO.getString("cID");
                    instructor = JO.getString("cInstructor");
                    Student student = new Student(name, email, className, classNum, instructor);
                    listView.setAdapter(studentAdapter);
                    studentAdapter.add(student);
                    count++;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getJSONByInstructorOnly(View v) {

        new SearchByInstructorOnlyBackground().execute();
    }


    /**
     * Searching students by BOTH course name and ID
     */
    class SearchByBothNameAndIDBackground extends AsyncTask<Void, Void, String> {
        String cName = split[0];
        String cID = split[1];

        @Override
        protected void onPreExecute() {
            json_url = "http://52.25.144.228/searchbybothnameandid.php";
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String sData = URLEncoder.encode("cName", "UTF-8")+ "=" + URLEncoder.encode(cName, "UTF-8")  + "&" +
                        URLEncoder.encode("cID", "UTF-8")+ "=" + URLEncoder.encode(cID, "UTF-8");
                bufferedWriter.write(sData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
                StringBuilder stringBuilder = new StringBuilder();
                while((JSON_String = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_String + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            }
            catch(MalformedURLException e) {
                e.printStackTrace();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected void onPostExecute(String result) {
            JSON_String = result;
            try {
                studentAdapter = new StudentAdapter(getBaseContext(), R.layout.student_row_layout);
                listView.setAdapter(null);
                jsonObject = new JSONObject(JSON_String);
                jsonArray = jsonObject.getJSONArray("students");
                int count = 0;
                ;
                while(count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    name = JO.getString("sName");
                    email = JO.getString("sEmail");
                    className = JO.getString("cName");
                    classNum = JO.getString("cID");
                    instructor = JO.getString("cInstructor");
                    Student student = new Student(name, email, className, classNum, instructor);
                    listView.setAdapter(studentAdapter);
                    studentAdapter.add(student);
                    count++;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getJSONByBothNameAndID(View v) {

        new SearchByBothNameAndIDBackground().execute();
    }


    /**
     * Searching students by BOTH course name and Instructor
     */
    class SearchByBothNameAndInstructorBackground extends AsyncTask<Void, Void, String> {
        String cName = split[0];
        String cInstructor = split[1];

        @Override
        protected void onPreExecute() {
            json_url = "http://52.25.144.228/searchbybothnameandinstructor.php";
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String sData = URLEncoder.encode("cName", "UTF-8")+ "=" + URLEncoder.encode(cName, "UTF-8")  + "&" +
                        URLEncoder.encode("cInstructor", "UTF-8")+ "=" + URLEncoder.encode(cInstructor, "UTF-8");
                bufferedWriter.write(sData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
                StringBuilder stringBuilder = new StringBuilder();
                while((JSON_String = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_String + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            }
            catch(MalformedURLException e) {
                e.printStackTrace();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected void onPostExecute(String result) {
            JSON_String = result;
            try {
                studentAdapter = new StudentAdapter(getBaseContext(), R.layout.student_row_layout);
                listView.setAdapter(null);
                jsonObject = new JSONObject(JSON_String);
                jsonArray = jsonObject.getJSONArray("students");
                int count = 0;
                ;
                while(count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    name = JO.getString("sName");
                    email = JO.getString("sEmail");
                    className = JO.getString("cName");
                    classNum = JO.getString("cID");
                    instructor = JO.getString("cInstructor");
                    Student student = new Student(name, email, className, classNum, instructor);
                    listView.setAdapter(studentAdapter);
                    studentAdapter.add(student);
                    count++;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getJSONByBothNameAndInstructor(View v) {

        new SearchByBothNameAndInstructorBackground().execute();
    }


    /**
     * Searching students by all of the above (name, ID, instructor)
     */
    class SearchByAllOfTheAboveBackground extends AsyncTask<Void, Void, String> {
        String cName = split[0];
        String cID = split[1];
        String cInstructor = split[2];

        @Override
        protected void onPreExecute() {
            json_url = "http://52.25.144.228/searchbyalloftheabove.php";
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String sData = URLEncoder.encode("cName", "UTF-8")+ "=" + URLEncoder.encode(cName, "UTF-8")  + "&" +
                        URLEncoder.encode("cID", "UTF-8")+ "=" + URLEncoder.encode(cID, "UTF-8") + "&" +
                        URLEncoder.encode("cInstructor", "UTF-8")+ "=" + URLEncoder.encode(cInstructor, "UTF-8");
                bufferedWriter.write(sData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
                StringBuilder stringBuilder = new StringBuilder();
                while((JSON_String = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_String + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            }
            catch(MalformedURLException e) {
                e.printStackTrace();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected void onPostExecute(String result) {
            JSON_String = result;
            try {
                studentAdapter = new StudentAdapter(getBaseContext(), R.layout.student_row_layout);
                listView.setAdapter(null);
                jsonObject = new JSONObject(JSON_String);
                jsonArray = jsonObject.getJSONArray("students");
                int count = 0;
                ;
                while(count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    name = JO.getString("sName");
                    email = JO.getString("sEmail");
                    className = JO.getString("cName");
                    classNum = JO.getString("cID");
                    instructor = JO.getString("cInstructor");
                    Student student = new Student(name, email, className, classNum, instructor);
                    listView.setAdapter(studentAdapter);
                    studentAdapter.add(student);
                    count++;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getJSONByAllOfTheAbove(View v) {

        new SearchByAllOfTheAboveBackground().execute();
    }





    class FetchFbId extends AsyncTask<Void, Void, String> {
        String fetchId_url;
        String studentName = selectedStudent.getName();

        @Override
        protected void onPreExecute() {
            fetchId_url = "http://52.25.144.228/fetchId.php";
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(fetchId_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String sData = URLEncoder.encode("studentName", "UTF-8") + "=" + URLEncoder.encode(studentName, "UTF-8");
                bufferedWriter.write(sData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
                StringBuilder stringBuilder = new StringBuilder();
                while ((idString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(idString + "\n");
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
            idString = result;
            try {
                jsonObject = new JSONObject(idString);
                jsonArray = jsonObject.getJSONArray("ID");
                int count = 0;
                ID = "";

                JSONObject JO = jsonArray.getJSONObject(count);
                ID = JO.getString("sFacebookID"); // fetch the ID from database

                // Activity tracker
                Intent intent = new Intent(NavigationActivity.this, ProfileActivity.class);
                intent.putExtra("activity", "Navigation");
                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Go to user's profile

        }
    }

    public void getFbId(View v) {
        new FetchFbId().execute();
    }
}

