package com.tec77.bsatahalk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.response.TopTenResponse;
import com.tec77.bsatahalk.database.SharedPref;

import java.util.ArrayList;

/**
 * Created by Nagwa on 25/02/2018.
 */

public class TopTenListAdapter extends RecyclerView.Adapter<TopTenListAdapter.ViewHolder> {

    private ArrayList<TopTenResponse.TopTenModel> topTenStudentList = new ArrayList<>();
    private Context context;
    private String currentUserId;


    public TopTenListAdapter(ArrayList<TopTenResponse.TopTenModel> topTenStudentList, Context context) {
        this.topTenStudentList = topTenStudentList;
        this.context = context;
        currentUserId = String.valueOf(SharedPref.getInstance(context).getInt("id"));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_ten_list, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.emptyViewHolder();
        TopTenResponse.TopTenModel student = topTenStudentList.get(position);
        holder.studentName.setText(student.getName());
        holder.studentScore.setText(student.getDegree() + "");
        holder.quizzNumberTxt.setText(student.getQuizNumber() + "");
        if (!student.getUser_image().isEmpty())
//            Glide.with(context)
//                    .load(student.getUser_image())
//                    .into(holder.studentImg);
            Picasso.with(context)
                    .load(student.getUser_image())
                    .placeholder(R.drawable.fake_profile)
                    .error(R.drawable.fake_profile)
                    .centerCrop()
                    .fit()
                    .into(holder.studentImg);

    }


    @Override
    public int getItemCount() {
        if (topTenStudentList == null) {
            return 0;
        } else {
            return
                    topTenStudentList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView studentImg;
        public TextView studentName, studentScore, quizzNumberTxt;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void emptyViewHolder() {
            studentName.setText(" ");
            studentScore.setText(" ");
            quizzNumberTxt.setText(" ");
            Picasso.with(context)
                    .load(R.drawable.fake_profile)
                    .placeholder(R.drawable.fake_profile)
                    .error(R.drawable.fake_profile)
                    .centerCrop().fit()
                    .into(studentImg);
        }

        private void initViews(final View itemView) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selected_position = getAdapterPosition();
//                    myOrderItemOnClickListener.onOrderClick(myOrdersList.get(selected_position));
                }
            });
            studentImg = itemView.findViewById(R.id.ItemTopTenList_CircleImageView_profile);
            studentName = itemView.findViewById(R.id.ItemTopTenList_TextView_name);
            studentScore = itemView.findViewById(R.id.ItemTopTenList_TextView_score);
            quizzNumberTxt = itemView.findViewById(R.id.ItemTopTenList_TextView_quizzNumber);

        }

    }
}
