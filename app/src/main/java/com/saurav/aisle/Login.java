package com.saurav.aisle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
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
public class Login extends AppCompatActivity {

    MaterialButton button;
    EditText editNumber;
    EditText et_prefix;

    ProgressDialog progressDialog;
    GlobalClass globalClass;
    ValidationClass validationClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        button = findViewById(R.id.bt_continue);
        editNumber = findViewById(R.id.et_number);
        et_prefix = findViewById(R.id.et_prefix);

        globalClass = (GlobalClass) getApplicationContext();
        validationClass = new ValidationClass(this);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validationCheck();

            }
        });

    }


    private void validationCheck() {


        if (!validationClass.validateMobileNo(editNumber)){
            return;
        }


        if (ConnectivityReceiver.isConnected()) {

            login();

        } else {

            Toast.makeText(getApplicationContext(),
                    "Please connect to internet",
                    Toast.LENGTH_LONG).show();
        }


    }

    private void login() {

        progressDialog.show();

        String URL=null;
        try {
            URL = ApiClient.BASE_URL + ApiClient.phone_number_login;
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
                        Boolean status = response.optBoolean("status");
                        if(status.equals(true)){
                            Intent intent = new Intent(Login.this, OTPScreen.class);
                            intent.putExtra("Number",et_prefix.getText().toString()+editNumber.getText().toString());
                            startActivity(intent);
                            finish();
                        }

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
            object.put("number",et_prefix.getText().toString()+editNumber.getText().toString());

            json = object.toString();

        }catch (Exception e){
            e.printStackTrace();
        }

        return json;

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


}