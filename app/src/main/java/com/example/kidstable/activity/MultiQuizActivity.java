package com.example.kidstable.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;


import com.example.kidstable.R;
import com.example.kidstable.databinding.ActivityDualQuizBinding;
import com.example.kidstable.dataclasses.RandomData;


import com.example.kidstable.modelclasses.DualModel;
import com.example.kidstable.utils.Constants;
import com.example.kidstable.utils.RoundedBackgroundSpan;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static com.example.kidstable.utils.Constants.setDefaultLanguage;
import static com.thekhaeng.pushdownanim.PushDownAnim.DEFAULT_PUSH_DURATION;
import static com.thekhaeng.pushdownanim.PushDownAnim.DEFAULT_RELEASE_DURATION;

public class MultiQuizActivity extends AppCompatActivity implements View.OnClickListener {

    List<DualModel> tableModelListT1 = new ArrayList<>();
    List<DualModel> tableModelListT2 = new ArrayList<>();
    List<TextView> textViewListT1 = new ArrayList<>();
    List<TextView> textViewListT2 = new ArrayList<>();
    DualModel tableModelT1;
    DualModel tableModelT2;
    Handler handlerT1 = new Handler();
    Handler handlerT2 = new Handler();
    boolean isRunnableT1 = false;
    boolean isRunnableT2 = false;
    boolean isDialogOpen = false;
    SharedPreferences pref;
    MediaPlayer answerPlayerT1;
    MediaPlayer mp;
    CountDownTimer countDownTimerT1, countDownTimerT2;
    String level_type;
    MediaPlayer answerPlayerT2;

    boolean isTimerT1, isTimerT2;
    boolean isPlay = true;
    int countT1 = 10, countT2 = 10, secT1, secT2;
    int quiz_t1_no = 0, quiz_t2_no = 0, wrong_t1_count = 0, right_t1_count = 0, right_t2_count = 0, wrong_t2_count = 0;

    ActivityDualQuizBinding activityDualBinding;

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


        activityDualBinding = DataBindingUtil.setContentView(this,R.layout.activity_dual_quiz);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTimerT1();
        setTimerT2();
    }

    public void setTimerT1() {

        countT1 = countT1 * 1000;

        countDownTimerT1 = new CountDownTimer(countT1, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isPlay = true;
                activityDualBinding.btnPlay.setImageResource(R.drawable.ic_pause);
                isTimerT1 = true;
                activityDualBinding.textT1Timer.setText(millisUntilFinished / 1000 + getString(R.string.space) + getString(R.string.s));
                countT1 = (int) (millisUntilFinished / 1000);
                secT1 = secT1 + 1;
                activityDualBinding.timerProgressT1.setProgress(countT1);
            }

            @Override
            public void onFinish() {
                isTimerT1 = false;
                if (quiz_t1_no < tableModelListT1.size() - 1) {
                    wrong_t1_count = wrong_t1_count + 1;
                    activityDualBinding.textT1WrongQuestion.setText(String.valueOf(wrong_t1_count));
                }
                setNextQuizT1();
            }
        }.start();

    }


    public void setNextQuizT1() {
        if (quiz_t1_no < tableModelListT1.size() - 1) {
            quiz_t1_no = quiz_t1_no + 1;
            setQuizT1(quiz_t1_no);
            countDownTimerT1.cancel();
            countT1 = 10;
            activityDualBinding.timerProgressT1.setMax(countT1);

            setTimerT1();


        } else {
            if (!isDialogOpen) {
                showResultDialog();
            }
        }

    }

    public void setTimerT2() {

        countT2 = countT2 * 1000;

        countDownTimerT2 = new CountDownTimer(countT2, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                isTimerT2 = true;

                isPlay = true;
                activityDualBinding.btnPlay.setImageResource(R.drawable.ic_pause);

                activityDualBinding.textT2Timer.setText(millisUntilFinished / 1000 + getString(R.string.space) + getString(R.string.s));
                countT2 = (int) (millisUntilFinished / 1000);
                secT2 = secT2 + 1;
                activityDualBinding.timerProgressT2.setProgress(countT2);
            }

            @Override
            public void onFinish() {
                isTimerT2 = false;
                if (quiz_t2_no < tableModelListT2.size() - 1) {
                    wrong_t2_count = wrong_t2_count + 1;
                    activityDualBinding.textT2WrongQuestion.setText(String.valueOf(wrong_t2_count));
                }

                setNextQuizT2();


            }
        }.start();

    }

    public void setNextQuizT2() {

        if (quiz_t2_no < tableModelListT2.size() - 1) {
            quiz_t2_no = quiz_t2_no + 1;
            setQuizT2(quiz_t2_no);

            countDownTimerT2.cancel();
            countT2 = 10;
            activityDualBinding.timerProgressT2.setMax(countT2);
            setTimerT2();
        } else {
            if (!isDialogOpen) {
                showResultDialog();
            }
        }

    }


    public void speakT1(int sound) {
        pref = getSharedPreferences(Constants.MyPref, MODE_PRIVATE);
        if (pref.getBoolean(Constants.IsSound, true)) {

            if (answerPlayerT1 != null) {
                answerPlayerT1.release();
            }
            answerPlayerT1 = MediaPlayer.create(getApplicationContext(), sound);
            answerPlayerT1.start();

        }
    }


    public void speakT2(int sound) {
        pref = getSharedPreferences(Constants.MyPref, MODE_PRIVATE);
        if (pref.getBoolean(Constants.IsSound, true)) {
            if (answerPlayerT2 != null) {
                answerPlayerT2.release();
            }
            answerPlayerT2 = MediaPlayer.create(getApplicationContext(), sound);
            answerPlayerT2.start();
        }
    }


    private void init() {
        level_type = Constants.getLevelType(getApplicationContext());
        mp = MediaPlayer.create(this, R.raw.click);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearRunnable();
                finish();
            }
        });


        activityDualBinding.textT1TrueQuestion.setText(String.valueOf(right_t1_count));
        activityDualBinding.textT1WrongQuestion.setText(String.valueOf(wrong_t1_count));

        activityDualBinding.textT2TrueQuestion.setText(String.valueOf(right_t2_count));
        activityDualBinding.textT2WrongQuestion.setText(String.valueOf(wrong_t2_count));

        activityDualBinding.textT1Op1.setOnClickListener(this);
        activityDualBinding.textT1Op2.setOnClickListener(this);
        activityDualBinding. textT1Op3.setOnClickListener(this);
        activityDualBinding.textT1Op4.setOnClickListener(this);
        activityDualBinding.textT2Op1.setOnClickListener(this);
        activityDualBinding.textT2Op2.setOnClickListener(this);
        activityDualBinding.textT2Op3.setOnClickListener(this);
        activityDualBinding.textT2Op4.setOnClickListener(this);
        activityDualBinding.btnPlay.setOnClickListener(this);


        PushDownAnim.setPushDownAnimTo(activityDualBinding.textT1Op1, activityDualBinding.textT1Op2, activityDualBinding.textT1Op3, activityDualBinding.textT1Op4,
                activityDualBinding.textT2Op1, activityDualBinding.textT2Op2, activityDualBinding.textT2Op3, activityDualBinding.textT2Op4).
                setScale(PushDownAnim.MODE_STATIC_DP, 10).setDurationPush(DEFAULT_PUSH_DURATION)
                .setDurationRelease(DEFAULT_RELEASE_DURATION)
                .setInterpolatorPush(PushDownAnim.DEFAULT_INTERPOLATOR)
                .setInterpolatorRelease(PushDownAnim.DEFAULT_INTERPOLATOR);
        activityDualBinding.timerProgressT1.setMax(countT1);
        activityDualBinding.timerProgressT2.setMax(countT2);

        getData();
        if (tableModelListT1.size() > 0) {
            setQuizT1(quiz_t1_no);
            setQuizT2(quiz_t2_no);
        }


    }


    public void getData() {

        if (Constants.getLevelType(getApplicationContext()).equalsIgnoreCase(getString(R.string.easy))) {
            tableModelListT1 = RandomData.getEasyData(getApplicationContext());
            tableModelListT2 = RandomData.getEasyData(getApplicationContext());
        } else if (Constants.getLevelType(getApplicationContext()).equalsIgnoreCase(getString(R.string.medium))) {
            tableModelListT1 = RandomData.getMediumData(getApplicationContext());
            tableModelListT2 = RandomData.getMediumData(getApplicationContext());
        } else if (Constants.getLevelType(getApplicationContext()).equalsIgnoreCase(getString(R.string.hard))) {
            tableModelListT1 = RandomData.getHardData(getApplicationContext());
            tableModelListT2 = RandomData.getHardData(getApplicationContext());
        }


    }


    public void addTextViewT1() {
        textViewListT1.add(activityDualBinding.textT1Op1);
        textViewListT1.add(activityDualBinding.textT1Op2);
        textViewListT1.add(activityDualBinding.textT1Op3);
        textViewListT1.add(activityDualBinding.textT1Op4);

        for (int i = 0; i < textViewListT1.size(); i++) {
            textViewListT1.get(i).setTextColor(getResources().getColorStateList(R.color.selector_text_color));
            textViewListT1.get(i).setBackgroundResource(R.drawable.selector_bg);
        }

    }


    public void addTextViewT2() {
        textViewListT2.add(activityDualBinding.textT2Op1);
        textViewListT2.add(activityDualBinding.textT2Op2);
        textViewListT2.add(activityDualBinding.textT2Op3);
        textViewListT2.add(activityDualBinding.textT2Op4);

        for (int i = 0; i < textViewListT2.size(); i++) {
            textViewListT2.get(i).setTextColor(getResources().getColorStateList(R.color.selector_text_color));
            textViewListT2.get(i).setBackgroundResource(R.drawable.selector_bg);
        }

    }

    public void setQuizT2(int quiz_t1_no) {
        addTextViewT2();
        activityDualBinding.textT2TotalQuestion.setText((quiz_t1_no + 1) + " " + getString(R.string.str_sign) + " " + tableModelListT2.size());
        tableModelT2 = tableModelListT2.get(quiz_t1_no);

        List<String> answerList = new ArrayList<>();

        answerList.add(String.valueOf(tableModelT2.op_1));
        answerList.add(String.valueOf(tableModelT2.op_2));
        answerList.add(String.valueOf(tableModelT2.op_3));
        answerList.add(String.valueOf(tableModelT2.answer));

        Collections.shuffle(answerList);

        activityDualBinding.textT2Op1.setText(String.valueOf(answerList.get(0)));
        activityDualBinding.textT2Op2.setText(String.valueOf(answerList.get(1)));
        activityDualBinding.textT2Op3.setText(String.valueOf(answerList.get(2)));
        activityDualBinding.textT2Op4.setText(String.valueOf(answerList.get(3)));


        activityDualBinding.textT21.setText(getSpanQuestion(tableModelT2), TextView.BufferType.SPANNABLE);


    }


    public void setQuizT1(int quiz_t1_no) {
        addTextViewT1();
        activityDualBinding.textT1TotalQuestion.setText((quiz_t1_no + 1) + " " + getString(R.string.str_sign) + " " + tableModelListT1.size());
        tableModelT1 = tableModelListT1.get(quiz_t1_no);

        List<String> answerList = new ArrayList<>();

        answerList.add(String.valueOf(tableModelT1.op_1));
        answerList.add(String.valueOf(tableModelT1.op_2));
        answerList.add(String.valueOf(tableModelT1.op_3));
        answerList.add(String.valueOf(tableModelT1.answer));

        Collections.shuffle(answerList);

        activityDualBinding.textT1Op1.setText(String.valueOf(answerList.get(0)));
        activityDualBinding.textT1Op2.setText(String.valueOf(answerList.get(1)));
        activityDualBinding.textT1Op3.setText(String.valueOf(answerList.get(2)));
        activityDualBinding.textT1Op4.setText(String.valueOf(answerList.get(3)));

        activityDualBinding.textT11.setText(getSpanQuestion(tableModelT1), TextView.BufferType.SPANNABLE);
    }


    public SpannableString getSpanQuestion(DualModel tableModel) {
        String[] separated = tableModel.question.split(":");
        SpannableString string;


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

        return string;
    }

    public void checkT1Answer(TextView textView) {


        if (isAddT1(textView)) {

            textViewListT1.clear();
            int color;
            if (textView.getText().toString().equalsIgnoreCase(tableModelT1.answer)) {

                right_t1_count = right_t1_count + 1;
                speakT1(R.raw.correct);
                textViewListT1.clear();
                textView.setBackgroundResource(R.drawable.green_bg);
                textView.setTextColor(Color.WHITE);

                color = ContextCompat.getColor(getApplicationContext(), R.color.green);
            } else {
                speakT1(R.raw.incorrect);
                textView.setBackgroundResource(R.drawable.red_bg);
                textView.setTextColor(Color.WHITE);
                wrong_t1_count = wrong_t1_count + 1;
                color = ContextCompat.getColor(getApplicationContext(), R.color.red);
            }

            if (level_type.equalsIgnoreCase(getString(R.string.easy))) {
                setEasyQuestion(tableModelT1, activityDualBinding.textT11, textView, color);
            } else if (level_type.equalsIgnoreCase(getString(R.string.medium))) {
                setMediumQuestion(tableModelT1, activityDualBinding.textT11, textView, color);
            } else {
                setHardQuestion(tableModelT1, activityDualBinding.textT11, textView, color);
            }

            activityDualBinding.textT1TrueQuestion.setText(String.valueOf(right_t1_count));
            activityDualBinding.textT1WrongQuestion.setText(String.valueOf(wrong_t1_count));

            if (!isRunnableT1) {
                isRunnableT1 = true;
                handlerT1.postDelayed(runnableT1, 500);
            }


        }

    }


    public void setHardQuestion(DualModel tableModel, TextView text_question, TextView textView, int color) {
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

            string = getQuestionString(tableModel, color, textView);

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

    public void setMediumQuestion(DualModel tableModel, TextView text_question, TextView textView, int color) {
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
            string = getQuestionString(tableModel, color, textView);
        }

        text_question.setText(string, TextView.BufferType.SPANNABLE);
    }


    public SpannableString getQuestionString(DualModel tableModel, int color, TextView textView) {
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

    public void setEasyQuestion(DualModel tableModel, TextView text_question, TextView textView, int color) {
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


    public void checkT2Answer(TextView textView) {
        if (isAddT2(textView)) {

            textViewListT2.clear();
            int color;
            if (textView.getText().toString().equalsIgnoreCase(tableModelT2.answer)) {
                right_t2_count = right_t2_count + 1;
                speakT2(R.raw.correct);
                textViewListT2.clear();
                textView.setBackgroundResource(R.drawable.green_bg);
                textView.setTextColor(Color.WHITE);
                color = ContextCompat.getColor(getApplicationContext(), R.color.green);
            } else {
                speakT2(R.raw.incorrect);
                textView.setBackgroundResource(R.drawable.red_bg);
                textView.setTextColor(Color.WHITE);
                wrong_t2_count = wrong_t2_count + 1;
                color = ContextCompat.getColor(getApplicationContext(), R.color.red);
            }


            if (level_type.equalsIgnoreCase(getString(R.string.easy))) {
                setEasyQuestion(tableModelT2, activityDualBinding.textT21, textView, color);
            } else if (level_type.equalsIgnoreCase(getString(R.string.medium))) {
                setMediumQuestion(tableModelT2, activityDualBinding.textT21, textView, color);
            } else {
                setHardQuestion(tableModelT2, activityDualBinding.textT21, textView, color);
            }


            activityDualBinding.textT2TrueQuestion.setText(String.valueOf(right_t2_count));
            activityDualBinding.textT2WrongQuestion.setText(String.valueOf(wrong_t2_count));
            if (!isRunnableT2) {
                isRunnableT2 = true;
                handlerT2.postDelayed(runnableT2, 500);
            }

            textViewListT2.remove(textView);
        }

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
        if (isRunnableT1) {


            handlerT1.removeCallbacks(runnableT1);
        }

        if (isRunnableT2) {

            handlerT2.removeCallbacks(runnableT2);
        }

        if (answerPlayerT1 != null) {
            answerPlayerT1.release();
        }

        if (answerPlayerT2 != null) {
            answerPlayerT2.release();
        }

        if (mp != null) {
            mp.release();
        }

        isPlay = false;
        activityDualBinding.btnPlay.setImageResource(R.drawable.ic_play);


        if (isTimerT1) {
            countDownTimerT1.cancel();
        }

        if (isTimerT2) {
            countDownTimerT2.cancel();
        }

    }

    @Override
    public void onBackPressed() {
        clearRunnable();
        super.onBackPressed();
    }

    Runnable runnableT1 = new Runnable() {
        @Override
        public void run() {
            isRunnableT1 = false;
            setNextQuizT1();
        }
    };


    Runnable runnableT2 = new Runnable() {
        @Override
        public void run() {
            isRunnableT2 = false;
            setNextQuizT2();
        }
    };


    public boolean isAddT1(TextView textView) {
        boolean isAdd = false;
        for (int i = 0; i < textViewListT1.size(); i++) {
            if (textViewListT1.get(i) == textView) {
                isAdd = true;
                break;
            }
        }
        return isAdd;
    }

    public boolean isAddT2(TextView textView) {
        boolean isAdd = false;
        for (int i = 0; i < textViewListT2.size(); i++) {
            if (textViewListT2.get(i) == textView) {
                isAdd = true;
                break;
            }
        }
        return isAdd;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_t1_op_1:
                checkT1Answer(activityDualBinding.textT1Op1);
                break;
            case R.id.text_t1_op_2:
                checkT1Answer(activityDualBinding.textT1Op2);
                break;
            case R.id.text_t1_op_3:
                checkT1Answer(activityDualBinding.textT1Op3);
                break;
            case R.id.text_t1_op_4:
                checkT1Answer(activityDualBinding.textT1Op4);
                break;
            case R.id.text_t2_op_1:
                checkT2Answer(activityDualBinding.textT2Op1);
                break;
            case R.id.text_t2_op_2:
                checkT2Answer(activityDualBinding.textT2Op2);
                break;
            case R.id.text_t2_op_3:
                checkT2Answer(activityDualBinding.textT2Op3);
                break;
            case R.id.text_t2_op_4:
                checkT2Answer(activityDualBinding.textT2Op4);
                break;
            case R.id.btn_play:
                if (isPlay) {
                    isPlay = false;
                    activityDualBinding.btnPlay.setImageResource(R.drawable.ic_play);
                    if (isTimerT2 && isTimerT1) {
                        isTimerT2 = false;
                        isTimerT1 = false;
                        countDownTimerT2.cancel();
                        countDownTimerT1.cancel();
                    }
                } else {
                    isPlay = true;
                    activityDualBinding.btnPlay.setImageResource(R.drawable.ic_pause);
                    isTimerT1 = true;
                    isTimerT2 = true;
                    setTimerT2();
                    setTimerT1();
                }
                break;

        }
    }


    public void showResultDialog() {

        if (isTimerT2) {
            countDownTimerT2.cancel();
        }

        if (isTimerT1) {
            countDownTimerT1.cancel();
        }
        isDialogOpen = true;
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View mView = layoutInflaterAndroid.inflate(R.layout.result_pair_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.setCanceledOnTouchOutside(false);

        ImageButton btn_refresh, btn_next;
        TextView text_t1_wrong_question, text_t2_wrong_question, text_t1_true_question, text_t2_true_question, txt_t1_result_string, txt_t2_result_string;

        btn_refresh = mView.findViewById(R.id.btn_refresh);
        btn_next = mView.findViewById(R.id.btn_next);
        text_t1_true_question = mView.findViewById(R.id.text_t1_true_question);
        text_t2_true_question = mView.findViewById(R.id.text_t2_true_question);
        text_t1_wrong_question = mView.findViewById(R.id.text_t1_wrong_question);
        text_t2_wrong_question = mView.findViewById(R.id.text_t2_wrong_question);
        txt_t1_result_string = mView.findViewById(R.id.txt_t1_result_string);
        txt_t2_result_string = mView.findViewById(R.id.txt_t2_result_string);

        text_t1_true_question.setText(String.valueOf(right_t1_count));
        text_t1_wrong_question.setText(String.valueOf(wrong_t1_count));


        text_t2_true_question.setText(String.valueOf(right_t2_count));
        text_t2_wrong_question.setText(String.valueOf(wrong_t2_count));


        if (right_t1_count != right_t2_count) {
            if (right_t1_count > right_t2_count) {
                txt_t1_result_string.setText(getString(R.string.pass));
                txt_t2_result_string.setText(getString(R.string.fail));
            } else {
                txt_t2_result_string.setText(getString(R.string.pass));
                txt_t1_result_string.setText(getString(R.string.fail));
            }
        } else {
            if (secT1 < secT2) {
                txt_t1_result_string.setText(getString(R.string.pass));
                txt_t2_result_string.setText(getString(R.string.fail));
            } else {

                txt_t2_result_string.setText(getString(R.string.pass));
                txt_t1_result_string.setText(getString(R.string.fail));
            }
        }

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSound();
                isDialogOpen = false;
                clearRunnable();
                Intent intent = new Intent(MultiQuizActivity.this, DashboardActivity.class);
                startActivity(intent);
                alertDialogAndroid.dismiss();
            }
        });

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSound();
                isDialogOpen = false;
                clearRunnable();
                recreate();
                overridePendingTransition(0, 0);
                alertDialogAndroid.dismiss();
            }
        });


    }


}
