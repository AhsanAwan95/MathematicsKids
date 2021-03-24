package com.example.kidstable.data;

import android.content.Context;
import android.util.Log;


import com.example.kidstable.R;
import com.example.kidstable.model.DualModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class RandomData {


    public static List<DualModel> getEasyData(Context context) {
        int min = 0;
        int max = 0;
        List<DualModel> tableModelList = new ArrayList<>();

        ArrayList<Integer> number = new ArrayList<>();
        for (int i = 1; i <= 12; ++i) number.add(i);
        Collections.shuffle(number);


        min = 1;
        max = 8;
        String space = context.getString(R.string.white_space);
        String double_space = context.getString(R.string.white_double_space);


        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            int missing_number = random.nextInt(3);
            int table_no = new Random().nextInt((max - min) + 1) + min;

            Log.e("missing_number", "" + missing_number);

            Log.e("", "" + i);

            int answer = table_no * number.get(i);


            String question;
            if (missing_number == 1) {
                question = space + ":" + space + "*" + double_space + number.get(i) + " = " + answer;

                answer = table_no;
            } else if (missing_number == 2) {
                question = table_no + " *" + space + ":" + space + "= " + answer;

                answer = number.get(i);
            } else if (missing_number == 3) {

                question = table_no + " *" + space + ":" + space + "= " + answer;

                answer = number.get(i);
            } else {
                question = table_no + " *" + double_space + number.get(i) + " =" + space + ":" + space;

            }


            tableModelList.add(new DualModel(String.valueOf((answer + 5)), String.valueOf((answer + 8)), String.valueOf((answer + 11))
                    , String.valueOf(answer), question, missing_number));


        }
        return tableModelList;
    }


    public static List<DualModel> getMediumData(Context context) {
        int min = 0;
        int max = 0;
        List<DualModel> tableModelList = new ArrayList<>();

        ArrayList<Integer> number = new ArrayList<>();
        for (int i = 1; i <= 12; ++i) number.add(i);
        Collections.shuffle(number);


        min = 5;
        max = 20;


        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            int missing_number = random.nextInt(4);
            int table_no = new Random().nextInt((max - min) + 1) + min;

            Log.e("missing_number", "" + missing_number);

            Log.e("", "" + i);

            int answer = table_no * number.get(i);

            String question;


            String space = context.getString(R.string.white_space);
            String double_space = context.getString(R.string.white_double_space);


            if (missing_number == 1) {
                question = space + ":" + space + "* " + number.get(i) + " = " + answer;

                answer = table_no;
            } else if (missing_number == 2) {
                question = table_no + " *" + space + ":" + space + "= " + answer;

                answer = number.get(i);
            } else if (missing_number == 3) {


                question = space + ":" + space + "*" + space + ":" + space + "= " + answer;

                String answer1 = table_no + " * " + number.get(i);

                Log.e("question", "" + question);
                tableModelList.add(new DualModel(((table_no + 1) + " * " + (number.get(i) + 3)), ((table_no + 5) + " * " + (number.get(i) + 2)),
                        ((table_no + 7) + " * " + (number.get(i) + 2))
                        , answer1, question, missing_number));

            } else if (missing_number == 4) {

                question = table_no + " *" + space + ":" + space + "= " + answer;

                answer = number.get(i);
            } else {
                question = table_no + " *" + double_space + number.get(i) + " =" + space + ":" + space;

            }

            if (missing_number != 3) {
                tableModelList.add(new DualModel(String.valueOf((answer + 5)), String.valueOf((answer + 8)), String.valueOf((answer + 11))
                        , String.valueOf(answer), question, missing_number));
            }


        }
        return tableModelList;
    }


    public static List<DualModel> getHardData(Context context) {
        int min = 0;
        int max = 0;
        List<DualModel> tableModelList = new ArrayList<>();

        ArrayList<Integer> number = new ArrayList<>();
        for (int i = 1; i <= 12; ++i) number.add(i);
        Collections.shuffle(number);


        min = 8;
        max = 24;
        String bracketStart = context.getString(R.string.bracket_start);
        String bracketEnd = context.getString(R.string.bracket_end);
        String sign;
        String equal = context.getString(R.string.sign_equal);


        String space = context.getString(R.string.white_space);
        String double_space = context.getString(R.string.white_double_space);


        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            int missing_number = random.nextInt(5);
            int table_no = new Random().nextInt((max - min) + 1) + min;

            Log.e("missing_number", "" + missing_number);

            Log.e("", "" + i);

            int answer = 0;


            String question = null;


            if (missing_number == 5) {
                answer = table_no;
                question = space + ":" + space + "* " + number.get(i) + " = " + (table_no * number.get(i));


                Log.e("table_no", "" + answer);
                tableModelList.add(new DualModel(String.valueOf((answer + 5)), String.valueOf((answer + 8)), String.valueOf((answer + 11))
                        , String.valueOf(answer), question, 5));

            } else if (missing_number == 2) {
                answer = number.get(i);
                question = table_no + " *" + space + ":" + space + "= " + (table_no * number.get(i));


                tableModelList.add(new DualModel(String.valueOf((answer + 5)), String.valueOf((answer + 8)), String.valueOf((answer + 11))
                        , String.valueOf(answer), question, missing_number));


            } else if (missing_number == 3) {

                String answer1 = table_no + " * " + number.get(i);
                question = space + ":" + space + "*" + space + ":" + space + "= " + (table_no * number.get(i));


                Log.e("question", "" + question);
                tableModelList.add(new DualModel(((table_no + 1) + " * " + (number.get(i) + 3)), ((table_no + 5) + " * " + (number.get(i) + 2)),
                        ((table_no + 7) + " * " + (number.get(i) + 2))
                        , answer1, question, missing_number));

            } else if (missing_number == 4) {
                answer = number.get(i);
                question = table_no + " *" + space + ":" + space + "= " + (table_no * number.get(i));


                tableModelList.add(new DualModel(String.valueOf((answer + 5)), String.valueOf((answer + 8)), String.valueOf((answer + 11))
                        , String.valueOf(answer), question, missing_number));


            } else if (missing_number == 1) {


                int missing_sign = random.nextInt(4);
                String white_space = context.getString(R.string.white_double_space);

                if (missing_sign == 1) {
                    answer = (table_no + number.get(i)) * number.get(i);
                    sign = context.getString(R.string.sign_addition);


                } else if (missing_sign == 2) {
                    answer = (table_no - number.get(i)) * number.get(i);
                    sign = context.getString(R.string.sign_subtraction);


                } else if (missing_sign == 4) {

                    answer = (table_no * number.get(i)) * number.get(i);
                    sign = context.getString(R.string.sign_multiplication);

                } else if (missing_sign == 3) {
                    double table_no1 = new Random().nextInt((max - min) + 1) + min;
                    double answer1 = (table_no1 / number.get(i)) * number.get(i);
                    sign = context.getString(R.string.sign_division);

                    Log.e("answer1", "" + answer1);

                    question = bracketStart + table_no1 + sign + number.get(i)
                            + bracketEnd + context.getString(R.string.sign_multiplication) + number.get(i) + equal + context.getString(R.string.sign_question) + white_space;


                    tableModelList.add(new DualModel(getDoubleNumber((answer1 + 5)), getDoubleNumber((answer1 + 8)), getDoubleNumber((answer1 + 11))
                            , getDoubleNumber(answer1), question, missing_number));

                } else {
                    answer = (table_no + number.get(i)) * number.get(i);
                    sign = context.getString(R.string.sign_addition);

                }

                if (missing_sign != 3) {
                    question = bracketStart + table_no + sign + number.get(i)
                            + bracketEnd + context.getString(R.string.sign_multiplication) + number.get(i) + equal + context.getString(R.string.sign_question) + white_space;


                    tableModelList.add(new DualModel(String.valueOf((answer + 5)), String.valueOf((answer + 8)), String.valueOf((answer + 11))
                            , String.valueOf(answer), question, missing_number));

                }


            } else {
                question = table_no + " * " + number.get(i) + " =" + space + ":" + space;
                answer = table_no * number.get(i);

                tableModelList.add(new DualModel(String.valueOf((answer + 5)), String.valueOf((answer + 8)), String.valueOf((answer + 11))
                        , String.valueOf(answer), question, missing_number));

            }

        }
        return tableModelList;
    }


    private static String getDoubleNumber(double value) {
        return String.format(Locale.ENGLISH, "%.01f", value);
    }

}
