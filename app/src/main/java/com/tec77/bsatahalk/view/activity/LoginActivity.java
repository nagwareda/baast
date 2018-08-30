package com.tec77.bsatahalk.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.request.ForgetPassRequest;
import com.tec77.bsatahalk.api.request.LoginRequest;
import com.tec77.bsatahalk.api.request.RequestOtherLogin;
import com.tec77.bsatahalk.database.SharedPref;
import com.tec77.bsatahalk.listener.FbLoginFailedListener;
import com.tec77.bsatahalk.utils.CheckConnection;
import com.tec77.bsatahalk.utils.ValidateEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class LoginActivity extends BaseActivity implements View.OnClickListener, FbLoginFailedListener
        , GoogleApiClient.OnConnectionFailedListener {
    private Button loginBtn;
    private LoginButton fbLoginBtn;
    private SignInButton googleLoginBtn;
    private EditText emailETxt, passETxt;
    private TextView forgetPassTxt;
    private LinearLayout newUser;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private DatabaseReference userDbReference;
    private FirebaseAuth mAuth;
    private SharedPref pref;
    private String userEmail;
    private EditText userEmailETxtDialog;
    private String accessToken;
    private ProgressDialog mProgressDialog;
    private boolean fBLoginDone = false;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = new SharedPref(this);
        pref.putString("lan","ar");
        setLocale(getApplicationContext(),"ar");

        if (pref.loggeedIn()) {
           // setLocale(getApplicationContext(),"ar");
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_login);
            initViews();
            actionView();
        }
    }

    private void initViews() {

        userDbReference = FirebaseDatabase.getInstance().getReference().child("users");
        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        loginBtn = findViewById(R.id.LoginActivity_btn_login);
        emailETxt = findViewById(R.id.LoginActivity_EditText_email);
        passETxt = findViewById(R.id.LoginActivity_EditText_Password);
        forgetPassTxt = findViewById(R.id.LoginActivity_textView_forgetPass);
        newUser = findViewById(R.id.LoginActivity_linear_newUser);

        fbLoginBtn = findViewById(R.id.LoginActivity_btn_fbLogin);
        googleLoginBtn = findViewById(R.id.LoginActivity_btn_googleLogin);

        dialog = new ProgressDialog(LoginActivity.this);
        String pleaseWait = getString(R.string.Dialog_please_wait);
        dialog.setMessage(pleaseWait);
        dialog.setCancelable(false);


        handelFBLogin();
        // googleSignOut();

        ValidateEditText validateEditText = new ValidateEditText(this, emailETxt);
        emailETxt = (EditText) validateEditText.ValidateEmail();

    }

    private void handelFBLogin() {

        Log.d("AppLog", "key:" + FacebookSdk.getApplicationSignature(this));

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        fbLoginBtn.setReadPermissions(Arrays.asList("public_profile", "email"));
        // Callback registration
        fbLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                accessToken = loginResult.getAccessToken().getToken().toString();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(final JSONObject object, GraphResponse response) {
                                try {
                                    // Application code
                                    System.out.println("JSONRESPOMSEEE " + response.toString());

                                    String email = object.getString("email");

                                    if (email.isEmpty())
                                        showEmailRequestDialog(object);
                                    else
                                        callFbLoginApi(object);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                Log.v("LoginActivity", "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.v("LoginActivity", exception.getCause().toString());
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    private void actionView() {
        loginBtn.setOnClickListener(this);
      forgetPassTxt.setOnClickListener(this);
        newUser.setOnClickListener(this);
        googleLoginBtn.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        } else
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == loginBtn.getId()) {
            if (emailETxt.getText().toString().isEmpty()||passETxt.getText().toString().isEmpty() || emailETxt.getError() != null)
                Toast.makeText(this, getString(R.string.toast_ensure_data), Toast.LENGTH_SHORT).show();
            else {
                if (CheckConnection.getInstance().checkInternetConnection(this)) {
                    new FastNetworkManger(this).login(prepareLoginRequest());
                } else {
                    Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        } else if (view.getId() == newUser.getId()) {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        } else if (view.getId() == googleLoginBtn.getId()) {
            if (CheckConnection.getInstance().checkInternetConnection(this)) {
                initGoogleSignIn();
                googleSignIn();
            } else {
                Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }

        }else if(view.getId() == forgetPassTxt.getId()){
            showEmailDialog();
        }
    }

    private void showEmailDialog() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.fragment_email_request_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        userEmailETxtDialog = (EditText) mView.findViewById(R.id.EmailRequestDialog_EditTxt_userMail);
        validateDialogEmail();

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

        alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmailETxtDialog.setFocusable(false);
                Boolean wantToCloseDialog = false;
                if (validateDialogEmail())
                    wantToCloseDialog = true;
                //Do stuff, possibly set wantToCloseDialog to true then...
                if (wantToCloseDialog) {
                    userEmail = userEmailETxtDialog.getText().toString();
                    callForgetPassRequest(userEmail);
                    alertDialogAndroid.dismiss();
                }
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });

    }

    private void callForgetPassRequest(String userEmail) {
       ForgetPassRequest body = new ForgetPassRequest();
       body.setEmail(userEmail);
        if (CheckConnection.getInstance().checkInternetConnection(this)) {
            new FastNetworkManger(this).forgetPassRequest(body);
        } else {
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }

    private void initGoogleSignIn() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestServerAuthCode(getString(R.string.google_server_client_id))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private LoginRequest prepareLoginRequest() {
        LoginRequest body = new LoginRequest();
        body.setEmail(emailETxt.getText().toString());
        body.setPassword(passETxt.getText().toString());
        return body;
    }

    private void showEmailRequestDialog(final JSONObject object) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.fragment_email_request_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        userEmailETxtDialog = (EditText) mView.findViewById(R.id.EmailRequestDialog_EditTxt_userMail);
        validateDialogEmail();

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                                LoginManager.getInstance().logOut();
                            }
                        });

        final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

        alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmailETxtDialog.setFocusable(false);
                Boolean wantToCloseDialog = false;
                if (validateDialogEmail())
                    wantToCloseDialog = true;
                //Do stuff, possibly set wantToCloseDialog to true then...
                if (wantToCloseDialog) {
                    userEmail = userEmailETxtDialog.getText().toString();
                    callFbLoginApi(object);
                    alertDialogAndroid.dismiss();
                }
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });

    }


    private boolean validateDialogEmail() {
        userEmailETxtDialog.setFocusable(true);
        ValidateEditText validateEditText = new ValidateEditText(this, userEmailETxtDialog);
        userEmailETxtDialog = (EditText) validateEditText.ValidateEmail();

        if (userEmailETxtDialog.getError() != null)
            return false;
        return true;
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void googleSignOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }



    private void handleGoogleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleGoogleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            String authCode = acct.getServerAuthCode();
            Log.d("code", authCode + "");
            requestAccessToken(authCode, acct);

        } else {

        }
    }

    private void requestAccessToken(String authCode, final GoogleSignInAccount acct) {
        if (CheckConnection.getInstance().checkInternetConnection(LoginActivity.this)) {
            dialog.show();
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("grant_type", "authorization_code")
                    .add("client_id", getString(R.string.google_server_client_id))
                    .add("client_secret", getString(R.string.google_client_sercet))
                    .add("redirect_uri", "")
                    .add("code", authCode)
                    .build();
            final Request request = new Request.Builder()
                    .url("https://www.googleapis.com/oauth2/v4/token")
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(final Request request, final IOException e) {
                    Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    //Log.e(LOG_TAG, e.toString());
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String accessToken = jsonObject.get("access_token").toString();
                        Log.i("access_token", jsonObject.get("access_token").toString());
                        callGoogleLoginAPi(acct, accessToken);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else
            Toast.makeText(this, getString(R.string.timeOut), Toast.LENGTH_SHORT).show();
    }

    private void callFbLoginApi(JSONObject fbResponseJO) {
        if (CheckConnection.getInstance().checkInternetConnection(LoginActivity.this)) {
            dialog.show();
            RequestOtherLogin fbLoginRequest = new RequestOtherLogin();
            fbLoginRequest.setPassword("123456");
            fbLoginRequest.setPhone("-");
            fbLoginRequest.setType("facebook_login");
            fbLoginRequest.setFacebookToken(accessToken.toString());
            Profile profile = Profile.getCurrentProfile();
            String pp = profile.getProfilePictureUri(100, 100).toString();
            fbLoginRequest.setUserImage(profile.getProfilePictureUri(100, 100).toString());
            try {
                fbLoginRequest.setName(fbResponseJO.getString("name"));
                if (fbResponseJO.getString("email").isEmpty())
                    fbLoginRequest.setEmail(userEmail);
                else
                    fbLoginRequest.setEmail(fbResponseJO.getString("email"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            new FastNetworkManger(this).requestFbLogin(fbLoginRequest, this, dialog);
        } else {
            Toast.makeText(this, getString(R.string.timeOut), Toast.LENGTH_SHORT).show();
        }
    }

    private void callGoogleLoginAPi(GoogleSignInAccount acct, String accToken) {
        RequestOtherLogin googleLoginRequest = new RequestOtherLogin();
        googleLoginRequest.setType("gmail_login");
        googleLoginRequest.setEmail(acct.getEmail());
        googleLoginRequest.setName(acct.getDisplayName());
        googleLoginRequest.setPhone("-");
        googleLoginRequest.setPassword("123456");
        googleLoginRequest.setFacebookToken(accToken);
        if (!acct.getPhotoUrl().toString().isEmpty())
            googleLoginRequest.setUserImage(acct.getPhotoUrl().toString());
        else
            googleLoginRequest.setUserImage("");
        new FastNetworkManger(this).requestFbLogin(googleLoginRequest, this, dialog);

    }

    @Override
    public void failedFbLogin(boolean mFbLoginDone) {
        fBLoginDone = mFbLoginDone;
        if (!mFbLoginDone) {
            LoginManager.getInstance().logOut();
            googleSignOut();
        } else {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!fBLoginDone)
            LoginManager.getInstance().logOut();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void setLocale(final Context ctx, String lang) {
        //langBoolean = true;
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale=myLocale;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ctx.createConfigurationContext(config);
            ctx.getResources().updateConfiguration(config, ctx.getResources().getDisplayMetrics());
            // restartActivity();
        }else{
            getApplicationContext().getResources().updateConfiguration(config,null);
        }



    }
}
