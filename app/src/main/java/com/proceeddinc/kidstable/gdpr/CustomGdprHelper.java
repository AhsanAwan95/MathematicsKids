package com.proceeddinc.kidstable.gdpr;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.proceeddinc.kidstable.R;
import com.google.ads.consent.AdProvider;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.consent.DebugGeography;


import java.util.List;

public class CustomGdprHelper {

    private final String TAG = CustomGdprHelper.class.getCanonicalName();
    private final Activity context;
    private AlertDialog mEuDialog;


    public CustomGdprHelper(Activity context) {
        this.context = context;
    }

    public void initialise() {
        ConsentInformation consentInformation = ConsentInformation.getInstance(context);

        if (ConsentDialogueConstant.isDebug) {
            ConsentInformation.getInstance(context).addTestDevice(ConsentDialogueConstant.TEST_DEVICE_ID);
            ConsentInformation.getInstance(context).
                    setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);
        }

        String[] publisherIds = {ConsentDialogueConstant.PUBLISHER_ID};
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                if (ConsentDialogueConstant.isDebug)
                    Log.d(TAG, "statusget " + consentStatus);

//                if (ConsentInformation.getInstance(context).isRequestLocationInEeaOrUnknown()) {
//                    Log.d(TAG, "User is from EEA " + consentStatus);

                    // If the returned ConsentStatus is UNKNOWN, Collect the user's consent.
                    if (consentStatus == ConsentStatus.UNKNOWN) {
                        displayConsentDialogue(false);
                    } else if (consentStatus == ConsentStatus.NON_PERSONALIZED) {

                    }


//                }
//                else {
//                    Log.d(TAG, "User is not from EEA " + consentStatus);
//                }

            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                Log.e(TAG, "User's consent status failed to update: " + errorDescription);
            }
        });
    }

    public boolean isConsentRequired() {
        final ConsentInformation consentInformation = ConsentInformation.getInstance(context);
        if (!consentInformation.isRequestLocationInEeaOrUnknown()) {
            Log.d(TAG, "User is outside EEA");
            return false;
        } else {
            return true;
        }
    }

    public void displayConsentDialogue(boolean showCancel) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.Custom_Dialog);
        View eu_consent_dialog = LayoutInflater.from(context).inflate(R.layout.eu_consent, null);

        alertDialog.setView(eu_consent_dialog).setCancelable(false);

        if (showCancel) {
            alertDialog.setPositiveButton(ConsentDialogueConstant.STR_CLOSE, null);
        }

        mEuDialog = alertDialog.create();
        mEuDialog.show();

        Button btnConsentYes = eu_consent_dialog.findViewById(R.id.btnConsentYes);
        Button btnConsentNo = eu_consent_dialog.findViewById(R.id.btnConsentNo);

        btnConsentYes.setText(ConsentDialogueConstant.STR_CONSENT_BUTTON_YES);
        btnConsentNo.setText(ConsentDialogueConstant.STR_CONSENT_BUTTON_NO);
        btnConsentYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEuDialog.cancel();
                Toast.makeText(context, ConsentDialogueConstant.STR_THANKS, Toast.LENGTH_LONG).show();
                ConsentInformation.getInstance(context).setConsentStatus(ConsentStatus.PERSONALIZED);
            }
        });
        btnConsentNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEuDialog.cancel();
                Toast.makeText(context, ConsentDialogueConstant.STR_THANKS, Toast.LENGTH_LONG).show();
                ConsentInformation.getInstance(context).setConsentStatus(ConsentStatus.NON_PERSONALIZED);
            }
        });

        TextView txtConsentDescription1 = eu_consent_dialog.findViewById(R.id.txtConsentDescription1);
        txtConsentDescription1.setText(ConsentDialogueConstant.getConsentDescription1());

        TextView txtConsentDescription2 = eu_consent_dialog.findViewById(R.id.txtConsentDescription2);
        txtConsentDescription2.setText(ConsentDialogueConstant.getConsentDescription2());

        TextView txtConsentDescription3 = eu_consent_dialog.findViewById(R.id.txtConsentDescription3);
        txtConsentDescription3.setText(ConsentDialogueConstant.getConsentDescription3(context));

        TextView txtConsentLearnMore = eu_consent_dialog.findViewById(R.id.txtConsentMoreLearnMore);
        txtConsentLearnMore.setText(ConsentDialogueConstant.getConsentLearnMore(context));
        txtConsentLearnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayConsentLearnMore();
            }
        });


    }

    private void displayConsentLearnMore() {


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.Custom_Dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View eu_consent_dialog = inflater.inflate(R.layout.eu_consent_more, null);

        alertDialog.setView(eu_consent_dialog)
                .setCancelable(false);

        final AlertDialog mEuMoreDialog = alertDialog.create();
        mEuMoreDialog.show();

        // Setup Consent More Dialogue Description 1
        TextView txtConsentMoreDescription1 = eu_consent_dialog.findViewById(R.id.txtConsentMoreDescription1);
        txtConsentMoreDescription1.setText(ConsentDialogueConstant.getStrConsentMoreDescription1(context));

        // Setup App Privacy Policy Link
        TextView txtConsentMoreLearnMore = eu_consent_dialog.findViewById(R.id.txtConsentMoreLearnMore);
        String link = "<a href=" + ConsentDialogueConstant.PRIVACY_URL + ">" + ConsentDialogueConstant.getConsentMoreLearnMore(context) + "</a>";
        txtConsentMoreLearnMore.setText(Html.fromHtml(link));
        txtConsentMoreLearnMore.setTextColor(Color.BLUE);
        txtConsentMoreLearnMore.setMovementMethod(LinkMovementMethod.getInstance());


        // Setup Back Button
        Button btnConsentMoreBack = eu_consent_dialog.findViewById(R.id.btnConsentMoreBack);
        btnConsentMoreBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEuMoreDialog.dismiss();
            }
        });

        // Setup Network Providers GridView
        GridView gridNetworkProviders = eu_consent_dialog.findViewById(R.id.gridNetworkProviders);
        List<AdProvider> adProviders = ConsentInformation.getInstance(context).getAdProviders();
        gridNetworkProviders.setAdapter(new AdProviders(context, adProviders));
    }


    public void resetConsent() {
        ConsentInformation consentInformation = ConsentInformation.getInstance(context);
        consentInformation.reset();
    }

    /**
     * To Prevent Dialogue To Recreating
     */
    public void onDestroy() {
        if (mEuDialog != null && mEuDialog.isShowing()) mEuDialog.cancel();
    }


}
