package com.saurav.aisle.utils;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.saurav.aisle.R;


/**
 * Created by Developer on 8/11/2020.
 */

public class ValidationClass {

    private Context context;

    public ValidationClass(Context context) {
        this.context = context;


    }

    public boolean validateMobileNo(EditText editText) {
        if (editText.getText().toString().trim().isEmpty()) {
            Toast.makeText(context,
                    context.getResources().getString(R.string.msg_enter_mobile),
                    Toast.LENGTH_SHORT).show();
            editText.requestFocus();
            return false;
        } else
        if (editText.getText().toString().trim().length() < 10){
            Toast.makeText(context,
                    context.getResources().getString(R.string.msg_enter_10digitmobile),
                    Toast.LENGTH_SHORT).show();
            editText.requestFocus();
            return false;
        }

        return true;
    }


    public boolean validateOtp(EditText editText) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.requestFocus();
            editText.setError(context.getResources().getString(R.string.msg_otp));
            return false;
        }

        return true;
    }


}
