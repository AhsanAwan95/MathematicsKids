package com.example.kidstable.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidstable.R;
import com.example.kidstable.adapters.NumberAdapter;
import com.example.kidstable.adapters.TableAdapter;
import com.example.kidstable.databinding.LearnTableBinding;
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


public class TableActivity extends AppCompatActivity implements NumberAdapter.setClick {

    NumberAdapter numberAdapter;
    TableAdapter tableAdapter;
    MediaPlayer mp;
    AdView mAdView;
    boolean interstitialCanceled;
    InterstitialAd mInterstitialAd;
    ConnectionDetector cd;
    private int table_no = 1;
    Activity activity;

    LearnTableBinding learnTableBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultLanguage(this);

//
        learnTableBinding = DataBindingUtil.setContentView(this,R.layout.learn_table);
        init();
        showbanner();
    }

    private void showbanner() {
        mAdView = findViewById(R.id.mAdView);
        // Forward Consent To AdMob
        Bundle extras = new Bundle();
        ConsentInformation consentInformation = ConsentInformation.getInstance(TableActivity.this);
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
        Intent intent = new Intent(TableActivity.this, DashboardActivity.class);
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
                Intent intent = new Intent(TableActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
        mp = MediaPlayer.create(this, R.raw.click);

        activity = this;

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 6);
        learnTableBinding.numberRecycler.setLayoutManager(layoutManager);

        numberAdapter = new NumberAdapter(getApplicationContext());
        learnTableBinding.numberRecycler.setAdapter(numberAdapter);
        numberAdapter.setClickListener(this);
        numberAdapter.setSelectedPos(0);

        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(getApplicationContext(), 2);
        learnTableBinding.tableRecycler.setLayoutManager(layoutManager1);

        tableAdapter = new TableAdapter(getApplicationContext(), 1);
        learnTableBinding.tableRecycler.setAdapter(tableAdapter);

        learnTableBinding.btnPlay.setOnClickListener(new View.OnClickListener() {
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
        Intent intent = new Intent(TableActivity.this, QuizActivity.class);
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
            ConsentInformation consentInformation = ConsentInformation.getInstance(TableActivity.this);
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
        cd = new ConnectionDetector(TableActivity.this);
        if (cd.isConnectingToInternet()) {
            mInterstitialAd = new InterstitialAd(TableActivity.this);
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
