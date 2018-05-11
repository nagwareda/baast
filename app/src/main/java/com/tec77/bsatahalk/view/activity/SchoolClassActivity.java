package com.tec77.bsatahalk.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.adapter.ClassesRecyclerAdapter;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.response.PartModelYear;
import com.tec77.bsatahalk.api.response.SchoolClassesResponse;
import com.tec77.bsatahalk.listener.SchoolClassResponseListener;
import com.tec77.bsatahalk.utils.CheckConnection;
import com.tec77.bsatahalk.utils.Const;

import java.util.ArrayList;

public class SchoolClassActivity extends BaseActivity implements SchoolClassResponseListener{

    private RecyclerView primaryRecycler,intermediateRecycler,highLevelRecycler;
    private Toolbar toolbar;
    private ArrayList<PartModelYear>primaryList = new ArrayList<>();
    private ArrayList<PartModelYear>secondList = new ArrayList<>();
    private ArrayList<PartModelYear>highList = new ArrayList<>();
    private ClassesRecyclerAdapter pRecyclerAdapter; // primary recycler
    private ClassesRecyclerAdapter mRecyclerAdapter; // intermediate recycler
    private ClassesRecyclerAdapter hRecyclerAdapter; // high recycler
    private LinearLayout networkFailedLinearLayout;
    private Button refreshConnectionBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes_list);
        intiViews();
    }

    private void intiViews() {
        primaryRecycler  = findViewById(R.id.ClassListActivity_recyclerView_primaryRecycler);
        intermediateRecycler  =findViewById(R.id.ClassListActivity_recyclerView_interMediateRecycler);
        highLevelRecycler = findViewById(R.id.ClassListActivity_recyclerView_highLevelRecycler);
        toolbar = findViewById(R.id.ClassListActivity_Toolbar_toolbar);
//        pRecyclerAdapter = new ClassesRecyclerAdapter(SchoolClassActivity.this,"primary",primaryList,this);
//        mRecyclerAdapter = new ClassesRecyclerAdapter(this,"intermediate",secondList,this);
//        hRecyclerAdapter = new ClassesRecyclerAdapter(this,"high",highList,this);
        networkFailedLinearLayout = findViewById(R.id.ClassListActivity_LinearLayout_NetworkFailed);
        refreshConnectionBtn = findViewById(R.id.ClassListActivity_btn_refreshConnection);
        refreshConnectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDataList();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        initDataList();


    }

    private void initDataList() {
        setAdapters();
        if (CheckConnection.getInstance().checkInternetConnection(this)) {
            networkFailedLinearLayout.setVisibility(View.GONE);
          //  new FastNetworkManger(this).getSchoolClass(this);
        } else {
            networkFailedLinearLayout.setVisibility(View.VISIBLE);
        }


//        primaryList.add(getString(R.string.first));
//        primaryList.add(getString(R.string.second));
//        primaryList.add(getString(R.string.third));
//        primaryList.add(getString(R.string.fourth));
//        primaryList.add(getString(R.string.fifth));
//        primaryList.add(getString(R.string.sixth));
//
//        secondAndHighList.add(getString(R.string.first));
//        secondAndHighList.add(getString(R.string.second));
//        secondAndHighList.add(getString(R.string.third));


    }
    private void setAdapters() {

        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(SchoolClassActivity.this, LinearLayoutManager.HORIZONTAL, false);
        primaryRecycler.setLayoutManager(horizontalLayoutManagaer);

        LinearLayoutManager horizontalLayoutManagaer1
                = new LinearLayoutManager(SchoolClassActivity.this, LinearLayoutManager.HORIZONTAL, false);
        intermediateRecycler.setLayoutManager(horizontalLayoutManagaer1);

        LinearLayoutManager horizontalLayoutManagaer2
                = new LinearLayoutManager(SchoolClassActivity.this, LinearLayoutManager.HORIZONTAL, false);
        intermediateRecycler.setLayoutManager(horizontalLayoutManagaer2);

        primaryRecycler.setAdapter(pRecyclerAdapter);
        intermediateRecycler.setAdapter(mRecyclerAdapter);
        highLevelRecycler.setAdapter(hRecyclerAdapter);
    }

    @Override
    public void categories(ArrayList<SchoolClassesResponse.Category> categoryList) {
        String type;
        PartModelYear years;
        for(int i=0;i<categoryList.size();i++){
            type = categoryList.get(i).getName();
            if(type.contains(Const.CLASS_LIST_RESPONSE_high)){
               highList.clear();
               highList.addAll(categoryList.get(i).getYear());
            }else if(type.contains(Const.CLASS_LIST_RESPONSE_intermediate)){
                secondList.clear();
                secondList.addAll(categoryList.get(i).getYear());
            }else if (type.contains(Const.CLASS_LIST_RESPONSE_primary)){
                primaryList.clear();
                primaryList.addAll(categoryList.get(i).getYear());
            }
        }

        pRecyclerAdapter.notifyDataSetChanged();
        mRecyclerAdapter.notifyDataSetChanged();
        hRecyclerAdapter.notifyDataSetChanged();

    }

    private void checkEmptyList(ArrayList<PartModelYear>list){
        PartModelYear noYear = new PartModelYear();
        noYear.setId(-1);
        noYear.setName(getString(R.string.no_year));
        if(list.isEmpty())
            list.add(noYear);
    }
//    @Override
//    public void onClassClick(String level,PartModelYear year) {
//       // Toast.makeText(this, "level = "+level+" ,year = "+year.getName(), Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(SchoolClassActivity.this,StudentListActivity.class);
//        intent.putExtra("category",level);
//        intent.putExtra("year",year.getId());
//        startActivity(intent);
//
//    }
}
