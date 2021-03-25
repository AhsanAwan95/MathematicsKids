package com.example.kidstable.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.kidstable.R;
import com.example.kidstable.fragments.MainQuizFragment;
import com.example.kidstable.modelclasses.Model;
import com.example.kidstable.utils.Constants;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;

import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import static com.example.kidstable.activity.SettingActivity.sendFeedback;
import static com.example.kidstable.utils.Constants.MyPref;
import static com.example.kidstable.utils.Constants.setDefaultLanguage;
import static com.example.kidstable.utils.Constants.setTime;


public class SingleQuizTypeActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ProgressDialog progressDialog;
    String level_type;
    MediaPlayer mp;
    AdView mAdView;
    public  static TextView txt_header;
    Activity activity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultLanguage(this);
        setContentView(R.layout.activity_single_type);
        init();
       // showbanner();

    }

    private void showbanner() {
        mAdView = findViewById(R.id.mAdView);
        // Forward Consent To AdMob
        Bundle extras = new Bundle();
        ConsentInformation consentInformation = ConsentInformation.getInstance(SingleQuizTypeActivity.this);
        if (consentInformation.getConsentStatus().equals(ConsentStatus.NON_PERSONALIZED)) {
            extras.putString("npa", "1");
        }
        if (getResources().getString(R.string.ADS_VISIBILITY).equals("YES")) {

        }
    }


    public void startSound() {
        if (Constants.getSound(getApplicationContext())) {
            if (mp != null) {
                mp.release();
            }
            mp = MediaPlayer.create(this, R.raw.click);
            mp.start();
        }
    }


    public void backIntent() {
        if(mp!=null){
            mp.release();
        }
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        backIntent();
    }

    private void init() {
        mp = MediaPlayer.create(this, R.raw.click);
        progressDialog = new ProgressDialog(this);

        level_type = getIntent().getStringExtra(Constants.LEVEL_TYPE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSound();
                backIntent();
            }
        });
        activity = this;
        txt_header= findViewById(R.id.txt_header);
        txt_header.setText(getString(R.string.easy));

        if (Constants.geLevelData(getApplicationContext(), getString(R.string.easy)).size() <= 0) {
            new AddLevel().execute();
        } else {
            setFragment();
        }


    }

    public void addData() {


        for (int i = 0; i < 10; i++) {
            SharedPreferences sharedPreferences = getSharedPreferences(MyPref, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Model model;
            if (i == 0) {
                model = new Model((i + 1), true, false, getString(R.string.easy));
            } else {
                model = new Model((i + 1), false, false, getString(R.string.easy));
            }


            Gson gson = new Gson();
            String json = gson.toJson(model);
            editor.putString(getString(R.string.easy) + i, json);
            editor.apply();

        }

        for (int i = 0; i < 10; i++) {
            SharedPreferences sharedPreferences = getSharedPreferences(MyPref, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Model model;
            if (i == 0) {
                model = new Model((i + 1), true, false, getString(R.string.medium));
            } else {
                model = new Model((i + 1), false, false, getString(R.string.medium));
            }

            Gson gson = new Gson();
            String json = gson.toJson(model);
            editor.putString(getString(R.string.medium) + i, json);
            editor.apply();

        }


        for (int i = 0; i < 10; i++) {
            SharedPreferences sharedPreferences = getSharedPreferences(MyPref, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Model model;
            if (i == 0) {
                model = new Model((i + 1), true, false, getString(R.string.hard));
            } else {
                model = new Model((i + 1), false, false, getString(R.string.hard));
            }

            Gson gson = new Gson();
            String json = gson.toJson(model);
            editor.putString(getString(R.string.hard) + i, json);
            editor.apply();

        }


    }


    public class AddLevel extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            progressDialog.setMessage(getString(R.string.please_wait));
        }

        @Override
        protected String doInBackground(Void... voids) {
            addData();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            setTime(getApplicationContext(), 0, getString(R.string.easy));
            setTime(getApplicationContext(), 0, getString(R.string.medium));
            setTime(getApplicationContext(), 0, getString(R.string.hard));
            setFragment();
        }
    }


    public void setFragment() {

        Bundle bundle = new Bundle();
        bundle.putString(Constants.LEVEL_TYPE, level_type);
        MainQuizFragment mainFragment = new MainQuizFragment();
        mainFragment.setArguments(bundle);



        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, mainFragment).commit();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_feedback) {
            sendFeedback(activity);
        } else if (item.getItemId() == R.id.menu_share) {
            share();
        } else if (item.getItemId() == R.id.menu_rate) {
            final String appPackageName = getPackageName();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendFeedBack() {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(getString(R.string.feedback_mail) +
                    "?subject=" + Uri.encode(getPackageName())));
            startActivity(intent);
        } catch (Exception ignored) {
        }
    }


    public void share() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Xyz");
        share.putExtra(Intent.EXTRA_TEXT, getString(R.string.SHARE_APP_LINK)
                + getPackageName());
        startActivity(Intent.createChooser(share, "Share Link!"));

    }


}
