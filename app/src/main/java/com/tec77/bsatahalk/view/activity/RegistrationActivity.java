package com.tec77.bsatahalk.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.request.RegistrationRequest;
import com.tec77.bsatahalk.utils.CheckConnection;
import com.tec77.bsatahalk.utils.ValidateEditText;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends BaseActivity implements View.OnClickListener {

    private EditText emailETxt, userNameETxt, passETxt, confirmPassETxt, phoneETxt;
    private ImageView profilePic;
    private Button saveBtn;
    private Toolbar toolbar;
    private int MY_PERMISSIONS_REQUEST_CAMERA = 2;
    private int MY_PERMISSIONS_REQUEST_READ_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();
        actionViews();
        validationFields();
    }

    private void initViews() {
        emailETxt = findViewById(R.id.RegistrationActivity_EditText_email);
        userNameETxt = findViewById(R.id.RegistrationActivity_EditText_userName);
        passETxt = findViewById(R.id.RegistrationActivity_EditText_Password);
        confirmPassETxt = findViewById(R.id.RegistrationActivity_EditText_confirmPassword);
        saveBtn = findViewById(R.id.RegistrationActivity_btn_register);
        profilePic = findViewById(R.id.RegistrationActivity_image_profile);
        phoneETxt = findViewById(R.id.RegistrationActivity_EditText_phone);
        toolbar = findViewById(R.id.RegistrationActivity_Toolbar_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
    }


    private void actionViews() {
        saveBtn.setOnClickListener(this);
        profilePic.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == saveBtn.getId()) {
            validationFields();
            if (emailETxt.getText().toString().isEmpty() || userNameETxt.getText().toString().isEmpty()
                    || passETxt.getText().toString().isEmpty() || confirmPassETxt.getText().toString().isEmpty()
                    || emailETxt.getError() != null || confirmPassETxt.getError() != null || passETxt.getError() != null)
                Toast.makeText(this, getString(R.string.toast_ensure_data), Toast.LENGTH_SHORT).show();
            else {
                if (CheckConnection.getInstance().checkInternetConnection(this)) {
                    new FastNetworkManger(this).signUp(prepareRegisterRequest());
                } else {
                    Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        } else if (view.getId() == profilePic.getId())
            dialogShowPhoto();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void validationFields() {
        ValidateEditText validateEditText = new ValidateEditText(this, emailETxt);
        emailETxt = (EditText) validateEditText.ValidateEmail();
        ValidateEditText validateEditText1 = new ValidateEditText(this, confirmPassETxt);
        confirmPassETxt = (EditText) validateEditText1.ValidateConfirmPassword(passETxt);

    }


    public void dialogShowPhoto() {

        String takephoto = getString(R.string.add_photo);
        String chooseFromLibrary = getString(R.string.choose_from_library);
        String cancel = getString(R.string.cancel_dialog);
        String addPhoto = getString(R.string.take_photo);
        final CharSequence[] items = {takephoto, chooseFromLibrary, cancel};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(addPhoto);
        final String finalTakephoto = takephoto;
        final String finalChooseFromLibrary = chooseFromLibrary;
        final String finalCancel = cancel;
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(finalTakephoto)) {
                    // to request a permission when the user performs an action on TakePhoto
                    if (ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
                        dialogShowPhoto();
                    } else {
                        startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE),
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                } else if (items[item].equals(finalChooseFromLibrary)) {
                    // to request a permission when the user performs an action on GetPhotoFromGallery
                    if (ContextCompat.checkSelfPermission(RegistrationActivity.this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(RegistrationActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                200);
                        dialogShowPhoto();
                    } else {
                        startActivityForResult(new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), MY_PERMISSIONS_REQUEST_READ_STORAGE);
                    }

                } else if (items[item].equals(finalCancel)) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public String convertImage2Base64() {

        profilePic.buildDrawingCache();
        Bitmap bm = profilePic.getDrawingCache();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos); //bm is the bitmap object
        byte[] b = bos.toByteArray();
        return "data:image/jpeg;base64," + Base64.encodeToString(b, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted!
                    // you may now do the action that requires this permission
                } else {
                    Toast.makeText(this, R.string.deny_perrmission_read_photo, Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            profilePic.setAlpha(1f);
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Matrix mat = new Matrix();
            // mat.postRotate(Integer.parseInt("270"));
            Bitmap bMapRotate = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), mat, true);
            profilePic.setImageBitmap(bMapRotate);
        }
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_STORAGE && resultCode == RESULT_OK && data != null) {
            profilePic.setAlpha(1f);
            Uri selectedImageURI = data.getData();
            Picasso.with(this).load(selectedImageURI).noPlaceholder().centerCrop().fit()
                    .into(profilePic);
        }

    }

    private RegistrationRequest prepareRegisterRequest() {
        RegistrationRequest body = new RegistrationRequest();
        body.setEmail(emailETxt.getText().toString());
        body.setName(userNameETxt.getText().toString());
        body.setPhone(phoneETxt.getText().toString());
        body.setPassword(passETxt.getText().toString());
        body.setUser_image(convertImage2Base64());
        return body;

    }


}
