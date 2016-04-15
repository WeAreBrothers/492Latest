package com.example.robien.beachbuddy;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robien on 4/2/2016.
 */
public class InviteAdapter extends ArrayAdapter {

    List list = new ArrayList();
    String idFromInvite, classFromInvite;

    public InviteAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(Invite object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        InviteHolder inviteHolder;
        if(row == null) {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.invite_row_layout, parent, false);
            inviteHolder = new InviteHolder();
            inviteHolder.tx_inviteName = (TextView)row.findViewById(R.id.tx_cName);
            inviteHolder.tx_inviteID = (TextView)row.findViewById(R.id.tx_cID);
            row.setTag(inviteHolder);
        }
        else
            inviteHolder = (InviteHolder)row.getTag();

        Invite mInvite = (Invite)this.getItem(position);
        inviteHolder.tx_inviteName.setText(mInvite.getClassInvite());
        inviteHolder.tx_inviteID.setText(mInvite.getClassID());



        RadioButton accept = (RadioButton)row.findViewById(R.id.acceptRadio);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Invite selectedInvite = (Invite)list.get(position);
                idFromInvite = selectedInvite.getClassID();
                classFromInvite = selectedInvite.getClassInvite();

                list.remove(position);
                notifyDataSetChanged();

                // accept invite
                acceptInvite(v);

                // not realy declining, just serves the purpose of deleting the invite from the DB
                declineInvite();


            }
        });


        RadioButton decline = (RadioButton)row.findViewById(R.id.declineRadio);
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Invite selectedInvite = (Invite)list.get(position);
                idFromInvite = selectedInvite.getClassID();
                classFromInvite = selectedInvite.getClassInvite();

                list.remove(position);
                notifyDataSetChanged();

                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }

                declineInvite();

            }
        });

        return row;
    }

    static class InviteHolder {
        TextView tx_inviteName, tx_inviteID;
    }


    class declineInvite extends AsyncTask<Void, Void, String> {
        String declineurl;
        String emailInInvite = LoginActivity.sEmail;
        String inviteName = classFromInvite;
        String inviteID = idFromInvite;

        @Override
        protected void onPreExecute() {
            declineurl = "http://52.25.144.228/deleteinvite.php";
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(declineurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("sEmail", "UTF-8")+ "=" + URLEncoder.encode(emailInInvite, "UTF-8" )  + "&" +
                        URLEncoder.encode("cName", "UTF-8")+ "=" + URLEncoder.encode(inviteName, "UTF-8"  ) + "&" +
                        URLEncoder.encode("cID", "UTF-8")+ "=" + URLEncoder.encode(inviteID, "UTF-8"  );
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
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

    public void declineInvite() {
        new declineInvite().execute();
    }


    class acceptInvite extends AsyncTask<Void, Void, String> {
        String accept_url;
        String emailInInvite = LoginActivity.sEmail;
        String inviteName = classFromInvite;
        String inviteID = idFromInvite;
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
                String sData = URLEncoder.encode("sEmail", "UTF-8") + "=" + URLEncoder.encode(emailInInvite, "UTF-8") + "&" +
                        URLEncoder.encode("cName", "UTF-8") + "=" + URLEncoder.encode(inviteName, "UTF-8") + "&" +
                        URLEncoder.encode("cID", "UTF-8") + "=" + URLEncoder.encode(inviteID, "UTF-8");
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
