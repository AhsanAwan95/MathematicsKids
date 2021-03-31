package com.example.kidstable.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.example.kidstable.R;
import com.example.kidstable.databinding.ActivityDashboardBinding;
import com.example.kidstable.databinding.NewDashboardBinding;
import com.example.kidstable.gdpr.CustomGdprHelper;
import com.example.kidstable.utils.Constants;
import com.thekhaeng.pushdownanim.PushDownAnim;

import static com.example.kidstable.utils.Constants.setDefaultLanguage;
import static com.thekhaeng.pushdownanim.PushDownAnim.DEFAULT_PUSH_DURATION;
import static com.thekhaeng.pushdownanim.PushDownAnim.DEFAULT_RELEASE_DURATION;


public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    //LinearLayout btn_table_quiz, btn_dual_quiz, btn_pythagoran, btn_learn_quiz;
    //ImageView btn_setting, btn_share;
    MediaPlayer mp;
    boolean isSound;
    CustomGdprHelper customGdprHelper;

    NewDashboardBinding activityDashboardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultLanguage(this);


        activityDashboardBinding =  DataBindingUtil.setContentView(this, R.layout.new_dashboard);

        init();
        // Setup Custom Consent
        customGdprHelper = new CustomGdprHelper(this);
        customGdprHelper.initialise();

    }

    private void init() {

        mp = MediaPlayer.create(this, R.raw.click);
        isSound = Constants.getSound(getApplicationContext());





        activityDashboardBinding.btnDualQuiz.setOnClickListener(this);
        activityDashboardBinding.btnTableQuiz.setOnClickListener(this);
        activityDashboardBinding.btnLearnQuiz.setOnClickListener(this);
        activityDashboardBinding.btnPythagoran.setOnClickListener(this);
        activityDashboardBinding.btnShare.setOnClickListener(this);
        activityDashboardBinding.btnSetting.setOnClickListener(this);


        PushDownAnim.setPushDownAnimTo(activityDashboardBinding.btnDualQuiz, activityDashboardBinding.btnTableQuiz,
                activityDashboardBinding.btnLearnQuiz, activityDashboardBinding.btnPythagoran).
                setScale(PushDownAnim.MODE_STATIC_DP, 10).setDurationPush(DEFAULT_PUSH_DURATION)
                .setDurationRelease(DEFAULT_RELEASE_DURATION)
                .setInterpolatorPush(PushDownAnim.DEFAULT_INTERPOLATOR)
                .setInterpolatorRelease(PushDownAnim.DEFAULT_INTERPOLATOR);


    }

    @Override
    public void onClick(View view) {

        Intent intent;
        if (view.getId() == R.id.btn_table_quiz) {
            intent = new Intent(DashboardActivity.this, TableActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_dual_quiz) {
            intent = new Intent(DashboardActivity.this, DualTypeQuiz.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_pythagoran) {
            intent = new Intent(DashboardActivity.this, PythagoranActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_learn_quiz) {
            intent = new Intent(DashboardActivity.this, SingleQuizTypeActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_setting) {
            intent = new Intent(DashboardActivity.this, SettingActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_share) {
            share();
        }
        startSound();

    }

    public void startSound() {
        if (isSound) {
            if (mp != null) {
                mp.release();
            }
            mp = MediaPlayer.create(this, R.raw.click);
            mp.start();
        }
    }


    public void share() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Xyz");
        share.putExtra(Intent.EXTRA_TEXT, getString(R.string.SHARE_APP_LINK)
                + DashboardActivity.this.getPackageName());
        startActivity(Intent.createChooser(share, "Share Link!"));
    }

    @Override
    protected void onDestroy() {
        mp.release();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        if(mp!=null){
//            mp.release();
//        }
        ActivityCompat.finishAffinity(this);
    }
}
