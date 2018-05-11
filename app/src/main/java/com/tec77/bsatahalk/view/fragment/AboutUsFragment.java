package com.tec77.bsatahalk.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.utils.CheckConnection;

public class AboutUsFragment extends BaseFragment {

    ImageView hamzaLogo;
    View view;
    private Toolbar toolbar;
    private TextView title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view =  inflater.inflate(R.layout.fragment_about_us, container, false);
        title = getActivity().findViewById(R.id.HomeActivity_TextView_title);
        title.setText(getActivity().getString(R.string.nav_aboutUs));
        hamzaLogo = view.findViewById(R.id.ContactUs_img_hamza);
        if(CheckConnection.getInstance().checkInternetConnection(getActivity()))
        hamzaLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.hamza.solutions/"));
                startActivity(browserIntent);
            }
        });
        return view;

    }

}
