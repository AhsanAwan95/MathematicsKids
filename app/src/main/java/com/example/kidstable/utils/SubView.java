package com.example.kidstable.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.kidstable.R;


public class SubView extends FrameLayout {


    int size, width;
    LinearLayout layout;
    private int table_no;
    private onSpeakText onSpeakText;
    private int no = -1;
    private int series_no;
    private int multi_no;

    public interface onSpeakText {
        void onSpeak(String speakString, String printString, int position);
    }


    public void getTableNo(int n) {
        this.no = n;
        layout.removeAllViews();
        refreshView();
    }


    public SubView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public SubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    public SubView(Context context, int series_no, int multino, int table_no, int size, int width, onSpeakText onSpeakText) {
        super(context);
        this.series_no = series_no;
        this.multi_no = multino;
        this.table_no = table_no;
        this.size = size;
        this.width = width;
        this.onSpeakText = onSpeakText;
        initView();
    }


    public void refreshView() {
        for (int position = 0; position < size; position++) {


            TextView textView = new TextView(getContext());

            LinearLayout.LayoutParams layoutParams;
            layoutParams = new LinearLayout.LayoutParams(width / size, width / size);

            textView.setLayoutParams(layoutParams);


            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.table_text_color));
            textView.setText(String.valueOf((table_no * (position + 1))));
            if ((table_no - 1) == 0) {
                textView.setTypeface(null, Typeface.BOLD);
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_highlight));

                if (position == 0) {
                    textView.setText("x");
                }
            } else {
                if (position == 0) {
                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_highlight));
                } else {
                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_1_bg));
                }
            }

            final int finalPosition = position;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int new_pos;
                    int multipe;
                    String speakString;
                    String printString;
                    if (onSpeakText != null) {

                        new_pos = finalPosition;

                        if ((new_pos) == 0 && multi_no != 0) {
                            multipe = series_no * table_no;
                            speakString = table_no + " into " + series_no + " equal " + multipe;
                            printString = table_no + " x " + series_no + " = " + multipe;
                        } else {

                            if (table_no == 1) {
                                int ans = multi_no / series_no;

                                multipe = (new_pos + 1) * ans;
                                table_no = ans;
                            } else {

                                multipe = (new_pos + 1) * table_no;
                            }

                            speakString = table_no + " into " + (new_pos + 1) + " equal " + multipe;
                            printString = table_no + " x " + (new_pos + 1) + " = " + multipe;

                        }

                        onSpeakText.onSpeak(speakString, printString, (table_no - 1));

                    }
                }
            });


            if (no != -1) {

                int textViewText;
                if (textView.getText().toString().equalsIgnoreCase("x")) {
                    textViewText = 1;
                } else {
                    textViewText = Integer.parseInt(textView.getText().toString());
                }

                if (no > textViewText) {

                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_green));
                } else if (no == textViewText) {

                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_green_highlight));
                    textView.setTextColor(Color.WHITE);
                    textView.setTypeface(null, Typeface.BOLD);
                }
                if (position == 0) {
                    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_green_highlight));
                    textView.setTextColor(Color.WHITE);
                    textView.setTypeface(null, Typeface.BOLD);
                }
            }


            if (series_no != -1 || no != -1) {


                if (position == (series_no - 1)) {

                    int textViewText;
                    if (textView.getText().toString().equalsIgnoreCase("x")) {
                        textViewText = 1;
                    } else {
                        textViewText = Integer.parseInt(textView.getText().toString());
                    }

                    if (textViewText < multi_no) {

                        if (textViewText == (series_no)) {
                            textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_green_highlight));
                            textView.setTextColor(Color.WHITE);
                            textView.setTypeface(null, Typeface.BOLD);
                        } else {
                            textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_green));
                        }
                    }


                }
            }

            layout.addView(textView);

        }


    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        final View view = inflate(getContext(), R.layout.sub_view, null);
        layout = view.findViewById(R.id.layout);
        refreshView();
        addView(view);
    }
}
