package com.proceeddinc.kidstable.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.proceeddinc.kidstable.R;
import com.proceeddinc.kidstable.activity.SingleLearnActivity;
import com.proceeddinc.kidstable.adapters.LevelAdapter;
import com.proceeddinc.kidstable.utils.ConnectionDetector;
import com.proceeddinc.kidstable.utils.Constants;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;


import java.util.concurrent.TimeUnit;


public class MediumQuizFragment extends Fragment implements LevelAdapter.onClik {

    private View view;
    private MediaPlayer mp;
    boolean interstitialCanceled;
    InterstitialAd mInterstitialAd;
    ConnectionDetector cd;
    int Mainposition =0;
    private void startSound() {
        if (Constants.getSound(getContext())) {
            if (mp != null) {
                mp.release();
            }
            mp = MediaPlayer.create(getContext(), R.raw.eventclick);
            mp.start();
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment, container, false);
        init();


        return view;
    }

    private void init() {
        mp = MediaPlayer.create(getActivity(), R.raw.eventclick);
        TextView txt_time = view.findViewById(R.id.txt_time);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(layoutManager);

        LevelAdapter levelAdapter = new LevelAdapter(getContext(), Constants.geLevelData(getContext(), getString(R.string.medium)));
        recyclerView.setAdapter(levelAdapter);
        levelAdapter.setoperatoinListener(this);


        long seconds = TimeUnit.MILLISECONDS.toSeconds(Constants.getTime(getActivity(), getString(R.string.medium)));

        txt_time.append(" " + seconds + " " + getString(R.string.s));

    }


    @Override
    public void onClick(View view, int position, int id) {
        Mainposition = id;
        if (!interstitialCanceled) {
            if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                ContinueIntent(Mainposition);
            }
        }
    }

    @Override
    public void onToast() {
        startSound();
        Toast.makeText(getContext(), "" + getString(R.string.clear_level), Toast.LENGTH_SHORT).show();
    }
    private void CallNewInsertial() {
//        // Forward Consent To AdMob
//        Bundle extras = new Bundle();
//        ConsentInformation consentInformation = ConsentInformation.getInstance(LearnTableActivity.this);
//        if (consentInformation.getConsentStatus().equals(ConsentStatus.NON_PERSONALIZED)) {
//            extras.putString("npa", "1");
//        }
        cd = new ConnectionDetector(getActivity());
        if (cd.isConnectingToInternet()) {
            mInterstitialAd = new InterstitialAd(getActivity());
            mInterstitialAd.setAdUnitId(getString(R.string.InterstitialAds));
            requestNewInterstitial();
            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdClosed() {
                    ContinueIntent(Mainposition);
                }
            });
        }
    }

    private void ContinueIntent(int mainposition) {
        startSound();
        Intent intent = new Intent(getContext(), SingleLearnActivity.class);
        intent.putExtra(Constants.LEVEL_NO, Mainposition);
        intent.putExtra(Constants.LEVEL_TYPE, getString(R.string.medium));
        startActivity(intent);
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
            ConsentInformation consentInformation = ConsentInformation.getInstance(getActivity());
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

//    @Override
//    protected void onPause() {
//        super.onPause();
//        mInterstitialAd = null;
//        interstitialCanceled = true;
//    }

    @Override
    public void onPause() {
        super.onPause();
        mInterstitialAd = null;
        interstitialCanceled = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        interstitialCanceled = false;
        if (getResources().getString(R.string.ADS_VISIBILITY).equals("YES")) {
            CallNewInsertial();
        }
    }
}
