package com.example.kidstable.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidstable.R;
import com.example.kidstable.utils.SubView;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {

    private Context context;
    private int width;
    private speakText speakText;
    private int notifyPosition = -1;
    private int multino;
    private int size;
    private int series_no = -1;

    public interface speakText {
        void onSpeakText(String speakString, String printString, int position);
    }


    public void setSpeakListener(speakText speakListener) {
        this.speakText = speakListener;
    }

    public MainAdapter(Context context, int width, int item_size) {
        this.context = context;
        this.width = width;
        this.size = item_size;
    }

    @NonNull
    @Override
    public MainAdapter.MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_main, parent, false);
        return new MainHolder(view);
    }


    public void getTableNo(int no, int multino, int series_no) {
        notifyPosition = no;
        this.multino = multino;
        this.series_no = series_no;
        notifyDataSetChanged();

    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.MainHolder holder, final int position) {


        SubView subView = new SubView(context, series_no, multino, (position + 1), size, width, new SubView.onSpeakText() {
            @Override
            public void onSpeak(String speakString, String printString, int pos) {
                if (speakText != null) {
                    Log.e("table_no", "" + position);
                    speakText.onSpeakText(speakString, printString, pos);
                    Log.e("position", "" + (position + 1));
                }
            }
        });
        holder.addViews.addView(subView);

        if (notifyPosition == position) {
            Log.e("position", "" + multino);
            subView.getTableNo(multino);
        }


    }

    @Override
    public int getItemCount() {
        return size;
    }

    class MainHolder extends RecyclerView.ViewHolder {

        RelativeLayout addViews;


        MainHolder(@NonNull View itemView) {
            super(itemView);
            addViews = itemView.findViewById(R.id.addView);
        }
    }
}
