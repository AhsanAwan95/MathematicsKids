package com.example.kidstable.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.kidstable.R;
import com.example.kidstable.model.Model;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.List;


public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.ViewHolder> {

    private Context context;
    private List<Model> levelModelList;
    private onClik onClik;


    public void setoperatoinListener(onClik onClik) {
        this.onClik = onClik;
    }

    public interface onClik {
        void onClick(View view, int position, int id);

        void onToast();
    }

    public LevelAdapter(Context context, List<Model> levelModelList) {
        this.context = context;
        this.levelModelList = levelModelList;
    }

    @NonNull
    @Override
    public LevelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_level, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {

        PushDownAnim.setPushDownAnimTo(viewHolder.cell)
                .setScale(PushDownAnim.MODE_STATIC_DP, 5)
                .setDurationPush(PushDownAnim.DEFAULT_PUSH_DURATION)
                .setDurationRelease(PushDownAnim.DEFAULT_RELEASE_DURATION)
                .setInterpolatorPush(PushDownAnim.DEFAULT_INTERPOLATOR)
                .setInterpolatorRelease(PushDownAnim.DEFAULT_INTERPOLATOR);

        Model model = levelModelList.get(i);

        if (model.isRunning || model.isComplete) {

            viewHolder.imageView.setVisibility(View.GONE);
            viewHolder.textView.setVisibility(View.VISIBLE);
            viewHolder.textView.setText("" + model.level);


            viewHolder.cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClik != null) {
                        onClik.onClick(v, i, levelModelList.get(i).level);
                    }
                }
            });


        } else {

            viewHolder.imageView.setVisibility(View.VISIBLE);
            viewHolder.textView.setVisibility(View.GONE);


            viewHolder.cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClik != null) {
                        onClik.onToast();
                    }

                }
            });

        }


    }


    @Override
    public int getItemCount() {
        return levelModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        LinearLayout cell;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
            cell = itemView.findViewById(R.id.bg_cell);

        }
    }
}
