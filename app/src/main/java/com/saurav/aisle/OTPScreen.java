package com.saurav.aisle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.saurav.aisle.utils.ApiClient;
import com.saurav.aisle.utils.ConnectivityReceiver;
import com.saurav.aisle.utils.GlobalClass;
import com.saurav.aisle.utils.ValidationClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


/**
 * Created by Saurav on 8/11/2020.
 **/
public class OTPScreen extends AppCompatActivity {

    MaterialButton button;
    EditText et_otp;
    ImageView iv_edit;
    MaterialTextView tv_phone_number;

    ProgressDialog progressDialog;
    GlobalClass globalClass;
    ValidationClass validationClass;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        button = findViewById(R.id.bt_continue_otp);
        et_otp = findViewById(R.id.et_otp);
        iv_edit = findViewById(R.id.iv_edit);
        tv_phone_number = findViewById(R.id.tv_phone_number);

        globalClass = (GlobalClass) getApplicationContext();
        validationClass = new ValidationClass(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            number = bundle.getString("Number");
        }

        tv_phone_number.setText(number);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validationCheck();

            }
        });

        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OTPScreen.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

    }


    private void validationCheck() {


        if (!validationClass.validateOtp(et_otp)){
            return;
        }


        if (ConnectivityReceiver.isConnected()) {

            validate();

        } else {

            Toast.makeText(getApplicationContext(),
                    "Please connect to internet",
                    Toast.LENGTH_LONG).show();
        }


    }

    private void validate() {

        progressDialog.show();

        String URL=null;
        try {
            URL = ApiClient.BASE_URL + ApiClient.verify_otp;
        }catch(Exception e){}

        Log.e(ApiClient.TAG, "URL = " + URL);

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        StringEntity jsonEntity = null;
        asyncHttpClient.addHeader("Accept", "application/json");
        asyncHttpClient.addHeader("__cfduid","df9b865983bd04a5de2cf5017994bbbc71618565720");

        Log.d(ApiClient.TAG, "Login- " + URL);

        try {

            jsonEntity = new StringEntity(getJsonString1());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int DEFAULT_TIMEOUT = 30 * 1000;
        asyncHttpClient.setMaxRetriesAndTimeout(5, DEFAULT_TIMEOUT);
        asyncHttpClient.setTimeout(DEFAULT_TIMEOUT);
        asyncHttpClient.post(this, URL, jsonEntity, "application/json",
                new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(ApiClient.TAG, "response = " + response);
                if (response != null) {

                    try {

                        progressDialog.dismiss();
                        String token = response.optString("token");
                        Intent intent = new Intent(OTPScreen.this, NotesActivity.class);
                        intent.putExtra("token",token);
                        startActivity(intent);
                        finish();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Toast.makeText(getApplicationContext(),
                        "Please provide correct Number",
                        Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d(ApiClient.TAG, "statusCode = " + statusCode);
                Log.d(ApiClient.TAG, "errorResponse = " + errorResponse);

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    private String getJsonString1(){
        String json = "";

        try {

            JSONObject object = new JSONObject();
            object.put("number",number);
            object.put("otp",et_otp.getText().toString());

            json = object.toString();

        }catch (Exception e){
            e.printStackTrace();
        }

        return json;

    }


}