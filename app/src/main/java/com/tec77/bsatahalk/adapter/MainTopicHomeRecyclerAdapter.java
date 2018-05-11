package com.tec77.bsatahalk.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.model.MainCategoryRecyclerModel;
import com.tec77.bsatahalk.view.fragment.CategoryFragment;
import com.tec77.bsatahalk.view.fragment.Emla2SerialFragment;
import com.tec77.bsatahalk.view.fragment.SerialListFragment;

import java.util.ArrayList;

/**
 * Created by Nagwa on 16/04/2018.
 */

public class MainTopicHomeRecyclerAdapter extends RecyclerView.Adapter<MainTopicHomeRecyclerAdapter.MyViewHolder> {


   public ArrayList<MainCategoryRecyclerModel> list = new ArrayList<>();
    public Context context;
    public OnHomeItemSelected callBack;


    public MainTopicHomeRecyclerAdapter(ArrayList<MainCategoryRecyclerModel> horizontalList, Context context,OnHomeItemSelected onHomeItemSelected) {
        this.list = horizontalList;
        this.context = context;
        this.callBack = onHomeItemSelected;
    }

    public interface OnHomeItemSelected {
        public void replaceFragmentFromCategory(Fragment fragment);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView txtview;
        public MyViewHolder(View view) {
            super(view);
            imageView= view.findViewById(R.id.ItemMainCategory_ImageView_categoryPhoto);
            txtview= view.findViewById(R.id.ItemMainCategory_TextView_categoryName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = list.get(getAdapterPosition()).txt;
                    if(name.equals(context.getString(R.string.serial))){
                        callBack.replaceFragmentFromCategory(new SerialListFragment());
                    }
                    else if(name.equals(context.getString(R.string.emlaa_grammer))){
                        callBack.replaceFragmentFromCategory(new Emla2SerialFragment());
                    }else if(name.equals(context.getString(R.string.classes_title))){
                        callBack.replaceFragmentFromCategory(new CategoryFragment());
                    }
                }
            });
        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_topic_home_recycler, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.imageView.setImageResource(list.get(position).imageId);
        holder.txtview.setText(list.get(position).txt);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                String list = MainTopicHomeRecyclerAdapter.this.list.get(position).txt.toString();

            }

        });

    }


    @Override
    public int getItemCount()
    {
        return list.size();
    }
}
