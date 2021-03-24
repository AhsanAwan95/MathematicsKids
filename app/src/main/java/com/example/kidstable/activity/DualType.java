package com.example.kidstable.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.kidstable.R;
import com.example.kidstable.utils.Constants;

import static com.example.kidstable.activity.SettingActivity.sendFeedback;
import static com.example.kidstable.utils.Constants.setDefaultLanguage;


public class DualType extends AppCompatActivity {

    LinearLayout btn_easy, btn_medium, btn_hard;
    int view_width, view_height;
    MediaPlayer mp;
    Activity activity;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultLanguage(this);
        setContentView(R.layout.activity_dual_type);
        init();

    }

    public void backIntent() {
        if (mp != null) {
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

        btn_easy = findViewById(R.id.btn_easy);
        btn_medium = findViewById(R.id.btn_medium);
        btn_hard = findViewById(R.id.btn_hard);
        activity = this;
        btn_easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passIntent(R.string.easy);
            }
        });

        btn_hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passIntent(R.string.hard);
            }
        });

        btn_medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passIntent(R.string.medium);
            }
        });

        new SetWidth().execute();

    }

    public class SetWidth extends AsyncTask<Void, Void, View> {
        @Override
        protected View doInBackground(Void... voids) {

            getWidth(btn_easy);
            getWidth(btn_medium);
            getWidth(btn_hard);

            return null;
        }

        @Override
        protected void onPostExecute(View view) {
            super.onPostExecute(view);
        }
    }

    public int getWidth(final LinearLayout btn) {
        final int[] width = new int[1];
        ViewTreeObserver vto = btn.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    btn.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    btn.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                width[0] = btn.getMeasuredWidth();
                int height = btn.getMeasuredHeight();

                if (view_width < width[0]) {
                    view_width = width[0];
                    view_height = height;
                    setWidth(view_height, view_width);

                }


            }
        });
        return width[0];
    }


    public void setWidth(int height, int width) {
        CardView.LayoutParams btn_easy_layoutParams = new CardView.LayoutParams(width, height);
        btn_easy_layoutParams.gravity = Gravity.CENTER;
        btn_easy.setLayoutParams(btn_easy_layoutParams);

        CardView.LayoutParams btn_medium_layoutParams = new CardView.LayoutParams(width, height);
        btn_medium_layoutParams.gravity = Gravity.CENTER;
        btn_medium.setLayoutParams(btn_medium_layoutParams);

        CardView.LayoutParams btn_hard_layoutParams = new CardView.LayoutParams(width, height);
        btn_hard_layoutParams.gravity = Gravity.CENTER;
        btn_hard.setLayoutParams(btn_hard_layoutParams);


    }

    public void passIntent(int string) {
        startSound();
        Constants.setLevelType(getApplicationContext(), getString(string));
        Intent intent = new Intent(DualType.this, DualActivity.class);
        startActivity(intent);
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

}
