package com.example.choi7.item_app;
/**
 * mainactivity
 */
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import me.pushy.sdk.Pushy;


public class MainActivity extends AppCompatActivity {
    TextView mInstructions;
    TextView mDeviceToken;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout rl1 = (RelativeLayout) findViewById(R.id.rl1);
        RelativeLayout rl2 = (RelativeLayout) findViewById(R.id.rl2);
        RelativeLayout rl3 = (RelativeLayout) findViewById(R.id.rl3);
        RelativeLayout rl4 = (RelativeLayout) findViewById(R.id.rl4);

        ImageView iv2=(ImageView)findViewById(R.id.iv2);
        ImageView iv3=(ImageView)findViewById(R.id.iv3);

        ImageView iv5=(ImageView)findViewById(R.id.iv5);
        ImageView iv6=(ImageView)findViewById(R.id.iv6);

        ImageView iv8=(ImageView)findViewById(R.id.iv8);
        ImageView iv9=(ImageView)findViewById(R.id.iv9);

        ImageView iv11=(ImageView)findViewById(R.id.iv11);
        ImageView iv12=(ImageView)findViewById(R.id.iv12);

        // Cache TextView objects
        mDeviceToken = findViewById(R.id.deviceToken);
        mInstructions = findViewById(R.id.instructions);

        TranslateAnimation anim = new TranslateAnimation(900, 0, 0, 0);
        anim.setDuration(700);
        rl1.startAnimation(anim);

        TranslateAnimation anim2 = new TranslateAnimation(1000, 0, 0, 0);
        anim2.setDuration(800);
        rl2.startAnimation(anim2);

        TranslateAnimation anim3 = new TranslateAnimation(1100, 0, 0, 0);
        anim3.setDuration(900);
        rl3.startAnimation(anim3);

        TranslateAnimation anim4 = new TranslateAnimation(1200, 0, 0, 0);
        anim4.setDuration(1000);
        rl4.startAnimation(anim4);

        ScaleAnimation scale_anim1=new ScaleAnimation(1,1,0,1);
        scale_anim1.setDuration(1000);
        iv2.startAnimation(scale_anim1);
        iv3.startAnimation(scale_anim1);

        ScaleAnimation scale_anim2=new ScaleAnimation(1,1,0,1);
        scale_anim2.setDuration(1150);
        iv5.startAnimation(scale_anim2);
        iv6.startAnimation(scale_anim2);

        ScaleAnimation scale_anim3=new ScaleAnimation(1,1,0,1);
        scale_anim3.setDuration(1300);
        iv8.startAnimation(scale_anim3);
        iv9.startAnimation(scale_anim3);

        ScaleAnimation scale_anim4=new ScaleAnimation(1,1,0,1);
        scale_anim4.setDuration(1400);
        iv11.startAnimation(scale_anim4);
        iv12.startAnimation(scale_anim4);

       View.OnClickListener listener = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent rl1_it = new Intent(MainActivity.this,Layout_one.class);
                startActivity(rl1_it);

            }
        };

        rl1.setOnClickListener(listener);


        View.OnClickListener listener2 = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent rl2_it = new Intent(MainActivity.this,Layout_two.class);
                startActivity(rl2_it);

            }
        };

        rl2.setOnClickListener(listener2);

        View.OnClickListener listener3 = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent rl3_it = new Intent(MainActivity.this,Layout_three.class);
                startActivity(rl3_it);

            }
        };

        rl3.setOnClickListener(listener3);

        View.OnClickListener listener4 = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent rl4_it = new Intent(MainActivity.this,Layout_four.class);
                startActivity(rl4_it);
            }
        };

        rl4.setOnClickListener(listener4);

        // Not registered yet?
        if (getDeviceToken() == null) {
            // Register with Pushy
            new RegisterForPushNotificationsAsync().execute();
        }
        else {
            // Start Pushy notification service if not already running
            Pushy.listen(this);

            // Update UI with device token
            updateUI();

        }
    }

    private class RegisterForPushNotificationsAsync extends AsyncTask<String, Void, Exception> {
        ProgressDialog mLoading;

        public RegisterForPushNotificationsAsync() {
            // Create progress dialog and set it up
            mLoading = new ProgressDialog(MainActivity.this);
            //mLoading.setMessage(getString(R.string.registeringDevice));
            mLoading.setCancelable(false);

            // Show it
            mLoading.show();
        }

        @Override
        protected Exception doInBackground(String... params) {
            try {
                // Assign a unique token to this device
                String deviceToken = Pushy.register(MainActivity.this);

                // Save token locally / remotely
                saveDeviceToken(deviceToken);
            }
            catch (Exception exc) {
                // Return exc to onPostExecute
                return exc;
            }

            // Success
            return null;
        }

        @Override
        protected void onPostExecute(Exception exc) {
            // Activity died?
            if (isFinishing()) {
                return;
            }

            // Hide progress bar
            mLoading.dismiss();

            // Registration failed?
            if (exc != null) {
                // Write error to logcat
                Log.e("Pushy", "Registration failed: " + exc.getMessage());

                // Display error dialog
                new AlertDialog.Builder(MainActivity.this).setTitle(R.string.registrationError)
                        .setMessage(exc.getMessage())
                        .setPositiveButton(R.string.ok, null)
                        .create()
                        .show();
            }

            // Update UI with registration result
            updateUI();
        }
    }

    private void updateUI() {
        // Get device token from SharedPreferences
        String deviceToken = getDeviceToken();

        // Registration failed?
        if (deviceToken == null) {
            // Display registration failed in app UI
            mInstructions.setText(R.string.restartApp);
            mDeviceToken.setText(R.string.registrationFailed);

            // Stop execution
            return;
        }

        // Display device token
        mDeviceToken.setText(deviceToken);

        // Display "copy from logcat" instructions
        mInstructions.setText(R.string.copyLogcat);

        // Write device token to logcat
        Log.d("Pushy", "Device token: " + deviceToken);
    }

    private String getDeviceToken() {
        // Get token stored in SharedPreferences
        return getSharedPreferences().getString("deviceToken", null);
    }

    private void saveDeviceToken(String deviceToken) {
        // Save token locally in app SharedPreferences
        getSharedPreferences().edit().putString("deviceToken", deviceToken).commit();

        // Your app should store the device token in your backend database
        //new URL("https://{YOUR_API_HOSTNAME}/register/device?token=" + deviceToken).openConnection();
    }

    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

}