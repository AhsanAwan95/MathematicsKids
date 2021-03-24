package com.example.kidstable.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;


import com.example.kidstable.R;
import com.example.kidstable.model.TableModel;
import com.example.kidstable.utils.Constants;
import com.example.kidstable.utils.RoundedBackgroundSpan;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


import static com.example.kidstable.utils.Constants.setDefaultLanguage;
import static com.thekhaeng.pushdownanim.PushDownAnim.DEFAULT_PUSH_DURATION;
import static com.thekhaeng.pushdownanim.PushDownAnim.DEFAULT_RELEASE_DURATION;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    private int table_no;
    List<TableModel> tableModelList = new ArrayList<>();
    List<TextView> textViewList = new ArrayList<>();
    TableModel tableModel;
    MediaPlayer mp;
    Handler handler = new Handler();
    boolean isRunnable = false;
    SharedPreferences pref;
    MediaPlayer answerPlayer;
    int quiz_no = 0, wrong_count = 0, right_count = 0;
    TextView txt_header, text_total_question, text_wrong_question, text_true_question, txt_op_1, txt_op_2, txt_op_3, txt_op_4, textView1, textView2, textView3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultLanguage(this);
        setContentView(R.layout.activity_quiz);
        init();
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


    private void init() {
        mp = MediaPlayer.create(this, R.raw.click);
        pref = getSharedPreferences(Constants.MyPref, MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSound();
                clearRunnable();
                finish();
            }
        });

        table_no = getIntent().getIntExtra(Constants.TABLE_NO, 0);

        txt_op_1 = findViewById(R.id.txt_op_1);
        txt_op_2 = findViewById(R.id.txt_op_2);
        txt_op_3 = findViewById(R.id.txt_op_3);
        txt_op_4 = findViewById(R.id.txt_op_4);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        text_total_question = findViewById(R.id.text_total_question);
        text_true_question = findViewById(R.id.text_true_question);
        text_wrong_question = findViewById(R.id.text_wrong_question);
        txt_header = findViewById(R.id.txt_header);
        txt_header.append(" " + table_no);
        text_true_question.setText(String.valueOf(right_count));
        text_wrong_question.setText(String.valueOf(wrong_count));


        txt_op_1.setOnClickListener(this);
        txt_op_2.setOnClickListener(this);
        txt_op_3.setOnClickListener(this);
        txt_op_4.setOnClickListener(this);

        PushDownAnim.setPushDownAnimTo(txt_op_1, txt_op_2, txt_op_3, txt_op_4).
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
        ArrayList<Integer> number = new ArrayList<>();
        for (int i = 1; i <= 12; ++i) number.add(i);
        Collections.shuffle(number);

        String space = getString(R.string.white_space);

        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            int missing_number = random.nextInt(3);


            Log.e("", "" + i);

            int answer = table_no * number.get(i);

            String question;
            if (missing_number == 1) {
                question = space + ":" + space + "* " + number.get(i) + " = " + answer;

                answer = table_no;
            } else if (missing_number == 2) {
                question = table_no + " *" + space + ":" + space + "= " + answer;

                answer = number.get(i);
            } else {
                question = table_no + " * " + number.get(i) + " =" + space + ":" + space;

            }

            tableModelList.add(new TableModel((answer + 5), (answer + 8), (answer + 11), answer, question, missing_number));


        }


    }


    public void addTextView() {
        textViewList.add(txt_op_1);
        textViewList.add(txt_op_2);
        textViewList.add(txt_op_3);
        textViewList.add(txt_op_4);

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


        txt_op_1.setText(String.valueOf(answerList.get(0)));
        txt_op_2.setText(String.valueOf(answerList.get(1)));
        txt_op_3.setText(String.valueOf(answerList.get(2)));
        txt_op_4.setText(String.valueOf(answerList.get(3)));


        String[] separated = tableModel.question.split(":");
        Log.e("separated", "" + separated[0] + " " + separated[1]);
        SpannableString string = new SpannableString(tableModel.question.replace(":", "?"));

        string.setSpan(new RoundedBackgroundSpan(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark)), separated[0].length() - 2, separated[0].length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//        string.setSpan(new RoundedBackgroundSpan(
//                ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark),ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary),(int)getResources().getDimension(R.dimen.text_size)),
//                separated[0].length() - 2, separated[0].length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView1.setText(string, TextView.BufferType.SPANNABLE);


    }

    public void speak(int sound) {
        Log.e("sound", "" + sound);
        pref = getSharedPreferences(Constants.MyPref, MODE_PRIVATE);
        if (pref.getBoolean(Constants.IsSound, true)) {

            if (answerPlayer != null) {
                answerPlayer.release();
            }
            answerPlayer = MediaPlayer.create(getApplicationContext(), sound);

            answerPlayer.start();

        }
    }

    public void checkAnswer(TextView textView) {
        if (isAdd(textView)) {
            int color;
            String[] separated = tableModel.question.split(":");
            SpannableString string;
            if (textView.getText().toString().length() > 1) {
                string = new SpannableString(tableModel.question.replace(":", textView.getText().toString() + " "));
            } else {
                string = new SpannableString(tableModel.question.replace(":", textView.getText().toString()));
            }

            if (Integer.parseInt(textView.getText().toString()) == tableModel.answer) {


                if (isAdd(textView)) {
                    right_count = right_count + 1;
                }
                textViewList.clear();


                speak(R.raw.correct);
                textView.setBackgroundResource(R.drawable.green_bg);
                textView.setTextColor(Color.WHITE);
                if (!isRunnable) {
                    isRunnable = true;
                    handler.postDelayed(runnable, 1000);
                }
                color = ContextCompat.getColor(getApplicationContext(), R.color.green);

            } else {
                speak(R.raw.incorrect);

                textView.setBackgroundResource(R.drawable.red_bg);
                textView.setTextColor(Color.WHITE);
                wrong_count = wrong_count + 1;
                textViewList.remove(textView);

                color = ContextCompat.getColor(getApplicationContext(), R.color.red);
            }

            if (textView.getText().toString().length() > 1) {

                string.setSpan(new RoundedBackgroundSpan(color), separated[0].length() - 2, separated[0].length() + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                string.setSpan(new RoundedBackgroundSpan(color), separated[0].length() - 2, separated[0].length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
            textView1.setText(string, TextView.BufferType.SPANNABLE);


            text_true_question.setText(String.valueOf(right_count));
            text_wrong_question.setText(String.valueOf(wrong_count));
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
        if (isRunnable) {
            isRunnable = false;
            if (quiz_no < tableModelList.size() - 1) {
                quiz_no = quiz_no + 1;
                setQuiz(quiz_no);
            } else {
                showResultDialog();
            }


            handler.removeCallbacks(runnable);
        }

        if (answerPlayer != null) {
            answerPlayer.release();
        }

        if (mp != null) {
            mp.release();
        }


    }

    @Override
    public void onBackPressed() {
        startSound();
        clearRunnable();
        finish();

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isRunnable = false;
            if (quiz_no < tableModelList.size() - 1) {
                quiz_no = quiz_no + 1;
                setQuiz(quiz_no);
            } else {
                showResultDialog();
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
                checkAnswer(txt_op_1);
                break;
            case R.id.txt_op_2:
                checkAnswer(txt_op_2);
                break;
            case R.id.txt_op_3:
                checkAnswer(txt_op_3);
                break;
            case R.id.txt_op_4:
                checkAnswer(txt_op_4);
                break;
        }

    }


    public void showResultDialog() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View mView = layoutInflaterAndroid.inflate(R.layout.result_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.setCanceledOnTouchOutside(false);
        ImageButton btn_refresh, btn_next;
        TextView text_wrong_question, text_true_question, txt_result_string;

        btn_refresh = mView.findViewById(R.id.btn_refresh);
        btn_next = mView.findViewById(R.id.btn_next);
        text_true_question = mView.findViewById(R.id.text_true_question);
        text_wrong_question = mView.findViewById(R.id.text_wrong_question);
        txt_result_string = mView.findViewById(R.id.txt_result_string);


        if (right_count > wrong_count) {
            txt_result_string.setText(getString(R.string.pass));
        } else {
            txt_result_string.setText(getString(R.string.fail));
        }

        text_true_question.setText(String.valueOf(right_count));
        text_wrong_question.setText(String.valueOf(wrong_count));

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSound();
                clearRunnable();
                Intent intent = new Intent(QuizActivity.this, LearnTableActivity.class);
                startActivity(intent);
                alertDialogAndroid.dismiss();
            }
        });

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSound();
                clearRunnable();
                recreate();
                overridePendingTransition(0, 0);
                alertDialogAndroid.dismiss();
            }
        });


    }
}
