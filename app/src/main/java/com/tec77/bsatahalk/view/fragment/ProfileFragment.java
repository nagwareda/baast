package com.tec77.bsatahalk.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.response.PartModelStudent;
import com.tec77.bsatahalk.database.SharedPref;
import com.tec77.bsatahalk.listener.ProfileResponseListener;
import com.tec77.bsatahalk.utils.CheckConnection;
import com.tec77.bsatahalk.view.activity.ChangePasswordActvity;
import com.tec77.bsatahalk.view.activity.EditProfileActivity;


public class ProfileFragment extends Fragment implements ProfileResponseListener, View.OnClickListener {

    private TextView nameTxt, emailTxt, phoneTxt, quizzesNoTxt, totlaDgreeTxt, title,ediom;
    private LinearLayout networkFailedLinearLayout;
    private Button refreshConnection, forgetPassBtn, editProfileBtn;
    private ImageView profile;
    PartModelStudent profileResponse;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews();
        callProfileRequest();
        return view;
    }

    private void callProfileRequest() {
        if (CheckConnection.getInstance().checkInternetConnection(getActivity())) {
            networkFailedLinearLayout.setVisibility(View.GONE);
            new FastNetworkManger(getActivity()).getProfile(this);
        } else
            networkFailedLinearLayout.setVisibility(View.VISIBLE);
    }


    private void initViews() {
        forgetPassBtn = view.findViewById(R.id.ProfileFragment_Btn_changePass);
        forgetPassBtn.setOnClickListener(this);
        editProfileBtn = view.findViewById(R.id.ProfileFragment_Btn_EditProfile);
        editProfileBtn.setOnClickListener(this);
        nameTxt = view.findViewById(R.id.ProfileFragment_txt_userName);
        emailTxt = view.findViewById(R.id.ProfileFragment_txt_emailTxt);
        phoneTxt = view.findViewById(R.id.ProfileFragment_txt_phoneTxt);
        quizzesNoTxt = view.findViewById(R.id.ProfileFragment_txt_quizesNoTxt);
        totlaDgreeTxt = view.findViewById(R.id.ProfileFragment_txt_totalDegreeTxt);
        networkFailedLinearLayout = view.findViewById(R.id.ProfileFragment_LinearLayout_NetworkFailed);
        refreshConnection = view.findViewById(R.id.ProfileFragment_btn_refreshConnection);
        profile = view.findViewById(R.id.ProfileFragment_image_profile);
        ediom = view.findViewById(R.id.ProfileFragment_txt_ediom);
        ediom.setText("إن الذي ملأ اللغات محاسن جعل الجمال وسره في الضاد"+"\n"+"(الشاعر أحمد شوقي)");
        title = getActivity().findViewById(R.id.HomeActivity_TextView_title);
        title.setText(getActivity().getString(R.string.nav_profile));

        refreshConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callProfileRequest();
            }
        });


    }

    @Override
    public void profileResponse(PartModelStudent responseProfile) {
        profileResponse = responseProfile;
        nameTxt.setText(responseProfile.getName());
        emailTxt.setText(responseProfile.getEmail());
        phoneTxt.setText(responseProfile.getPhone());
        quizzesNoTxt.setText("" + responseProfile.getNumberQuizes() + "");
        totlaDgreeTxt.setText("" + responseProfile.getTotalDegree() + "");

        if (!responseProfile.getUser_image().isEmpty())
            Picasso.with(getActivity())
                    .load(responseProfile.getUser_image())
                    .placeholder(R.drawable.fake_profile)
                    .error(R.color.blackColor)
                    .centerCrop().fit()
                    .into(profile);
//            Glide.with(getActivity())
//                    .load(responseProfile.getUser_image())
//                    .into(profile);

        if (SharedPref.getInstance(getActivity()).getBoolean("otherLogin")) {
            forgetPassBtn.setVisibility(View.GONE);
            editProfileBtn.setVisibility(View.GONE);
        }
//            Picasso.with(getActivity())
//                    .load(responseProfile.getUser_image())
//                    .placeholder(R.drawable.fake_profile)
//                    .error(R.color.blackColor)
//                    .centerCrop().fit()
//                    .into(profile);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == editProfileBtn.getId()) {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            intent.putExtra("profile", profileResponse);
            startActivity(intent);

        } else if (view.getId() == forgetPassBtn.getId()) {
            startActivity(new Intent(getActivity(), ChangePasswordActvity.class));
        }

    }
}
