package com.example.noone.curofy;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;


public class OTP implements TextWatcher {

    private View view;
    public OTP(View view)
    {
        this.view = view;
    }

    EditText et1,et2,et3,et4;

    public void setEt1(EditText et1) {
        this.et1 = et1;
    }

    public void setEt2(EditText et2) {
        this.et2 = et2;
    }

    public void setEt3(EditText et3) {
        this.et3 = et3;
    }

    public void setEt4(EditText et4) {
        this.et4 = et4;
    }

    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        switch(view.getId())
        {

            case R.id.editText1:
                if(text.length()==1)
                    et2.requestFocus();
                break;
            case R.id.editText2:
                if(text.length()==1)
                    et3.requestFocus();
                break;
            case R.id.editText3:
                if(text.length()==1)
                    et4.requestFocus();
                break;
            case R.id.editText4:
                break;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }
}
