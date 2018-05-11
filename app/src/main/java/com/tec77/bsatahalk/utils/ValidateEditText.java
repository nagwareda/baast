package com.tec77.bsatahalk.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.tec77.bsatahalk.R;


/**
 * Created by mosta on 21/9/2017.
 */

public class ValidateEditText {

    private EditText view;
    private Context context;

    public ValidateEditText(Context context, EditText view) {
        this.view = view;
        this.context = context;
    }

    public View ValidateEmail()
    {
        final String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+" +
                "@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+" +
                "[a-zA-Z]{2,}))$";

        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!view.getText().toString().isEmpty()) {
                    if (!view.getText().toString().matches(ePattern)) {
                        view.setError(context.getString(R.string.error_email));
                    }
                }
            }
        });
        return view;
    }



    public View ValidateConfirmPassword(final EditText Password) {
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!view.getText().toString().equals(Password.getText().toString())) {
                    view.setError(context.getString(R.string.error_confirm_password));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return view;
    }

}
