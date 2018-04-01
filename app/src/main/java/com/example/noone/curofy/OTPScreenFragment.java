package com.example.noone.curofy;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noone.curofy.network.MyRerotfitClient;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPScreenFragment extends Fragment {

    String mUserCountryName;
    String mUserCountryCode;
    String mUserMobileNo;
    String mUserSessionID;
    private ProgressBar mLoader;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserCountryCode = getArguments().getString("country_code");
        mUserCountryName = getArguments().getString("country_name");
        mUserMobileNo = getArguments().getString("mobile_no");
        mUserSessionID = getArguments().getString("session_id");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.otp_screen_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoader = view.findViewById(R.id.progress_bar);
        TextView textView = view.findViewById(R.id.sub_title_tv);

        textView.setText("We have sent a verification code on your number " + mUserMobileNo);

        final EditText editText1 = view.findViewById(R.id.editText1);
        final EditText editText2 = view.findViewById(R.id.editText2);
        final EditText editText3 = view.findViewById(R.id.editText3);
        final EditText editText4 = view.findViewById(R.id.editText4);


        editText1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText1.getText().toString().length() == 1)     //size as per your requirement
                {
                    editText2.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });

        editText2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText2.getText().toString().length() == 1)     //size as per your requirement
                {
                    editText3.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });

        editText3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText3.getText().toString().length() == 1)     //size as per your requirement
                {
                    editText4.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });

        editText4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText4.getText().toString().length() == 1)     //size as per your requirement
                {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });

        LinearLayout ll = view.findViewById(R.id.resend_ll);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeApiCallForResend();
            }
        });

        TextView verify_tv = view.findViewById(R.id.verify_tv);
        verify_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeApiCallForVerify();
            }
        });

    }

    private void makeApiCallForResend() {
        if (!TextUtils.isEmpty(mUserMobileNo) && !TextUtils.isEmpty(mUserCountryCode)){
            mLoader.setVisibility(View.VISIBLE);
            Map<String, String > mp = new HashMap<>();
            mp.put("mobile_no", mUserMobileNo);
            mp.put("country_code", mUserCountryCode);
            mp.put("country_name", mUserCountryName);

            Call<JsonObject> call = MyRerotfitClient.getRetrofitService().sendOTP(mp);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) { // success
                    mLoader.setVisibility(View.GONE);

                    int status = response.body().get("status").getAsInt();
                    if (status == 1){
                        mUserSessionID = response.body().getAsJsonObject("data").get("session_id").getAsString();
                        Toast.makeText(getActivity(), "OTP sent successfully", Toast.LENGTH_LONG).show();
                    } else if (status == 0) {
                        String msg = response.body().get("message").getAsString();

                        Toast.makeText(getActivity(),msg, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) { // failure
                    mLoader.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Invalid mobile number or country code", Toast.LENGTH_LONG).show();
        }
    }

    private void makeApiCallForVerify() {
        if (!TextUtils.isEmpty(mUserSessionID)) {
            String s1 = ((EditText)getView().findViewById(R.id.editText1)).getText().toString();
            String s2 = ((EditText)getView().findViewById(R.id.editText2)).getText().toString();
            String s3 = ((EditText)getView().findViewById(R.id.editText3)).getText().toString();
            String s4 = ((EditText)getView().findViewById(R.id.editText4)).getText().toString();

            String s5 = s1 + s2 + s3 + s4;

            if (!TextUtils.isEmpty(s5)) {

                mLoader.setVisibility(View.VISIBLE);

                Map<String, String> mp = new HashMap<>();
                mp.put("mobile_no", mUserMobileNo);
                mp.put("country_code", mUserCountryCode);
                mp.put("country_name", mUserCountryName);
                mp.put("session_id", mUserSessionID);
                mp.put("otp", s5);


                Call<JsonObject> call = MyRerotfitClient.getRetrofitService().verifyOTP(mp);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) { // success
                        mLoader.setVisibility(View.GONE);

                        ((EditText)getView().findViewById(R.id.editText1)).setText("");
                        ((EditText)getView().findViewById(R.id.editText2)).setText("");
                        ((EditText)getView().findViewById(R.id.editText3)).setText("");
                        ((EditText)getView().findViewById(R.id.editText4)).setText("");

                        ((EditText)getView().findViewById(R.id.editText1)).requestFocus();

                        int status = response.body().get("status").getAsInt();
                        if (status == 1) {
                            Toast.makeText(getActivity(), " Login Successfully", Toast.LENGTH_LONG).show();

                        } else if (status == 0) {
                            String msg = response.body().get("message").getAsString();

                            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) { // failure
                        mLoader.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Please enter valid OTP code", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }
}
