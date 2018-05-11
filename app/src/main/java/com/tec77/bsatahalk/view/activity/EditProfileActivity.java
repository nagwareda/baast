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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.request.RequestEditProfile;
import com.tec77.bsatahalk.api.response.PartModelStudent;
import com.tec77.bsatahalk.database.SharedPref;
import com.tec77.bsatahalk.utils.CheckConnection;
import com.tec77.bsatahalk.utils.ValidateEditText;

import java.io.ByteArrayOutputStream;

public class EditProfileActivity extends BaseActivity implements View.OnClickListener {
    //this email is already found
    private EditText name, phone, email;
    private Button save, refreshBtn;
    private ImageView profilePic;
    private int MY_PERMISSIONS_REQUEST_CAMERA = 2;
    private int MY_PERMISSIONS_REQUEST_READ_STORAGE = 1;
    private LinearLayout networkFailedLinearLayout, phoneLinear;
    private Toolbar toolbar;
    private PartModelStudent profileResponse;
    SharedPref pref = SharedPref.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initViews();
    }

    private void initViews() {
        profileResponse = (PartModelStudent) getIntent().getSerializableExtra("profile");
        name = findViewById(R.id.EditProfileFragment_txt_userName);
        phone = findViewById(R.id.EditProfileFragment_txt_phoneTxt);
        // email = findViewById(R.id.EditProfileFragment_txt_emailTxt);
        save = findViewById(R.id.EditProfileFragment_Btn_save);
        refreshBtn = findViewById(R.id.EditProfileActivity_btn_refreshConnection);
        profilePic = findViewById(R.id.EditProfileFragment_image_profile);
        networkFailedLinearLayout = findViewById(R.id.EditProfileFragment_LinearLayout_NetworkFailed);
        toolbar = findViewById(R.id.EditProfileActivity_Toolbar_toolbar);
        phoneLinear = findViewById(R.id.EditProfileFragment_phoneLinear);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        save.setOnClickListener(this);
        profilePic.setOnClickListener(this);
        refreshBtn.setOnClickListener(this);

//        if (!CheckConnection.getInstance().checkInternetConnection(this))
//            networkFailedLinearLayout.setVisibility(View.VISIBLE);
        if (profileResponse != null)
            putDataInView();

    }

    private void putDataInView() {
        name.setText(profileResponse.getName());
//        email.setText(profileResponse.getEmail());
        phone.setText(profileResponse.getPhone());

        if (!profileResponse.getUser_image().isEmpty())
            Glide.with(this)
                    .load(profileResponse.getUser_image())
                    .into(profilePic);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == profilePic.getId()) {
            dialogShowPhoto();
        } else if (view.getId() == save.getId()) {
            validationFields();
            if (name.getText().toString().isEmpty() || phone.getText().toString().isEmpty())
                Toast.makeText(this, getString(R.string.toast_ensure_data), Toast.LENGTH_SHORT).show();
            else {
                callEditProfileApi();

            }
        }
    }
//        } else if (view.getId() == refreshBtn.getId())
//            callEditProfileApi();
//    }

    private void callEditProfileApi() {
        if (CheckConnection.getInstance().checkInternetConnection(this)) {
            networkFailedLinearLayout.setVisibility(View.GONE);
            new FastNetworkManger(this).editProfile(prepareObject());
        } else {
            networkFailedLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    private RequestEditProfile prepareObject() {
        RequestEditProfile obj = new RequestEditProfile();
        obj.setUserId(pref.getInt("id"));
        obj.setEmail(profileResponse.getEmail());
        obj.setName(name.getText().toString());
        obj.setPhone(phone.getText().toString());
        obj.setUserImage(convertImage2Base64());
        return obj;
    }

    private void validationFields() {
        ValidateEditText validateEditText = new ValidateEditText(this, email);
//        email = (EditText) validateEditText.ValidateEmail();
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
                    if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
                        dialogShowPhoto();
                    } else {
                        startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE),
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                } else if (items[item].equals(finalChooseFromLibrary)) {
                    // to request a permission when the user performs an action on GetPhotoFromGallery
                    if (ContextCompat.checkSelfPermission(EditProfileActivity.this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(EditProfileActivity.this,
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
        // bitmap = ((BitmapDrawable) profile.getDrawable()).getBitmap();
        profilePic.buildDrawingCache();
        Bitmap bm = profilePic.getDrawingCache();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos); //bm is the bitmap object
        byte[] b = bos.toByteArray();
        return "data:image/jpeg;base64," + Base64.encodeToString(b, 0);
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


}
