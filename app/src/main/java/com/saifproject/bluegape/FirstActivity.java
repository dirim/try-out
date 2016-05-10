package com.saifproject.bluegape;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.w3c.dom.Text;

import java.util.Random;


public class FirstActivity extends ActionBarActivity {
    private ViewFlipper myViewFlipper;
    private float initialXPoint;
    int[] image = { R.drawable.slide1, R.drawable.slide2,
            R.drawable.slide3, R.drawable.slide4};
    Toolbar toolbar;
    TextView txt;
    EditText edit;
    DrawerLayout mdrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        myViewFlipper = (ViewFlipper) findViewById(R.id.myflipper);
        mdrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout1);
        txt=(TextView)findViewById(R.id.text1);
        //Typeface typeface=Typeface.createFromAsset(getAssets(), "fonts/FlyBoyBB.ttf");
        //txt.setTypeface(typeface);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        edit=(EditText)findViewById(R.id.edit);

        for (int i = 0; i < image.length; i++) {
            ImageView imageView = new ImageView(FirstActivity.this);
            imageView.setImageResource(image[i]);
            myViewFlipper.addView(imageView);
        }
            Random ran=new Random();
            int n=ran.nextInt(100);
            if (n%2==0)
            {
                myViewFlipper.setInAnimation(FirstActivity.this, R.anim.view_transition_in_left);
                myViewFlipper.setOutAnimation(FirstActivity.this, R.anim.view_transition_out_left);
            }
            else
            {
                myViewFlipper.setInAnimation(FirstActivity.this, R.anim.view_transition_in_right);
                myViewFlipper.setOutAnimation(FirstActivity.this, R.anim.view_transition_out_right);
            }
            myViewFlipper.setAutoStart(true);
            myViewFlipper.setFlipInterval(3000);
            myViewFlipper.startFlipping();
    }
    public void show(View view)
    {
        String value=edit.getText().toString();
        if(value.startsWith("0")||value.startsWith("1")||value.startsWith("2")||value.startsWith("3")||value.startsWith("4")||value.startsWith("5")||value.startsWith("6")||value.startsWith("7")||value.startsWith("8")||value.startsWith("9"))
        {
            Snackbar.make(mdrawerLayout, "Wrong input..", Snackbar.LENGTH_SHORT)
                    .show();
        }
        else {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("val", value);
            startActivity(i);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first, menu);
        getMenuInflater().inflate(R.menu.menu_developer, menu);
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

            Intent i=new Intent(FirstActivity.this,offline.class);
            startActivity(i);
            return true;
        }
        else if (id == R.id.action_deve) {
            startActivity(new Intent(this,Developer.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
