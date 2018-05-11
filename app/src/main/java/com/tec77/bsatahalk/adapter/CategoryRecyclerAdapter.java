package com.tec77.bsatahalk.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.response.PartModelYear;
import com.tec77.bsatahalk.api.response.SchoolClassesResponse;

import java.util.ArrayList;

/**
 * Created by Nagwa on 12/03/2018.
 */

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SchoolClassesResponse.Category> classList = new ArrayList<>();
    private ClassesRecyclerAdapter.ClassOnClickListener classOnClickListener;
    private ClassesRecyclerAdapter yearAdapter;

    public interface ClassOnClickListener {
        void onClassClick(String level, PartModelYear year);
    }

    public CategoryRecyclerAdapter(Context mContext, ArrayList<SchoolClassesResponse.Category> mClassNumberList,
                                   ClassesRecyclerAdapter.ClassOnClickListener callBack) {
        this.context = mContext;
        this.classList = mClassNumberList;
        this.classOnClickListener = callBack;
    }

    @Override
    public CategoryRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_year_recycler, parent, false);
        CategoryRecyclerAdapter.ViewHolder holder = new CategoryRecyclerAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(CategoryRecyclerAdapter.ViewHolder holder, int position) {
        holder.emptyViewHolder();
        if (!classList.get(position).getYear().isEmpty()) {
            holder.lienar.setVisibility(View.VISIBLE);
            holder.categoryName.setText(classList.get(position).getName());
            yearAdapter = new ClassesRecyclerAdapter(context, classList.get(position).getId(),
                    classList.get(position).getYear(),classOnClickListener);
            holder.yearRecycler.setAdapter(yearAdapter);
            holder.yearRecycler.setHasFixedSize(true);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            holder.yearRecycler.setLayoutManager(gridLayoutManager);
        }
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryName;
        public RecyclerView yearRecycler;
        public LinearLayout lienar;
        int selected_position;


        public ViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void emptyViewHolder() {
            categoryName.setText("-");
            lienar.setVisibility(View.GONE);

        }

        private void initViews(final View itemView) {
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    selected_position = getAdapterPosition();
//                    //classOnClickListener.onClassClick(level,classList.get(selected_position));
//                }
//            });

            categoryName = itemView.findViewById(R.id.itemCategoryYear_text_yearTxt);
            yearRecycler = itemView.findViewById(R.id.itemCategoryYear_recyclerView_primaryRecycler);
            lienar =itemView.findViewById(R.id.itemCategoryYear_cardView_categoryCard);


        }


    }
}

