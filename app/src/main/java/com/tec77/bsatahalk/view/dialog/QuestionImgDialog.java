package com.tec77.bsatahalk.view.dialog;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.glide.GlideImageLoader;
import com.github.piasy.biv.view.BigImageView;
import com.squareup.picasso.Picasso;
import com.tec77.bsatahalk.R;

public class QuestionImgDialog extends DialogFragment {

    private BigImageView questionImg;
    private String questionImgStr;

    public QuestionImgDialog() {
    }
    @SuppressLint("ValidFragment")
    public QuestionImgDialog(String order_bill) {
        questionImgStr = order_bill;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BigImageViewer.initialize(GlideImageLoader.with(getActivity().getApplicationContext()));
        View view = inflater.inflate(R.layout.question_img_dialog, container, false);
        initView(view);
        loadImage(questionImgStr);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = (getResources().getDisplayMetrics().heightPixels)-50;
        getDialog().getWindow().setLayout(width, height);
       // getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setTitle(null);
    }

    private void initView(View view) {

        questionImg =  view.findViewById(R.id.QuestionImgDialog_ImageView_qestionImg);

    }

    private void loadImage(String urlImage) {
        questionImg.showImage(Uri.parse(urlImage));
        questionImg.setInitScaleType(BigImageView.INIT_SCALE_TYPE_CENTER_CROP);
//        Picasso.with(getActivity()).load(urlImage)
//                .placeholder(R.color.whiteColor)
//                .error(R.color.whiteColor)
//                .into(questionImg);
    }
}
