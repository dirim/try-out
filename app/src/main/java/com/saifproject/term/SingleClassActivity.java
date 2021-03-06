package com.saifproject.term;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SingleClassActivity extends Activity {
    private final static String TOKEN = "ZBKYOILLYWU4OEK4E3ZV4QXV32TYNQEF4NRWR2OOOYSOJGTW";

    ArrayList<LinkedHashMap<String, String>> cl = null;
    Parsing html;
    String url;
    private ProgressDialog pDialog;
    int pos;
    TextView lblName;
    TextView lblEmail;
    String ans = "", heading = "", lat = "", lon = "", id = "";
    MYDBHandler dbhander;
    String val = "0";
    LinearLayout linear1;
    Button btnLike;
    String likeMessage;
    TextView likeMessageView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_class);

        linear1 = (LinearLayout) findViewById(R.id.linear1);
        //Read from Shared Preference

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        val = sharedPref.getString("val", "0");
        System.out.println("initial value is " + val);

        if (val.equals("0")) {
            //Write to Shared Preference

            sharedPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("val", "1");
            editor.commit();
            System.out.println("Final value is " + val);

            dbhander = new MYDBHandler(SingleClassActivity.this, null, null, 1);

        }
        // getting intent data
        Intent in = getIntent();


        // Get JSON values from previous intent

        String position = in.getStringExtra("pos");
        url = in.getStringExtra("url");
        pos = Integer.parseInt(position);


        // Displaying all values on the screen
        lblName = (TextView) findViewById(R.id.name_label);
        lblEmail = (TextView) findViewById(R.id.email_label);

        btnLike = (Button) findViewById(R.id.btnLike);

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new VenueLike().execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            new GetContacts().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:0,0?q=" + (lat + "," + lon)));
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class VenueLike extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SingleClassActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            html = new Parsing();
            String likeUrl = "https://api.foursquare.com/v2/venues/4b9158c9f964a52010b533e3/like?client_id="+MainActivity.CLIENT_ID+"&client_secret="+MainActivity.CLIENT_SECRET+"&v=20160510&oauth_token="+TOKEN;

            likeMessage = html.JsonParseForLike(likeUrl, "POST");

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            btnLike.setVisibility(View.INVISIBLE);
            likeMessageView = (TextView) findViewById(R.id.likeMessage);
            likeMessageView.setText(likeMessage);
            likeMessageView.setVisibility(View.VISIBLE);
        }

    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SingleClassActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            cl = new ArrayList<LinkedHashMap<String, String>>();
            html = new Parsing();
            html.JsonParse(url, "GET");

//        cl.clear();

            cl = html.contactList;

            System.out.println("size of arraylist in single is " + cl.size() + " with url " + url);
            int counter = 0;

            for (HashMap<String, String> map : cl) {
                for (Map.Entry<String, String> mapEntry : map.entrySet()) {
                    if (counter == pos) {
                        String key = mapEntry.getKey();
                        String value = mapEntry.getValue();

                        System.out.println("Value of key and value is " + key + "  " + value);
                        if (key == "id")
                            id = value;
                        if (key == "names")
                            heading = value;
                        if (!key.equals("id") && !key.equals("names"))
                            ans = ans + " " + key + "  : " + value + "\n\n";

                        if (key.equals("lat")) {
                            lat = value;
                        }
                        if (key.equals("lng"))
                            lon = value;
                    }

                }
                counter++;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            lblName.setText(heading);
            lblEmail.setText(ans);


        }
    }

    public void add(View view) {
        dbhander = new MYDBHandler(SingleClassActivity.this, null, null, 1);
        int counter = 0;
        String val = heading + ans;
        System.out.println("Values sent are " + val);
        dbhander.add(val);
        dbhander.show();
        Snackbar.make(linear1, "Data Added to Offline Page..", Snackbar.LENGTH_SHORT)
                .show();

    }

}