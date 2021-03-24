package com.example.kidstable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidstable.R;


public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {

    private Context context;
    private int table_no;
    private int count = 6;
    private int t_count = 1;


    public TableAdapter(Context context, int table_no) {
        this.context = context;
        this.table_no = table_no;
    }

    public void setTableNo(int table_no) {
        count = 6;
        t_count = 1;
        this.table_no = table_no;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public TableAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_table, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableAdapter.ViewHolder holder, int position) {

        if (position % 2 == 0) {
            holder.textView.setText(table_no + " * " + t_count + " = " + (table_no * t_count));
            t_count++;
        } else {
            holder.textView.setText(table_no + " * " + (count + 1) + " = " + (table_no * (count + 1)));
            count++;
        }

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
        }
    }
}
