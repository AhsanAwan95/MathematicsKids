package com.proceeddinc.kidstable.gdpr;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.proceeddinc.kidstable.R;
import com.google.ads.consent.AdProvider;


import java.util.List;

public class AdProviders extends BaseAdapter {
    private Context mContext;
    private List<AdProvider> adProviders;

    public AdProviders(Context c, List<AdProvider> adProviders) {
        mContext = c;
        this.adProviders = adProviders;
    }

    @Override
    public int getCount() {
        return adProviders.size();
    }

    @Override
    public AdProvider getItem(int position) {
        return adProviders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            view = new View(mContext);
            view = inflater.inflate(R.layout.row_ad_provider, null);
            TextView textView = view.findViewById(R.id.tv_network_provider);
            String link = "<a href=" + adProviders.get(position).getPrivacyPolicyUrlString() + ">" + adProviders.get(position).getName() + "</a>";

            textView.setText(Html.fromHtml(link));
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            view = convertView;
        }

        return view;
    }
}
