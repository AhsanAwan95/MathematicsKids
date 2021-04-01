package com.proceeddinc.kidstable.gdpr;

import android.content.Context;

import com.proceeddinc.kidstable.R;


public class ConsentDialogueConstant {


    // GLOBAL DECLARATION
    public static final boolean isDebug = true;
    public static final String TEST_DEVICE_ID = "ca-app-pub-3940256099942544/1033173712";
//    public static final String TEST_DEVICE_ID = "<YOUR_TEST_DEVICE_ID>";
    public static final String PUBLISHER_ID = "pub-9297690518647609";
//    public static final String PUBLISHER_ID = "<PUBLISHER_ID_HERE>";
    public static final String PRIVACY_URL = "https://www.google.com/";

    // CONSENT DIALOGUE
    private static final String STR_CONSENT_DESCRIPTION_1 = "We use Google Admob to show ads. Ads support our work, and enable further development of this app. We care about your privacy and data security.";
    private static final String STR_CONSENT_DESCRIPTION_2 = "Can we continue to use your data to tailor ads for be you?";
    public static final String STR_CONSENT_BUTTON_YES= "Yes, continue to show relevant ads";
    public static final String STR_CONSENT_BUTTON_NO = "No, show ads that are irrelevant";
    public static final String STR_CLOSE = "Close";
    public static final String STR_THANKS = "Thank You !";

    public static String getConsentDescription1(){
        return STR_CONSENT_DESCRIPTION_1;
    }

    public static String getConsentDescription2(){
        return STR_CONSENT_DESCRIPTION_2;
    }

    public static String getConsentDescription3(Context context){
        return "You can change your choice anytime for " + context.getResources().getString(R.string.app_name) + " in the app settings. Our partners will collect data and use a unique identifier on your device to show you ads.";
    }

    public static String getConsentLearnMore(Context context){
        return "Learn how " + context.getResources().getString(R.string.app_name) + " and our partners collect and use data.";
    }

    // CONSENT DIALOGUE MORE

    public static String getStrConsentMoreDescription1(Context context){
        return "You can change your choice anytime for " + context.getResources().getString(R.string.app_name) + " in the app settings. Learn how our partners collect and use data.";
    }

    public static String getConsentMoreLearnMore(Context context){
        return "How " + context.getResources().getString(R.string.app_name) + " uses your data";
    }
}
