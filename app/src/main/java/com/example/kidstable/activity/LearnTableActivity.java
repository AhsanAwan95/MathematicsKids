package com.example.kidstable.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidstable.R;
import com.example.kidstable.adapter.NumberAdapter;
import com.example.kidstable.adapter.TableAdapter;
import com.example.kidstable.utils.ConnectionDetector;
import com.example.kidstable.utils.Constants;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import static com.example.kidstable.activity.SettingActivity.sendFeedback;
import static com.example.kidstable.utils.Constants.setDefaultLanguage;


public class LearnTableActivity extends AppCompatActivity implements NumberAdapter.setClick {

    RecyclerView table_recycler, number_recycler;
    NumberAdapter numberAdapter;
    TableAdapter tableAdapter;
    Button btn_play;
    MediaPlayer mp;
    AdView mAdView;
    boolean interstitialCanceled;
    InterstitialAd mInterstitialAd;
    ConnectionDetector cd;
    private int table_no = 1;
    Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultLanguage(this);
        setContentView(R.layout.learn_table);
        init();
        showbanner();
    }

    private void showbanner() {
        mAdView = findViewById(R.id.mAdView);
        // Forward Consent To AdMob
        Bundle extras = new Bundle();
        ConsentInformation consentInformation = ConsentInformation.getInstance(LearnTableActivity.this);
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

    @Override
    public void onBackPressed() {
        if (mp != null) {
            mp.release();
        }
        Intent intent = new Intent(LearnTableActivity.this, DashboardActivity.class);
        startActivity(intent);
    }

    private void init() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp != null) {
                    mp.release();
                }
                Intent intent = new Intent(LearnTableActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
        mp = MediaPlayer.create(this, R.raw.click);
        btn_play = findViewById(R.id.btn_play);
        table_recycler = findViewById(R.id.table_recycler);
        number_recycler = findViewById(R.id.number_recycler);
        activity = this;

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 6);
        number_recycler.setLayoutManager(layoutManager);

        numberAdapter = new NumberAdapter(getApplicationContext());
        number_recycler.setAdapter(numberAdapter);
        numberAdapter.setClickListener(this);
        numberAdapter.setSelectedPos(0);

        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(getApplicationContext(), 2);
        table_recycler.setLayoutManager(layoutManager1);

        tableAdapter = new TableAdapter(getApplicationContext(), 1);
        table_recycler.setAdapter(tableAdapter);

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!interstitialCanceled) {
                if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    ContinueIntent();
                }
                    }

            }
        });


    }

    private void ContinueIntent() {
        startSound();
        Intent intent = new Intent(LearnTableActivity.this, QuizActivity.class);
        intent.putExtra(Constants.TABLE_NO, table_no);
        startActivity(intent);
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

    @Override
    public void onTableNoClick(int pos) {
        numberAdapter.setSelectedPos((pos - 1));
        tableAdapter.setTableNo(pos);
        table_no = pos;
        startSound();
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



    public void share() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Xyz");
        share.putExtra(Intent.EXTRA_TEXT, getString(R.string.SHARE_APP_LINK)
                + getPackageName());
        startActivity(Intent.createChooser(share, "Share Link!"));

    }

    @Override
    protected void onResume() {
        super.onResume();
        interstitialCanceled = false;
        if (getResources().getString(R.string.ADS_VISIBILITY).equals("YES")) {
            CallNewInsertial();
        }
    }

    @Override
    protected void onPause() {
        mInterstitialAd = null;
        interstitialCanceled = true;
        super.onPause();
    }

    private void requestNewInterstitial() {
//        AdRequest adRequest = new AdRequest.Builder()
//                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .addTestDevice(ConsentDialogueConstant.TEST_DEVICE_ID)
//                .build();
//        interstitial.loadAd(adRequest);
        try {
            // Forward Consent To AdMob
            Bundle extras = new Bundle();
            ConsentInformation consentInformation = ConsentInformation.getInstance(LearnTableActivity.this);
            if (consentInformation.getConsentStatus().equals(ConsentStatus.NON_PERSONALIZED)) {
                extras.putString("npa", "1");
            }
            AdRequest adRequest = new AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                    .build();
            mInterstitialAd.loadAd(adRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CallNewInsertial() {
//        // Forward Consent To AdMob
//        Bundle extras = new Bundle();
//        ConsentInformation consentInformation = ConsentInformation.getInstance(LearnTableActivity.this);
//        if (consentInformation.getConsentStatus().equals(ConsentStatus.NON_PERSONALIZED)) {
//            extras.putString("npa", "1");
//        }
        cd = new ConnectionDetector(LearnTableActivity.this);
        if (cd.isConnectingToInternet()) {
            mInterstitialAd = new InterstitialAd(LearnTableActivity.this);
            mInterstitialAd.setAdUnitId(getString(R.string.InterstitialAds));
            requestNewInterstitial();
            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdClosed() {
                    ContinueIntent();
                }
            });
        }
    }
}
