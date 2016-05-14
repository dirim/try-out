package com.saifproject.term;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


public class MainActivity extends ListActivity{

    String val;
    LocationManager locationManager ;
    String provider;
    // GPSTracker class
    GPSTracker gps;
    String url="";
    double longitude;
    double latitude;
    String parseurl=null;
    private ProgressDialog pDialog;
    JSONArray contacts = null;
    ArrayList<LinkedHashMap<String, String>> contactList=null;
    Parsing html;
    Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    TextView text;

    public static String CLIENT_ID = "LYP22X4OPZVIWAWZG2ITKZJG0YPIEHRMD3KD4H33HPFSLSIR";
    public static String CLIENT_SECRET = "FUHXUQ3NPHPJHJ0PIXPALRRKYSLR4HBEV4MD54I0WRHH42LJ";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        text=(TextView)findViewById(R.id.txt);

        //Typeface typeface=Typeface.createFromAsset(getAssets(), "fonts/Blazed.ttf");
        //text.setTypeface(typeface);
        ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String name = ((TextView) view.findViewById(R.id.name))
                        .getText().toString();
                String cost = ((TextView) view.findViewById(R.id.email))
                        .getText().toString();
                String description = ((TextView) view.findViewById(R.id.mobile))
                        .getText().toString();
                String pos=Integer.toString(position);
                // Starting single contact activity
                Intent in = new Intent(getApplicationContext(),
                        SingleClassActivity.class);
                in.putExtra("names", name);
                in.putExtra("id", cost);
                in.putExtra("lat", description);
                in.putExtra("pos",pos);
                in.putExtra("url",url);
                startActivity(in);
            }
        });


        // show location button click event
        // create class object
        gps = new GPSTracker(MainActivity.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            // \n is for new line
            Snackbar.make(mDrawerLayout, "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Snackbar.LENGTH_SHORT)
                    .show();
//            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        Bundle extras = getIntent().getExtras();
        String val=extras.getString("val");

        System.out.println("Val is "+val);
        url="https://api.foursquare.com/v2/venues/search?client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&v=20160510+&ll="+latitude+"%2C"+longitude+"&query="+val;
        try {
            new GetContacts().execute();
        }
        catch(Exception e){

        }


    }


    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            html = new Parsing();

            html.JsonParse(url, "GET");

            contactList = new ArrayList<LinkedHashMap<String, String>>();
            contactList.clear();

            contactList=html.contactList;
            System.out.println("Size id contactlist in Main " + contactList.size());

            int c=0;
            for (LinkedHashMap<String, String> map : contactList) {
                for (Map.Entry<String, String> mapEntry : map.entrySet()) {
                    if (c == 0) {
                        String key = mapEntry.getKey();
                        String value = mapEntry.getValue();

                        System.out.println("Value of key and value is " + key + "  " + value);
                    } else
                        break;
                }
                c++;
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
            if(contactList.size()==0)
            {
                text.setText("Sorry No Place found of your choice.. ");

            }
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this,contactList  ,
                    R.layout.list_item, new String[] { "names","address" ,"lat"
            }, new int[] { R.id.name,
                    R.id.email, R.id.mobile });

            setListAdapter(adapter);

        }

    }

}
