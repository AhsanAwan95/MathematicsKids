package com.example.kidstable.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.kidstable.R;
import com.example.kidstable.gdpr.CustomGdprHelper;
import com.example.kidstable.utils.Constants;

import static com.example.kidstable.utils.Constants.getSound;
import static com.example.kidstable.utils.Constants.setDefaultLanguage;


public class SettingActivity extends AppCompatActivity {

    LinearLayout btn_language;
    RelativeLayout btn_privacy_policy, btn_rate_us, btn_feedback, btn_sound,btn_reset_gdpr;
    TextView txt_sound;
    ImageView img_sound;
    MediaPlayer mp;
    CustomGdprHelper customGdprHelper;
    Activity activity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultLanguage(this);
        setContentView(R.layout.activity_setting);
        customGdprHelper = new CustomGdprHelper(this);

        activity = this;
        init();
        setClick();


    }

    public static void sendFeedback( Activity activity) {

        String str;
        try {
            str = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{activity.getResources().getString(R.string.feedback_mail)});
            i.putExtra(Intent.EXTRA_SUBJECT, "Feedback for "+R.string.app_name);
            i.putExtra(Intent.EXTRA_TEXT, "\n\n----------------------------------\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + str + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER + "\n" + "feedback : " );
            try {
                activity.startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setClick() {
        btn_rate_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSound();
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        btn_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSound();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.privacy_policy_link)));
                startActivity(browserIntent);
            }
        });


        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSound();
                sendFeedback(activity);
            }
        });



        btn_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSound();
                if (getSound(getApplicationContext())) {
                    Constants.setSound(getApplicationContext(), false);

                } else {
                    Constants.setSound(getApplicationContext(), true);
                }
                setSoundIcons();
            }
        });

        btn_reset_gdpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                customGdprHelper.resetConsent();
                Toast.makeText(getApplicationContext(),"GDPR content ads reseted", Toast.LENGTH_SHORT).show();
//                customGdprHelper.displayConsentDialogue(false);
                customGdprHelper.resetConsent();
//                customGdprHelper = new CustomGdprHelper(MainActivity.this);
                customGdprHelper.initialise();
            }
        });

    }


    public void backIntent() {
        if (mp != null) {
            mp.release();
        }
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    public void startSound() {
        if (getSound(getApplicationContext())) {
            if (mp != null) {
                mp.release();
            }
            mp = MediaPlayer.create(this, R.raw.click);
            mp.start();
        }
    }


    private void init() {
        mp = MediaPlayer.create(this, R.raw.click);

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


        btn_feedback = findViewById(R.id.btn_feedback);

        btn_privacy_policy = findViewById(R.id.btn_privacy_policy);
        btn_reset_gdpr = findViewById(R.id.btn_reset_gdpr);
        btn_rate_us = findViewById(R.id.btn_rate_us);
        btn_sound = findViewById(R.id.btn_sound);
        img_sound = findViewById(R.id.img_sound);
        txt_sound = findViewById(R.id.txt_sound);




        setSoundIcons();
    }

    public void setSoundIcons() {
        if (getSound(getApplicationContext())) {
            img_sound.setImageResource(R.drawable.switch_on);
            txt_sound.setText(getString(R.string.on));
        } else {
            img_sound.setImageResource(R.drawable.switch_off);
            txt_sound.setText(getString(R.string.off));
        }
    }

    @Override
    public void onBackPressed() {
        backIntent();
    }


    public void setLanguageDialog() {
        final Dialog mViewDateDialog = new Dialog(this);
        mViewDateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mViewDateDialog.setContentView(R.layout.dialog_language);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mViewDateDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        mViewDateDialog.setCanceledOnTouchOutside(true);
        mViewDateDialog.getWindow().setAttributes(lp);
        mViewDateDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);









        mViewDateDialog.show();
    }


}
