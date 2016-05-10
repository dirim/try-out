package com.saifproject.term;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.foursquare.android.nativeoauth.FoursquareCancelException;
import com.foursquare.android.nativeoauth.FoursquareDenyException;
import com.foursquare.android.nativeoauth.FoursquareInvalidRequestException;
import com.foursquare.android.nativeoauth.FoursquareOAuth;
import com.foursquare.android.nativeoauth.FoursquareOAuthException;
import com.foursquare.android.nativeoauth.FoursquareUnsupportedVersionException;
import com.foursquare.android.nativeoauth.model.AccessTokenResponse;
import com.foursquare.android.nativeoauth.model.AuthCodeResponse;

import java.util.Random;


public class FirstActivity extends ActionBarActivity {
    private static final int REQUEST_CODE_FSQ_CONNECT = 200;
    private static final int REQUEST_CODE_FSQ_TOKEN_EXCHANGE = 201;

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
        ensureUi();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_FSQ_CONNECT:
                onCompleteConnect(resultCode, data);
                break;

            case REQUEST_CODE_FSQ_TOKEN_EXCHANGE:
                onCompleteTokenExchange(resultCode, data);
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Update the UI. If we already fetched a token, we'll just show a success
     * message.
     */
    private void ensureUi() {
        boolean isAuthorized = !TextUtils.isEmpty(ExampleTokenStore.get().getToken());
//
//        TextView tvMessage = (TextView) findViewById(R.id.tvMessage);
//        tvMessage.setVisibility(isAuthorized ? View.VISIBLE : View.GONE);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setVisibility(isAuthorized ? View.GONE : View.VISIBLE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the native auth flow.
                Intent intent = FoursquareOAuth.getConnectIntent(FirstActivity.this, MainActivity.CLIENT_ID);

                // If the device does not have the Foursquare app installed, we'd
                // get an intent back that would open the Play Store for download.
                // Otherwise we start the auth flow.
                if (FoursquareOAuth.isPlayStoreIntent(intent)) {
                    toastMessage(FirstActivity.this, "yukle sunu amk");
                    startActivity(intent);
                } else {
                    startActivityForResult(intent, REQUEST_CODE_FSQ_CONNECT);
                }
            }
        });
    }

    private void onCompleteConnect(int resultCode, Intent data) {
        AuthCodeResponse codeResponse = FoursquareOAuth.getAuthCodeFromResult(resultCode, data);
        Exception exception = codeResponse.getException();

        if (exception == null) {
            // Success.
            String code = codeResponse.getCode();
            performTokenExchange(code);

        } else {
            if (exception instanceof FoursquareCancelException) {
                // Cancel.
                toastMessage(this, "Canceled");

            } else if (exception instanceof FoursquareDenyException) {
                // Deny.
                toastMessage(this, "Denied");

            } else if (exception instanceof FoursquareOAuthException) {
                // OAuth error.
                String errorMessage = exception.getMessage();
                String errorCode = ((FoursquareOAuthException) exception).getErrorCode();
                toastMessage(this, errorMessage + " [" + errorCode + "]");

            } else if (exception instanceof FoursquareUnsupportedVersionException) {
                // Unsupported Fourquare app version on the device.
                toastError(this, exception);

            } else if (exception instanceof FoursquareInvalidRequestException) {
                // Invalid request.
                toastError(this, exception);

            } else {
                // Error.
                toastError(this, exception);
            }
        }
    }

    private void onCompleteTokenExchange(int resultCode, Intent data) {
        AccessTokenResponse tokenResponse = FoursquareOAuth.getTokenFromResult(resultCode, data);
        Exception exception = tokenResponse.getException();

        if (exception == null) {
            String accessToken = tokenResponse.getAccessToken();
            // Success.
            toastMessage(this, "Access token: " + accessToken);
            System.out.println("TOKEN: " + accessToken);

            // Persist the token for later use. In this example, we save
            // it to shared prefs.
            ExampleTokenStore.get().setToken(accessToken);

            // Refresh UI.
            ensureUi();

        } else {
            if (exception instanceof FoursquareOAuthException) {
                // OAuth error.
                String errorMessage = ((FoursquareOAuthException) exception).getMessage();
                String errorCode = ((FoursquareOAuthException) exception).getErrorCode();
                toastMessage(this, errorMessage + " [" + errorCode + "]");

            } else {
                // Other exception type.
                toastError(this, exception);
            }
        }
    }

    /**
     * Exchange a code for an OAuth Token. Note that we do not recommend you
     * do this in your app, rather do the exchange on your server. Added here
     * for demo purposes.
     *
     * @param code
     *          The auth code returned from the native auth flow.
     */
    private void performTokenExchange(String code) {
        Intent intent = FoursquareOAuth.getTokenExchangeIntent(this, MainActivity.CLIENT_ID, MainActivity.CLIENT_SECRET, code);
        startActivityForResult(intent, REQUEST_CODE_FSQ_TOKEN_EXCHANGE);
    }

    public static void toastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void toastError(Context context, Throwable t) {
        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
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
