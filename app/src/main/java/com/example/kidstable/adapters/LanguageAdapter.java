package com.example.kidstable.adapters;

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
import com.example.kidstable.utils.Constants;

import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {


    private List<String> languageList;
    private Context context;
    private OnLanguageClick onLanguageClick;

    public LanguageAdapter(Context context, List<String> languageList, OnLanguageClick onLanguageClick) {
        this.context = context;
        this.languageList = languageList;
        this.onLanguageClick = onLanguageClick;
    }

    @NonNull
    @Override
    public LanguageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_language, parent, false);
        return new ViewHolder(view);
    }


    public interface OnLanguageClick {

        void onClick(String s);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageAdapter.ViewHolder holder, int position) {
        holder.txt_language.setText(languageList.get(position));
        String path = "flag_" +
                Constants.getLanguageCodeFromLanguage(context, languageList.get(position)) + ".png";
        holder.img_flag.setImageBitmap(Constants.getBitmapFromAsset(context, "flag/" + path));

        if (Constants.getLanguageName(context).equalsIgnoreCase(languageList.get(position))) {
            holder.cell.setBackgroundResource(R.drawable.language_bg);
        } else {
            holder.cell.setBackgroundResource(0);

        }
    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_flag;
        LinearLayout cell;
        TextView txt_language;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_language = itemView.findViewById(R.id.txt_language);
            img_flag = itemView.findViewById(R.id.img_flag);
            cell = itemView.findViewById(R.id.cell);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (onLanguageClick != null) {
                        onLanguageClick.onClick(languageList.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
}
