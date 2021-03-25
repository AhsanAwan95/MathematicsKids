package com.example.kidstable.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.kidstable.R;
import com.example.kidstable.activity.SingleQuizTypeActivity;
import com.example.kidstable.indicator.DachshundTabLayout;
import com.example.kidstable.utils.Constants;
import com.google.android.material.tabs.TabLayout;


import java.util.Objects;


public class MainQuizFragment extends Fragment {

    private ViewPager viewPager;

    private View view;
    private DachshundTabLayout tabLayout;
    private String level_type;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            level_type = getArguments().getString(Constants.LEVEL_TYPE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.view_activity, container, false);
        init();


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        PagerAdapter viewpageradapter = new PagerAdapter(getChildFragmentManager(), getContext());
        viewPager.setAdapter(viewpageradapter);
        viewpageradapter.notifyDataSetChanged();
        tabLayout.setupWithViewPager(viewPager);

        if (!TextUtils.isEmpty(level_type)) {
            if (level_type.equalsIgnoreCase(getString(R.string.easy))) {
                viewPager.setCurrentItem(0);
                SingleQuizTypeActivity.txt_header.setText(getString(R.string.easy));
            } else if (level_type.equalsIgnoreCase(getString(R.string.medium))) {
                viewPager.setCurrentItem(1);
                SingleQuizTypeActivity.txt_header.setText(getString(R.string.medium));
            } else {
                viewPager.setCurrentItem(2);
                SingleQuizTypeActivity.txt_header.setText(getString(R.string.hard));
            }
        }


        for (int i = 0; i < viewpageradapter.getCount(); i++) {
            View customView = viewpageradapter.getCustomView(getActivity(), i);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(customView);
            }
        }




        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    SingleQuizTypeActivity.txt_header.setText(getString(R.string.easy));
                } else if (position == 1) {
                    SingleQuizTypeActivity.txt_header.setText(getString(R.string.medium));
                } else {
                    SingleQuizTypeActivity.txt_header.setText(getString(R.string.hard));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    private void init() {


        viewPager = view.findViewById(R.id.view);
        tabLayout = view.findViewById(R.id.tab_layout);
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        String string;
        Context context;
        int selected_pos;

        PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new EasyQuizFragment();
                case 1:
                    return new MediumQuizFragment();
                case 2:
                    return new HardQuizFragment();
                default:
                    return null;
            }
        }


        View getCustomView(Context context, int pos) {
            @SuppressLint("InflateParams") View mView = LayoutInflater.from(context).inflate(R.layout.custom_view, null);
            TextView mTextView = mView.findViewById(R.id.textView);
            ImageView imageView = mView.findViewById(R.id.imageView);
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setText(getPageTitle(pos));
            imageView.setImageResource(getImage(pos));

            return mView;
        }

        public void setSelectedPos(int pos) {
            this.selected_pos = pos;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    string = context.getResources().getString(R.string.easy);
                    return string;
                case 1:
                    string = context.getResources().getString(R.string.medium);
                    return string;
                case 2:
                    string = context.getResources().getString(R.string.hard);
                    return string;


            }
            return null;
        }

        public int getImage(int pos) {

            switch (pos) {
                case 0:
                    return R.drawable.easy;
                case 1:

                    return R.drawable.medium;
                case 2:
                    return R.drawable.old;


            }
            return 0;
        }
    }


}
