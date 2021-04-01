package com.proceeddinc.kidstable.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.proceeddinc.kidstable.R;


public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.ViewHolder> {

    private Context context;
    private int selected_pos;
    private setClick setClick;


    public NumberAdapter(Context context) {
        this.context = context;

    }

    public void setClickListener(setClick setClick) {
        this.setClick = setClick;
    }

    public interface setClick {

        void onTableNoClick(int pos);
    }

    public void setSelectedPos(int selected_pos) {
        this.selected_pos = selected_pos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NumberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_number, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NumberAdapter.ViewHolder holder, int position) {

        if (selected_pos == position) {
            holder.textView.setBackgroundResource(R.drawable.select_bg);
            holder.textView.setTextColor(Color.WHITE);
        } else {
            holder.textView.setBackgroundResource(R.drawable.unselect_bg);
            holder.textView.setTextColor(ContextCompat.getColor(context, R.color.txt_color));
        }
        holder.textView.setText(String.valueOf((position + 1)));
    }

    @Override
    public int getItemCount() {
        return 12;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (setClick != null) {
                        setClick.onTableNoClick((getAdapterPosition() + 1));
                    }
                }
            });
        }
    }
}
