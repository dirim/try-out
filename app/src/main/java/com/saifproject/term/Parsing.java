package com.saifproject.term;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class Parsing {

    private String[] fields = {"address", "lat", "lng", "distance", "city", "state"};


    Context context;

    ArrayList<LinkedHashMap<String, String>> contactList;

    public void JsonParse(String URL1) {
        contactList = new ArrayList<LinkedHashMap<String, String>>();

        String logoLink = null;
        try {

            // instantiate our json parser
            JsonParser jParser = new JsonParser();
            final String TAG = "AsyncTaskParseJson.java";

            // set your json string url here
            String yourJsonStringUrl = URL1;
            System.out.println("Value of URL at Parsing class is " + URL1);

            // contacts JSONArray
            JSONArray dataJsonArr = null;

            // get json string from url
            JSONObject json = jParser.getJSONFromUrl(URL1);

            // get the array of users
            JSONObject responseData = json.getJSONObject("response");
            dataJsonArr = responseData.getJSONArray("venues");

            // loop through all users
            int len = dataJsonArr.length();
            if (len > 20) {
                len = 20;
            }
            for (int i = 0; i < len; i++) {

                JSONObject c = dataJsonArr.getJSONObject(i);

                // Storing each json item in variable
                String id = c.getString("id");
                String names = c.getString("name");

                JSONObject location = c.getJSONObject("location");

                JSONObject category = c.getJSONArray("categories").getJSONObject(0);
                String catName = category.getString("name");

                LinkedHashMap<String, String> value = new LinkedHashMap<String, String>();
                value.put("names", names);

                value.put("venueId", id);
                value.put("name", catName);

                boolean isContinue = false;

                for (String field: fields ) {
                    if (!location.has(field)) {
                        isContinue = true;
                        break;
                    }


                    String fieldValue = location.getString(field);

                    System.out.println("Name is " + names + " with id : " + id);

                    value.put(field, fieldValue);

                    System.out.println("Size id contactlist in Prasing " + contactList.size());
                }

                contactList.add(value);

                if (isContinue)
                    continue;


            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }

    }


    public class JsonParser {

        final String TAG = "JsonParser.java";

        InputStream is = null;
        JSONObject jObj = null;
        String json = "";

        public JSONObject getJSONFromUrl(String ul) {
            System.out.println("Inside jsonparser class");

            try {
                URL url = new URL(ul);
                URLConnection connection = url.openConnection();
                //connection.addRequestProperty("Referer", "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=%22mixorg.com%22&rsz=8");

                String line;
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    System.out.println(builder.toString());
                }
                json = builder.toString();

            } catch (Exception e) {
                Log.e(TAG, "Error converting result " + e.toString());
            }

            // try parse the string to a JSON object
            try {
                jObj = new JSONObject(json);
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing json data " + e.toString());
            } catch (Exception e) {

            }
            // return JSON String
            return jObj;
        }
    }
}