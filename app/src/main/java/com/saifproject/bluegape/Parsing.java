package com.saifproject.bluegape;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class Parsing {

	Context context;

	ArrayList<LinkedHashMap<String, String>> contactList;

	public void JsonParse(String URL1){
		contactList = new ArrayList<LinkedHashMap<String, String>>();

		String logoLink=null;
		try {
			 
            // instantiate our json parser
            JsonParser jParser = new JsonParser();
            final String TAG = "AsyncTaskParseJson.java";

            // set your json string url here
            String yourJsonStringUrl = URL1;
			System.out.println("Value of URL at Parsing class is "+URL1);

            // contacts JSONArray
            JSONArray dataJsonArr = null;

            // get json string from url
            JSONObject json = jParser.getJSONFromUrl(URL1);

            // get the array of users
            JSONObject responseData = json.getJSONObject("response");
            dataJsonArr = responseData.getJSONArray("venues");

            // loop through all users
			int len=dataJsonArr.length();
			if(len>20)
			{
				len=20;
			}
            for (int i = 0; i < len; i++) {

                JSONObject c = dataJsonArr.getJSONObject(i);
                
                // Storing each json item in variable
				String id = c.getString("id");
                String names = c.getString("name");
				JSONObject location=c.getJSONObject("location") ;
				String address=location.getString("address");
				String lat=location.getString("lat");
				String lng=location.getString("lng");
				String distance=location.getString("distance");
				String city=location.getString("city");
				String state=location.getString("state");
				String country=location.getString("country");
				System.out.println("Name is " + names + " with id : " + id);

				LinkedHashMap<String, String> value = new LinkedHashMap<String, String>();

				value.put("id", id);
				value.put("names", names);
				value.put("address", address);
				value.put("lat", lat);
				value.put("lng", lng);
				value.put("distance", distance);
				value.put("city", city);
				value.put("state", state);
				value.put("country", country);

				contactList.add(value);
				System.out.println("Size od cintactlist in Prasing " + contactList.size());


			}

        } catch (JSONException e) {
            e.printStackTrace();
        }
		catch(Exception e){

		}

	}



	public class JsonParser {
	 
	    final String TAG = "JsonParser.java";
	 
	     InputStream is = null;
	     JSONObject jObj = null;
	     String json = "";
	 
	    public JSONObject getJSONFromUrl(String ul) {
	    	System.out.println("Inside jsonparser class");

	    try{	URL url = new URL(ul);
	URLConnection connection = url.openConnection();
	//connection.addRequestProperty("Referer", "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=%22mixorg.com%22&rsz=8");

	String line;
	StringBuilder builder = new StringBuilder();
	BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	while((line = reader.readLine()) != null) {
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
	        }
			catch (JSONException e)
			{
	            Log.e(TAG, "Error parsing json data " + e.toString());
	        }
			catch(Exception e){
	        	
	        }
	        // return JSON String
	        return jObj;
	    }
	}
}