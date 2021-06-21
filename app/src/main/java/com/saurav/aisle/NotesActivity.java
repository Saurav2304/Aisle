package com.saurav.aisle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.saurav.aisle.utils.ApiClient;
import com.saurav.aisle.utils.GlobalClass;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.BlurTransformation;


public class NotesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView imageView;
    ImageView imageView1;
    ImageView imageView2;
    BottomNavigationView bottom;




    Fragment fragment = null;
    ProgressDialog progressDialog;
    GlobalClass globalClass;
    String token;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        initViews();

    }

    public void initViews(){

        globalClass = (GlobalClass) getApplicationContext();


        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        imageView = findViewById(R.id.imageView);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        bottom = findViewById(R.id.bottom_navigation);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            token = bundle.getString("token");
        }

        TestAPI(token);

        bottom.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        bottom.getOrCreateBadge(R.id.notes).setNumber(9);
        bottom.getOrCreateBadge(R.id.matches).setNumber(50);
        bottom.getOrCreateBadge(R.id.notes).setBackgroundColor(getResources().getColor(R.color.violet));
        bottom.getOrCreateBadge(R.id.matches).setBackgroundColor(getResources().getColor(R.color.violet));



    }

    private void TestAPI(String token){

        if (!progressDialog.isShowing())
            progressDialog.show();

        String URL = ApiClient.BASE_URL+ApiClient.test_profile_list;

        Log.e(ApiClient.TAG, "Rating = "+URL);

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("__cfduid","df9b865983bd04a5de2cf5017994bbbc71618565720");
        client.addHeader("Authorization",token);
        int DEFAULT_TIMEOUT = 20 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        client.setTimeout(DEFAULT_TIMEOUT);
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(ApiClient.TAG, "Response = "+response);
                if (response != null){

                    try {
                        Picasso.get().load(R.drawable.photo_1)
                                .centerCrop()
                                .fit()
                                .error(R.drawable.photo_1)
                                .into(imageView);
                        Picasso.get().load(R.drawable.photo_2)
                                .centerCrop()
                                .fit()
                                .transform(new BlurTransformation(getApplicationContext(), 25, 1))
                                .into(imageView1);
                        Picasso.get().load(R.drawable.photo_3)
                                .centerCrop()
                                .fit()
                                .transform(new BlurTransformation(getApplicationContext(), 25, 1))
                                .into(imageView2);

                        progressDialog.dismiss();

                    }catch (Exception e){
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }else{

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
                Log.d(ApiClient.TAG, "statusCode = "+statusCode);
                Log.d(ApiClient.TAG, "errorResponse = "+errorResponse);
                //  progressDialog.dismiss();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.

        fragment = null;
        Intent intent1 = null;

        switch (item.getItemId()){

            case R.id.discover:

                break;

            case R.id.notes:

                break;
            case R.id.matches:

                break;

            case R.id.profile:

                break;

        }


        item.setChecked(true);
        return true;
    }

    private void setMyIntent(final Intent intent){

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {

                if (intent != null) {

                    startActivity(intent);

                }

            }
        };
        handler.postDelayed(runnable, 300);
    }

    /////////////////////

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        moveTaskToBack(true);
    }

}
