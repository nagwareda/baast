package com.tec77.bsatahalk.view.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tec77.bsatahalk.R;


public class SolveE3rabQuestionDialog extends DialogFragment {

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_solve_e3rab_question_dialog, container, false);
        //getDialog().setTitle(getString(R.string.way_solve_e3rab_question));
        getDialog().setTitle(null);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = (getResources().getDisplayMetrics().heightPixels)/2;
        getDialog().getWindow().setLayout(width, height);
        // getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setTitle(null);
    }
}
