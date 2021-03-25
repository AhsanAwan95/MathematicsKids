package com.example.kidstable.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidstable.R;
import com.example.kidstable.adapters.MainAdapter;
import com.example.kidstable.utils.Constants;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import java.util.Locale;

import static com.example.kidstable.activity.SettingActivity.sendFeedback;
import static com.example.kidstable.utils.Constants.setDefaultLanguage;


public class PythagoranActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, MainAdapter.speakText {

    RecyclerView recyclerView;
    TextView textView;
    private TextToSpeech tts;
    MainAdapter mainAdapter;
    int item_size = 10;
    private MediaPlayer mp;
    AdView mAdView;
    Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultLanguage(this);
        setContentView(R.layout.activity_pythagoran);
        init();
        showbanner();
    }

    private void showbanner() {
        mAdView = findViewById(R.id.mAdView);
        // Forward Consent To AdMob
        Bundle extras = new Bundle();
        ConsentInformation consentInformation = ConsentInformation.getInstance(PythagoranActivity.this);
        if (consentInformation.getConsentStatus().equals(ConsentStatus.NON_PERSONALIZED)) {
            extras.putString("npa", "1");
        }
        if (getResources().getString(R.string.ADS_VISIBILITY).equals("YES")) {
            AdRequest adRequest = new AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                    .build();
            mAdView.loadAd(adRequest);
        }
    }

    private void startSound() {
        if (Constants.getSound(getApplicationContext())) {
            if (mp != null) {
                mp.release();
            }
            mp = MediaPlayer.create(this, R.raw.click);
            mp.start();
        }
    }

    @Override
    public void onBackPressed() {
        backIntent();
    }

    public void backIntent() {
        if (mp != null) {
            mp.release();
        }
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    private void init() {
        mp = MediaPlayer.create(this, R.raw.click);

        tts = new TextToSpeech(this, this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSound();
                backIntent();
            }
        });
        textView = findViewById(R.id.textView);
        activity = this;


        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int width = displayMetrics.widthPixels;
        item_size = 12;
        mainAdapter = new MainAdapter(getApplicationContext(), width, item_size);
        recyclerView.setAdapter(mainAdapter);
        mainAdapter.setSpeakListener(PythagoranActivity.this);

        mainAdapter.getTableNo(5, 42, 7);
        textView.setText(6 + " x " + 7 + " = " + 42);
    }


    private void speakOut(String s) {
        if (!TextUtils.isEmpty(s)) {
            tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


    @Override
    public void onSpeakText(String speakString, String printString, int position) {
        textView.setText(printString);
        speakOut(speakString);
        String[] separated = printString.split("=");
        String[] separated1 = printString.split("x");
        String[] separated2 = separated1[1].split("=");

        mainAdapter.getTableNo(position, Integer.parseInt(separated[1].trim()), Integer.parseInt(separated2[0].trim()));

    }


    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {


            int result = tts.setLanguage(Locale.getDefault());


            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            } else {
                speakOut("");
            }
        } else {
            Toast.makeText(getBaseContext(), "TTS Engine Initilization Failed!", Toast.LENGTH_SHORT).show();
        }
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
