package com.tec77.bsatahalk.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.response.PartModelYear;
import com.tec77.bsatahalk.view.activity.StudentListActivity;

import java.util.ArrayList;

/**
 * Created by Nagwa on 19/02/2018.
 */

public class ClassesRecyclerAdapter extends RecyclerView.Adapter<ClassesRecyclerAdapter.ViewHolder> {
    private Context context;
    private int level;
    private ArrayList<PartModelYear> classList = new ArrayList<>();
    private ClassOnClickListener classOnClickListener;

    public interface ClassOnClickListener {
        void onClassClick(int category, int  year);
    }

    public ClassesRecyclerAdapter(Context mContext, int mLevel, ArrayList<PartModelYear> mClassNumberList,
                                  ClassOnClickListener callback) {
        this.context = mContext;
        this.level = mLevel;
        this.classList = mClassNumberList;
         this.classOnClickListener = callback;
    }

    @Override
    public ClassesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.primary_list_item, parent, false);
        ClassesRecyclerAdapter.ViewHolder holder = new ClassesRecyclerAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.emptyViewHolder();
        String levelNum = classList.get(position).getName();
        holder.classNumber.setText(levelNum);

    }

    @Override
    public int getItemCount() {
        return classList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView classNumber;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void emptyViewHolder() {
            classNumber.setText("-");

        }

        private void initViews(final View itemView) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selected_position = getAdapterPosition();
                     classOnClickListener.onClassClick(level,classList.get(selected_position).getId());
//                    Intent intent = new Intent(context, StudentListActivity.class);
//                    intent.putExtra("category", level);
//                    intent.putExtra("year", classList.get(selected_position).getId());
//                    context.startActivity(intent);
                }
            });
            classNumber = itemView.findViewById(R.id.ClassListActivity_textView_classNumber);
            cardView =itemView.findViewById(R.id.ClassListActivity_cardView_itemCard);

        }


    }
}

