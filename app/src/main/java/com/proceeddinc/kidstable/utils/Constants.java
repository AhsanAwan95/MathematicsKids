package com.proceeddinc.kidstable.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.proceeddinc.kidstable.R;
import com.proceeddinc.kidstable.modelclasses.Model;
import com.google.gson.Gson;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Constants {

    public static final String TABLE_NO = "tableno";
    public static final String LEVEL_NO = "level_no";
    public static final String MyPref = "MyPref";
    public static final String LEVEL_TYPE = "LEVEL_TYPE";
    private static final String LANGUAGE = "language";
    private static final String LANGUAGE_CODE = "LANGUAGE_CODE";
    public static final String IsSound = "isSound";

    public static void setLevelType(Context context, String s) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LEVEL_TYPE, s);
        editor.apply();
    }

    public static String getLevelType(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LEVEL_TYPE, context.getString(R.string.easy));
    }

    public static void setTime(Context context, long time, String type) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(type, time);

        editor.apply();
    }


    public static long getTime(Context context, String type) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);

        return sharedPreferences.getLong(type, 0);
    }




    public static List<Model> geLevelData(Context context, String type) {
        List<Model> modelList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            SharedPreferences sharedPreferences1 = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences1.getString(type + i, null);
            Model model = gson.fromJson(json, Model.class);

            if (model != null) {
                modelList.add(model);
            }
        }

        return modelList;
    }


    public static void setSound(Context context, boolean isSound) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IsSound, isSound);
        editor.apply();
    }


    public static boolean getSound(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IsSound, true);
    }

    public static void setLanguage(Context context, String s) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LANGUAGE, s);
        editor.apply();
    }


    public static String getLanguageName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LANGUAGE, context.getString(R.string.english));
    }


    public static List<String> getLanguageList(Context context) {

        List<String> languageList = new ArrayList<>();

        languageList.add(context.getString(R.string.english));


        return languageList;
    }




    public static Bitmap getBitmapFromAsset(Context context,String strName) {
        AssetManager assetManager = context.getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(istr);
    }


    public static String getLanguageCodeFromLanguage(Context context, String s) {

        if (s.equalsIgnoreCase(context.getString(R.string.english))) {
            return context.getString(R.string.english_code);
        }


        return null;
    }


    public static void setDefaultLanguage(Activity activity) {
        Locale locale = new Locale(getLanguageCode(activity));
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
    }

    private static String getLanguageCode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LANGUAGE_CODE, context.getString(R.string.english_code));
    }

    public static void setLanguageCode(Context context, String s) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LANGUAGE_CODE, s);
        editor.apply();
    }

}
