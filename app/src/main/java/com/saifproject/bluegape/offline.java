package com.saifproject.bluegape;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;


public class offline extends ActionBarActivity {

    String data="";
    MYDBHandler mydbHandler;
    TextView txt;
    ArrayList<String> arrayList;
    String str="";
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        mydbHandler=new MYDBHandler(this,null,null,1);
        txt=(TextView)findViewById(R.id.text12);
        arrayList=mydbHandler.show();
        System.out.println("Value of arralist in offline is "+arrayList.size());
        for(int i=0;i<arrayList.size();i++)
        {
            str=str+arrayList.get(i);
        }
        txt.setText(str);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_offline, menu);
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
}
