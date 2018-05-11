package com.tec77.bsatahalk.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import java.util.Map;


public class LoginActivity extends BaseActivity implements View.OnClickListener, FbLoginFailedListener
        , GoogleApiClient.OnConnectionFailedListener {
    private Button loginBtn;
    private LoginButton fbLoginBtn;
    private SignInButton googleLoginBtn;
    private EditText emailETxt, passETxt;
    private TextView forgetPassTxt, help;
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
        if (pref.loggeedIn()) {
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
        //forgetPassTxt = findViewById(R.id.LoginActivity_Text_forgetPass);
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
                                    //String phone = object.getString("phone");
//                                    String name = object.getString("name");
//                                    Profile profile = Profile.getCurrentProfile();
//                                    Uri uri = (Uri) profile.getProfilePictureUri(200, 200);

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

//        facebookLoginCallback = new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//
//                 handleFacebookAccessToken(loginResult.getAccessToken());
//                //accessToken = AccessToken.getCurrentAccessToken();
//                fbProfile = Profile.getCurrentProfile();
////                if (fbProfile != null) {
////                    pref.putString("profile", String.valueOf(fbProfile.getProfilePictureUri(100, 100)).toString());
////                    pref.putString("userName", fbProfile.getName());
////                   // pref.setLoggedin(true);
////                }
//                GraphRequest.newMeRequest(
//                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(JSONObject me, GraphResponse response) {
//                                if (response.getError() != null) {
//                                    // handle error
//                                } else {
//                                    // get email and id of the user
//                                    userEmail = me.optString("email");
//                                    if (userEmail.isEmpty()) {
//                                        showEmailRequestDialog();
//                                    } else
//                                        callFbLoginApi();
//                                    //  String id = me.optString("id");
//
//                                }
//                            }
//                        }).executeAsync();
//
////                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
////                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                startActivity(intent);
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.d("error", error.toString());
//
//            }
//        };
//        fbLoginBtn.registerCallback(callbackManager, facebookLoginCallback);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//            // and the GoogleSignInResult will be available instantly.
//            Log.d(TAG, "Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//            handleGoogleSignInResult(result);
//        } else {
//            // If the user has not previously signed in on this device or the sign-in has expired,
//            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
//            // single sign-on will occur in this branch.
//            showProgressDialog();
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
//                    handleGoogleSignInResult(googleSignInResult);
//                }
//            });
//        }


//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            for (UserInfo profile : currentUser.getProviderData()) {
//                // Id of the provider (ex: google.com)
//                String providerId = profile.getProviderId();
//
//                // UID specific to the provider
//                String uid = profile.getUid();
//
//                // Name, email address, and fake_profile photo Url
//                String name = profile.getDisplayName();
//                String email = profile.getEmail();
//                Uri photoUrl = profile.getPhotoUrl();
//            };
//            //updateUI(currentUser);
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
    }

    @Override
    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    private void actionView() {
        loginBtn.setOnClickListener(this);
//        forgetPassTxt.setOnClickListener(this);
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
            if (emailETxt.getText().toString().isEmpty() || emailETxt.getError() != null )
                Toast.makeText(this, getString(R.string.toast_ensure_data), Toast.LENGTH_SHORT).show();
            else {
                new FastNetworkManger(this).login(prepareLoginRequest());}
        } else if (view.getId() == newUser.getId()) {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        } else if (view.getId() == googleLoginBtn.getId()) {
            initGoogleSignIn();
            googleSignIn();
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

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("FacebookAccessToken", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        //Log.d("authUserId", mAuth.getUid() + "");
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signInWithCredential", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("authCurrUserId", user.getUid());

//                            userDbReference.child(user.getUid());
//                            addUserDataToDB(user);

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signInWithCredential", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void addUserDataToDB(FirebaseUser user) {

        //add fake_profile pic to the user
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName("Jane Q. User")
                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/fake_profile.jpg"))
                .build();

        Map map = new HashMap();
        // put correct values in keys
        map.put("email", user.getEmail());
        map.put("userName", user.getDisplayName());
        map.put("photo", user.getPhotoUrl());
        userDbReference.setValue(map);
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
                        updateUI(false);
                    }
                });
    }


    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
//            btnSignIn.setVisibility(View.GONE);
//            btnSignOut.setVisibility(View.VISIBLE);
//            btnRevokeAccess.setVisibility(View.VISIBLE);
//            llProfileLayout.setVisibility(View.VISIBLE);
        } else {
//            btnSignIn.setVisibility(View.VISIBLE);
//            btnSignOut.setVisibility(View.GONE);
//            btnRevokeAccess.setVisibility(View.GONE);
//            llProfileLayout.setVisibility(View.GONE);
        }
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleGoogleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            String authCode = acct.getServerAuthCode();
            Log.d("code", authCode + "");
            requestAccessToken(authCode, acct);


//            // Signed in successfully, show authenticated UI.
//            GoogleSignInAccount acct = result.getSignInAccount();
//            //Toast.makeText(this, acct.getDisplayName(), Toast.LENGTH_SHORT).show();
//            Log.e(TAG, "display name: " + acct.getDisplayName());
//
//            String personName = acct.getDisplayName();
//            String personPhotoUrl = acct.getPhotoUrl().toString();
//            String email = acct.getEmail();
//
//            Log.e(TAG, "Name: " + personName + ", email: " + email
//                    + ", Image: " + personPhotoUrl);
////            txtName.setText(personName);
////            txtEmail.setText(email);
////            Glide.with(getApplicationContext()).load(personPhotoUrl)
////                    .thumbnail(0.5f)
////                    .crossFade()
////                    .diskCacheStrategy(DiskCacheStrategy.ALL)
////                    .into(imgProfilePic);
//            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
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
            fbLoginRequest.setPassword("-");
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
        googleLoginRequest.setPassword("-");
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
}
