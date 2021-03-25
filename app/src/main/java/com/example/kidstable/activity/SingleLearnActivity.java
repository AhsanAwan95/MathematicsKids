package com.example.kidstable.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.kidstable.R;
import com.example.kidstable.dataclasses.RandomData;
import com.example.kidstable.modelclasses.DualModel;
import com.example.kidstable.modelclasses.Model;
import com.example.kidstable.utils.Constants;
import com.example.kidstable.utils.RoundedBackgroundSpan;
import com.google.gson.Gson;

import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static com.example.kidstable.utils.Constants.LEVEL_NO;
import static com.example.kidstable.utils.Constants.MyPref;
import static com.example.kidstable.utils.Constants.setDefaultLanguage;
import static com.example.kidstable.utils.Constants.setTime;
import static com.thekhaeng.pushdownanim.PushDownAnim.DEFAULT_PUSH_DURATION;
import static com.thekhaeng.pushdownanim.PushDownAnim.DEFAULT_RELEASE_DURATION;

public class SingleLearnActivity extends AppCompatActivity implements View.OnClickListener {

    List<DualModel> tableModelList = new ArrayList<>();
    List<TextView> textViewList = new ArrayList<>();
    DualModel tableModel;
    String level_type;
    Handler handler = new Handler();
    Handler timerHandler = new Handler();
    boolean isRunnable = false;
    int quiz_no = 0, level_no = 0, wrong_count = 0, right_count = 0;
    long countTime;
    SharedPreferences pref;
    MediaPlayer answerPlayer;
    private MediaPlayer mp;
    TextView txt_header, text_timer, text_total_question, text_wrong_question, text_true_question, text_op_1, text_op_2, text_op_3, text_op_4, text_question;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultLanguage(this);
        setContentView(R.layout.activity_single_quiz);
        init();
    }


    public void backIntent() {
        if (mp != null) {
            mp.release();
        }

        setTime(getApplicationContext(), (countTime + Constants.getTime(getApplicationContext(), level_type)), level_type);
        Intent intent = new Intent(this, SingleQuizTypeActivity.class);
        intent.putExtra(Constants.LEVEL_TYPE, level_type);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void speak(int sound) {
        pref = getSharedPreferences(MyPref, MODE_PRIVATE);
        if (pref.getBoolean(Constants.IsSound, true)) {
            if (answerPlayer != null) {
                answerPlayer.release();
            }
            answerPlayer = MediaPlayer.create(getApplicationContext(), sound);
            answerPlayer.start();
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

    private void init() {
        mp = MediaPlayer.create(this, R.raw.click);
        pref = getSharedPreferences(MyPref, MODE_PRIVATE);

        level_type = getIntent().getStringExtra(Constants.LEVEL_TYPE);
        level_no = getIntent().getIntExtra(LEVEL_NO, 0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSound();
                clearRunnable();
                backIntent();
            }
        });
        countTime = 0;


        text_timer = findViewById(R.id.text_timer);

        text_op_1 = findViewById(R.id.txt_op_1);
        text_op_2 = findViewById(R.id.txt_op_2);
        text_op_3 = findViewById(R.id.txt_op_3);
        text_op_4 = findViewById(R.id.txt_op_4);

        text_total_question = findViewById(R.id.text_total_question);
        text_true_question = findViewById(R.id.text_true_question);
        text_wrong_question = findViewById(R.id.text_wrong_question);
        text_question = findViewById(R.id.text_question);
        txt_header = findViewById(R.id.txt_header);

        text_true_question.setText(String.valueOf(right_count));
        text_wrong_question.setText(String.valueOf(wrong_count));


        txt_header.setText(getString(R.string.level) + level_no);
        text_op_1.setOnClickListener(this);
        text_op_2.setOnClickListener(this);
        text_op_3.setOnClickListener(this);
        text_op_4.setOnClickListener(this);


        PushDownAnim.setPushDownAnimTo(text_op_1, text_op_2, text_op_3, text_op_4).
                setScale(PushDownAnim.MODE_STATIC_DP, 10).setDurationPush(DEFAULT_PUSH_DURATION)
                .setDurationRelease(DEFAULT_RELEASE_DURATION)
                .setInterpolatorPush(PushDownAnim.DEFAULT_INTERPOLATOR)
                .setInterpolatorRelease(PushDownAnim.DEFAULT_INTERPOLATOR);


        getData();
        if (tableModelList.size() > 0) {
            setQuiz(quiz_no);
        }


    }

    public void getData() {

        if (level_type.equalsIgnoreCase(getString(R.string.easy))) {
            tableModelList = RandomData.getEasyData(getApplicationContext());
        } else if (level_type.equalsIgnoreCase(getString(R.string.medium))) {
            tableModelList = RandomData.getMediumData(getApplicationContext());
        } else if (level_type.equalsIgnoreCase(getString(R.string.hard))) {
            tableModelList = RandomData.getHardData(getApplicationContext());
        }


    }

    private void startClock() {
        timerHandler.postDelayed(timerRunnable, 1000);
    }

    @Override
    protected void onResume() {
        startClock();
        super.onResume();
    }


    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            countTime = countTime + 1000;
            int seconds = (int) (countTime / 1000) % 60;
            text_timer.setText(seconds + " " + getString(R.string.s));
            timerHandler.postDelayed(timerRunnable, 1000);
        }
    };


    public void addTextView() {
        textViewList.add(text_op_1);
        textViewList.add(text_op_2);
        textViewList.add(text_op_3);
        textViewList.add(text_op_4);

        for (int i = 0; i < textViewList.size(); i++) {
            textViewList.get(i).setTextColor(getResources().getColorStateList(R.color.selector_text_color));
            textViewList.get(i).setBackgroundResource(R.drawable.selector_bg);
        }

    }


    public void setQuiz(int quiz_no) {
        addTextView();
        text_total_question.setText((quiz_no + 1) + " " + getString(R.string.str_sign) + " " + tableModelList.size());
        tableModel = tableModelList.get(quiz_no);

        List<String> answerList = new ArrayList<>();

        answerList.add(String.valueOf(tableModel.op_1));
        answerList.add(String.valueOf(tableModel.op_2));
        answerList.add(String.valueOf(tableModel.op_3));
        answerList.add(String.valueOf(tableModel.answer));

        Collections.shuffle(answerList);


        text_op_1.setText(String.valueOf(answerList.get(0)));
        text_op_2.setText(String.valueOf(answerList.get(1)));
        text_op_3.setText(String.valueOf(answerList.get(2)));
        text_op_4.setText(String.valueOf(answerList.get(3)));

        String[] separated = tableModel.question.split(":");
        SpannableString string = null;


        if (level_type.equalsIgnoreCase(getString(R.string.medium)) && tableModel.missing_pos == 3) {
            string = new SpannableString(tableModel.question.replace(":", "?"));
            string.setSpan(new RoundedBackgroundSpan(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark)), separated[0].length() - 2, separated[0].length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            string.setSpan(new RoundedBackgroundSpan(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark)),
                    separated[1].length() + 2, separated[1].length() + 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (level_type.equalsIgnoreCase(getString(R.string.hard)) && tableModel.missing_pos == 3) {
            string = new SpannableString(tableModel.question.replace(":", "?"));
            string.setSpan(new RoundedBackgroundSpan(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark)), separated[0].length() - 2, separated[0].length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            string.setSpan(new RoundedBackgroundSpan(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark)),
                    separated[1].length() + 2, separated[1].length() + 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (level_type.equalsIgnoreCase(getString(R.string.hard)) && tableModel.missing_pos == 1) {
            string = new SpannableString(tableModel.question);
            String[] s = tableModel.question.split("=");

            string.setSpan(new RoundedBackgroundSpan(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark)),
                    s[0].length() + 2, s[0].length() + s[1].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            string = new SpannableString(tableModel.question.replace(":", "?"));
            string.setSpan(new RoundedBackgroundSpan(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark)), separated[0].length() - 2, separated[0].length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        text_question.setText(string, TextView.BufferType.SPANNABLE);


    }


    public void checkAnswer(TextView textView) {
        if (isAdd(textView)) {
            int color;
            if (textView.getText().toString().equalsIgnoreCase(tableModel.answer)) {
                speak(R.raw.correct);
                if (isAdd(textView)) {
                    right_count = right_count + 1;
                }
                textViewList.clear();
                textView.setBackgroundResource(R.drawable.green_bg);
                textView.setTextColor(Color.WHITE);
                if (!isRunnable) {
                    isRunnable = true;
                    handler.postDelayed(runnable, 1000);
                }
                color = ContextCompat.getColor(getApplicationContext(), R.color.green);
            } else {
                speak(R.raw.incorrect);
                countTime = countTime + 8000;
                textView.setBackgroundResource(R.drawable.red_bg);
                textView.setTextColor(Color.WHITE);
                wrong_count = wrong_count + 1;
                textViewList.remove(textView);
                color = ContextCompat.getColor(getApplicationContext(), R.color.red);
            }

            if (level_type.equalsIgnoreCase(getString(R.string.easy))) {
                setEasyQuestion(textView, color);
            } else if (level_type.equalsIgnoreCase(getString(R.string.medium))) {
                setMediumQuestion(textView, color);
            } else {
                setHardQuestion(textView, color);
            }

            text_true_question.setText(String.valueOf(right_count));
            text_wrong_question.setText(String.valueOf(wrong_count));
        }

    }


    public void setHardQuestion(TextView textView, int color) {
        String[] separated;
        SpannableString string;

        String space = getString(R.string.space);
        String white_space = getString(R.string.white_space);

        if (tableModel.missing_pos == 1) {
            string = new SpannableString(tableModel.question.replace("?", textView.getText().toString() + white_space));
            String[] s = tableModel.question.split("=");

            string.setSpan(new RoundedBackgroundSpan(color),
                    s[0].length() + 2, s[0].length() + (s[1].length() + (textView.getText().toString().length() - 1)), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        } else if (tableModel.missing_pos == 3) {

            string = getQuestionString(color, textView);

        } else {

            separated = tableModel.question.split(":");
            if (textView.getText().toString().length() > 1) {
                string = new SpannableString(tableModel.question.replace(":", textView.getText().toString() + space));
            } else {
                string = new SpannableString(tableModel.question.replace(":", textView.getText().toString()));
            }


            if (textView.getText().toString().length() > 1) {
                string.setSpan(new RoundedBackgroundSpan(color), separated[0].length() - 2, separated[0].length() + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                string.setSpan(new RoundedBackgroundSpan(color), separated[0].length() - 2, separated[0].length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        text_question.setText(string, TextView.BufferType.SPANNABLE);
    }

    public void setMediumQuestion(TextView textView, int color) {
        String[] separated;
        SpannableString string;

        String space = getString(R.string.space);

        if (tableModel.missing_pos != 3) {

            separated = tableModel.question.split(":");
            if (textView.getText().toString().length() > 1) {
                string = new SpannableString(tableModel.question.replace(":", textView.getText().toString() + space));
            } else {
                string = new SpannableString(tableModel.question.replace(":", textView.getText().toString()));
            }

            if (textView.getText().toString().length() > 1) {
                string.setSpan(new RoundedBackgroundSpan(color), separated[0].length() - 2, separated[0].length() + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                string.setSpan(new RoundedBackgroundSpan(color), separated[0].length() - 2, separated[0].length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            string = getQuestionString(color, textView);
        }

        text_question.setText(string, TextView.BufferType.SPANNABLE);
    }


    public SpannableString getQuestionString(int color, TextView textView) {
        String space = getString(R.string.space);
        String white_space = getString(R.string.white_space);


        SpannableString string = new SpannableString(tableModel.question.replace("   :   *   :   ", space + space + textView.getText().toString() + space + space + space));
        string = new SpannableString(string.toString().replace(" * ", white_space + "*" + white_space));


        String text_answer = textView.getText().toString().replace(" * ", " : ");
        String[] s = text_answer.split(" : ");

        int f_end, s_start, s_end;
        if (s[0].length() == 1 && s[1].length() == 1) {
            f_end = 5;
            s_start = 8;
            s_end = 13;
        } else if (s[0].length() == 2 && s[1].length() == 2) {
            f_end = 6;
            s_start = 9;
            s_end = 15;
        } else if (s[0].length() == 1 && s[1].length() == 2) {
            f_end = 5;
            s_start = 8;
            s_end = 13;
        } else {
            f_end = 6;
            s_start = 9;
            s_end = 14;
        }


        string.setSpan(new RoundedBackgroundSpan(color), 0, f_end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        string.setSpan(new RoundedBackgroundSpan(color), s_start, s_end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return string;
    }

    public void setEasyQuestion(TextView textView, int color) {
        String[] separated;
        SpannableString string;

        String space = getString(R.string.space);

        separated = tableModel.question.split(":");
        if (textView.getText().toString().length() > 1) {
            string = new SpannableString(tableModel.question.replace(":", textView.getText().toString() + space));
        } else {
            string = new SpannableString(tableModel.question.replace(":", textView.getText().toString()));
        }


        if (textView.getText().toString().length() > 1) {
            string.setSpan(new RoundedBackgroundSpan(color), separated[0].length() - 2, separated[0].length() + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            string.setSpan(new RoundedBackgroundSpan(color), separated[0].length() - 2, separated[0].length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        text_question.setText(string, TextView.BufferType.SPANNABLE);

    }


    @Override
    protected void onDestroy() {
        clearRunnable();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        clearRunnable();
        super.onPause();
    }

    public void clearRunnable() {
        if (isRunnable) {
            isRunnable = false;
            if (quiz_no < tableModelList.size() - 1) {
                quiz_no = quiz_no + 1;
                setQuiz(quiz_no);
            } else {
                showT1ResultDialog();
            }

            handler.removeCallbacks(runnable);
        }

        if (timerHandler != null) {
            timerHandler.removeCallbacks(timerRunnable);
        }

        if (answerPlayer != null) {
            answerPlayer.release();
        }


    }

    @Override
    public void onBackPressed() {
        clearRunnable();
        backIntent();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isRunnable = false;
            if (quiz_no < tableModelList.size() - 1) {
                quiz_no = quiz_no + 1;
                setQuiz(quiz_no);
            } else {
                showT1ResultDialog();
            }
        }
    };


    public boolean isAdd(TextView textView) {
        boolean isAdd = false;
        for (int i = 0; i < textViewList.size(); i++) {
            if (textViewList.get(i) == textView) {
                isAdd = true;
                break;
            }
        }
        return isAdd;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_op_1:
                checkAnswer(text_op_1);
                break;
            case R.id.txt_op_2:
                checkAnswer(text_op_2);
                break;
            case R.id.txt_op_3:
                checkAnswer(text_op_3);
                break;
            case R.id.txt_op_4:
                checkAnswer(text_op_4);
                break;

        }
    }


    public void showT1ResultDialog() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View mView = layoutInflaterAndroid.inflate(R.layout.result_single_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

        ImageButton btn_refresh, btn_next;
        TextView text_wrong_question, text_true_question, txt_result_string;

        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.setCanceledOnTouchOutside(false);

        btn_refresh = mView.findViewById(R.id.btn_refresh);
        btn_next = mView.findViewById(R.id.btn_next);
        text_true_question = mView.findViewById(R.id.text_true_question);
        text_wrong_question = mView.findViewById(R.id.text_wrong_question);
        txt_result_string = mView.findViewById(R.id.txt_result_string);

        text_true_question.setText(String.valueOf(right_count));
        text_wrong_question.setText(String.valueOf(wrong_count));

        if (timerHandler != null) {
            timerHandler.removeCallbacks(timerRunnable);
        }

        setTime(getApplicationContext(), countTime, level_type);

        if (right_count > wrong_count) {

            for (int i = 0; i < 10; i++) {
                SharedPreferences sharedPreferences = getSharedPreferences(MyPref, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Model model;
                if ((level_no - 1) >= i) {
                    model = new Model((i + 1), false, true, level_type);
                } else if (((level_no - 1) + 1) == i) {
                    model = new Model((i + 1), true, false, level_type);
                } else {
                    model = new Model((i + 1), false, false, level_type);
                }

                Gson gson = new Gson();
                String json = gson.toJson(model);
                editor.putString(level_type + i, json);
                editor.commit();

            }

            txt_result_string.setText(getString(R.string.pass));

        } else {
            txt_result_string.setText(getString(R.string.fail));
        }


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSound();
                clearRunnable();
                if (right_count > wrong_count) {
                    passResultIntent((level_no + 1), level_type);
                    alertDialogAndroid.dismiss();
                } else {
                    Toast.makeText(SingleLearnActivity.this, "" + getString(R.string.clear_level), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSound();
                clearRunnable();
                passResultIntent(level_no, level_type);
                overridePendingTransition(0, 0);
                alertDialogAndroid.dismiss();
            }
        });


    }

    public void passResultIntent(int level_no, String level_type) {
        Intent intent = new Intent(SingleLearnActivity.this, SingleLearnActivity.class);
        intent.putExtra(LEVEL_NO, (level_no));
        intent.putExtra(Constants.LEVEL_TYPE, level_type);
        startActivity(intent);
    }


}
