package com.tec77.bsatahalk.view.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.request.RequestChangePassword;
import com.tec77.bsatahalk.database.SharedPref;
import com.tec77.bsatahalk.utils.CheckConnection;
import com.tec77.bsatahalk.utils.ValidateEditText;
import com.tec77.bsatahalk.view.activity.BaseActivity;

public class ChangePasswordActvity extends BaseActivity implements View.OnClickListener {

    private EditText oldPass, newPass, confNewPass;
    private Button save, refreshBtn;
    private Toolbar toolbar;
    private LinearLayout refreshConnectionLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_actvity);
        initViews();
        actionViews();

    }

    private void initViews() {
        oldPass = findViewById(R.id.ChangePassActivity_EditText_oldPassword);
        newPass = findViewById(R.id.ChangePassActivity_EditText_newPassword);
        confNewPass = findViewById(R.id.ChangePassActivity_EditText_confNewPassword);
        save = findViewById(R.id.ChangePassActivity_btn_save);
        refreshBtn = findViewById(R.id.ChangePassActivity_btn_refreshConnection);
        refreshConnectionLinear = findViewById(R.id.ChangePassActivity_LinearLayout_NetworkFailed);
        toolbar = findViewById(R.id.ChangePassActivity_Toolbar_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
    }

    private void actionViews() {
        save.setOnClickListener(this);
        refreshBtn.setOnClickListener(this);
    }

    private void callChangePassApi() {
        if (CheckConnection.getInstance().checkInternetConnection(this)) {
            refreshConnectionLinear.setVisibility(View.GONE);
            new FastNetworkManger(this).changePassword(prepareRequestBody());
        } else {
            refreshConnectionLinear.setVisibility(View.VISIBLE);
        }
    }

    private RequestChangePassword prepareRequestBody() {
        SharedPref pref = SharedPref.getInstance(this);
        RequestChangePassword body = new RequestChangePassword();
        body.setOldPassword(oldPass.getText().toString());
        body.setNewPassword(newPass.getText().toString());
        body.setUserId(pref.getInt("id"));
        return body;
    }

    private void ValidatePass() {
        ValidateEditText validateEditText1 = new ValidateEditText(this, confNewPass);
        confNewPass = (EditText) validateEditText1.ValidateConfirmPassword(newPass);

        if (!newPass.getText().toString().equals(confNewPass.getText().toString())) {
            if (confNewPass.getError() == null)
                confNewPass.setError(getString(R.string.error_confirm_password));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == save.getId()) {
            ValidatePass();
            if (oldPass.getText().toString().isEmpty() || newPass.getText().toString().isEmpty()
                    || confNewPass.getText().toString().isEmpty() || confNewPass.getError() != null || newPass.getError() != null)
                Toast.makeText(this, getString(R.string.toast_ensure_data), Toast.LENGTH_SHORT).show();
            else {
                callChangePassApi();
            }
        } else if (view.getId() == refreshBtn.getId()) {
            callChangePassApi();
        }

    }
}
