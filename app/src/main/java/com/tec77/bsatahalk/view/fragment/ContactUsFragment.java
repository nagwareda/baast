package com.tec77.bsatahalk.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.request.ContactUsRequest;
import com.tec77.bsatahalk.utils.CheckConnection;


public class ContactUsFragment extends BaseFragment {
    private TextView title;
    private EditText messageTxt;
    private Button sendBtn;
    private View view;
    private String msgLesson = "";

    public ContactUsFragment() {

    }

    @SuppressLint("ValidFragment")
    public ContactUsFragment(String msg) {
        msgLesson = msg;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        title = getActivity().findViewById(R.id.HomeActivity_TextView_title);
        title.setText(getActivity().getString(R.string.contact_us));
        messageTxt = view.findViewById(R.id.ContactUsActivity_TextView_message);
        sendBtn = view.findViewById(R.id.ContactUsActivity_btn_send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CheckConnection.getInstance().checkInternetConnection(getActivity())) {
                    if (messageTxt.getText().toString().isEmpty())
                        Toast.makeText(getActivity(), getActivity().getString(R.string.enter_your_message), Toast.LENGTH_SHORT).show();
                    else {
                        ContactUsRequest request = new ContactUsRequest();
                        request.setQuestion(msgLesson + messageTxt.getText().toString());
                        new FastNetworkManger(getActivity()).sendMessage(request, messageTxt);
                    }
                } else {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}
