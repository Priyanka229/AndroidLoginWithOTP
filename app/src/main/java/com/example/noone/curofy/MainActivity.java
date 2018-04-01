package com.example.noone.curofy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoginScreenFragment.LoginScreenCallback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginScreenFragment f = new LoginScreenFragment();
        f.setCallback(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, f)
                .commit();
    }



    @Override
    public void proceedButtonClick(String sessionID, String mobileNo, String countryName, String countryCode) {
        Bundle bundle = new Bundle();
        bundle.putString("session_id", sessionID);
        bundle.putString("mobile_no", mobileNo);
        bundle.putString("country_name", countryName);
        bundle.putString("country_code", countryCode);


        OTPScreenFragment f = new OTPScreenFragment();
        f.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, f)
                .addToBackStack(null)
                .commit();
    }
}

