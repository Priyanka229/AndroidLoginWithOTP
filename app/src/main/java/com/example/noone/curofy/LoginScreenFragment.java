package com.example.noone.curofy;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noone.curofy.network.MyRerotfitClient;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginScreenFragment extends Fragment {

    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    SliderPagerAdapter sliderPagerAdapter;
    ArrayList<Integer> slider_image_list;
    private TextView[] dots;
    int page_position = 0;

    LoginScreenCallback mLoginScreenCallback;
    private ProgressBar mLoader;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Spinner spinner = view.findViewById(R.id.spinner);
        final Spinner spinner_code = view.findViewById(R.id.spinner_code);
        mLoader = view.findViewById(R.id.progress_bar);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                spinner_code.setSelection(position);

                // Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                String item = adapterView.getItemAtPosition(position).toString();
                spinner.setSelection(position);
                //Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        List<String> country = new ArrayList<String>();
        country.add("India");
        country.add("Austria");
        country.add("United Kingdom");
        country.add("Pakistan");
        country.add("Sri Lanka");
        country.add("Singapore");

        List<String> code = new ArrayList<String>();
        code.add("+91");
        code.add("+61");
        code.add("+44");
        code.add("+92");
        code.add("+94");
        code.add("+65");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, country);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        ArrayAdapter<String> dataAdapterCode = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, code);
        dataAdapterCode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_code.setAdapter(dataAdapterCode);

        init();

        addBottomDots(0);

        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == slider_image_list.size()) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                vp_slider.setCurrentItem(page_position, true);
            }
        };

        TextView textView = view.findViewById(R.id.rl_tv1);
        final EditText editText = view.findViewById(R.id.number_etv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeApiCall();
            }
        });
    }

    private void init() {

        vp_slider =  getView().findViewById(R.id.view_pager);
        ll_dots =  getView().findViewById(R.id.pager_dots);

        slider_image_list = new ArrayList<>();

        slider_image_list.add(R.drawable.tour_image);
        slider_image_list.add(R.drawable.tour_image);
        slider_image_list.add(R.drawable.tour_image);
        slider_image_list.add(R.drawable.tour_image  );


        sliderPagerAdapter = new SliderPagerAdapter(getActivity(), slider_image_list);
        vp_slider.setAdapter(sliderPagerAdapter);

        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_list.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#000000"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#FFFFFF"));
    }

    public interface LoginScreenCallback {
        void proceedButtonClick(String sessionID, String mobileNo, String countryName, String countryCode);
    }

    public void setCallback(LoginScreenCallback callback){
        mLoginScreenCallback = callback;

    }

    private void makeApiCall() {

        EditText mobileNumberET = getView().findViewById(R.id.number_etv);
        Spinner countryNameSP = getView().findViewById(R.id.spinner);
        Spinner countryCodeSP = getView().findViewById(R.id.spinner_code);


        final String mobileNo = mobileNumberET.getText().toString();
        final String countryName = countryNameSP.getSelectedItem().toString();
        final String countryCode = countryCodeSP.getSelectedItem().toString();

        if (!TextUtils.isEmpty(mobileNo) && !TextUtils.isEmpty(countryCode)){
            mLoader.setVisibility(View.VISIBLE);

            Map<String, String > mp = new HashMap<>();
            mp.put("mobile_no", mobileNo);
            mp.put("country_code", countryCode);
            mp.put("country_name", countryName);

            Call<JsonObject> call = MyRerotfitClient.getRetrofitService().sendOTP(mp);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) { // success
                    mLoader.setVisibility(View.GONE);

                    int status = response.body().get("status").getAsInt();
                    if (status == 1){
                        String sessionID = response.body().getAsJsonObject("data").get("session_id").getAsString();

                        mLoginScreenCallback.proceedButtonClick(sessionID, mobileNo, countryName, countryCode);

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

}
