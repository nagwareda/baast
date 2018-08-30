package com.tec77.bsatahalk.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.facebook.login.LoginManager;
import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.request.AddQuizDegreeRequest;
import com.tec77.bsatahalk.api.request.ContactUsRequest;
import com.tec77.bsatahalk.api.request.ForgetPassRequest;
import com.tec77.bsatahalk.api.request.LessonRateRequest;
import com.tec77.bsatahalk.api.request.LoginRequest;
import com.tec77.bsatahalk.api.request.PushFireBaseTokenRequest;
import com.tec77.bsatahalk.api.request.RegistrationRequest;
import com.tec77.bsatahalk.api.request.RequestChangePassword;
import com.tec77.bsatahalk.api.request.RequestEditProfile;
import com.tec77.bsatahalk.api.request.RequestOtherLogin;
import com.tec77.bsatahalk.api.request.UpdateFireBaseTokenRequest;
import com.tec77.bsatahalk.api.response.GeneralResponse;
import com.tec77.bsatahalk.api.response.LoginResponse;
import com.tec77.bsatahalk.api.response.Quiz.QuizResponse;
import com.tec77.bsatahalk.api.response.ResponseAllQuestion;
import com.tec77.bsatahalk.api.response.ResponseChooseQuestion;
import com.tec77.bsatahalk.api.response.ResponseGetAnswer;
import com.tec77.bsatahalk.api.response.ResponseHomeVideo;
import com.tec77.bsatahalk.api.response.ResponseLessonConclusion;
import com.tec77.bsatahalk.api.response.SchoolClassesResponse;
import com.tec77.bsatahalk.api.response.SerialListResponse;
import com.tec77.bsatahalk.api.response.TopTenResponse;
import com.tec77.bsatahalk.database.SharedPref;
import com.tec77.bsatahalk.listener.AllQuestionsListner;
import com.tec77.bsatahalk.listener.AnswerQuestionListner;
import com.tec77.bsatahalk.listener.FbLoginFailedListener;
import com.tec77.bsatahalk.listener.LessonConclusionResponseListener;
import com.tec77.bsatahalk.listener.LessonQuizResponseListener;
import com.tec77.bsatahalk.listener.LessonRateResponseListener;
import com.tec77.bsatahalk.listener.ProfileResponseListener;
import com.tec77.bsatahalk.listener.QuizDegreeResponseListener;
import com.tec77.bsatahalk.listener.ResponseChooseQuestionListener;
import com.tec77.bsatahalk.listener.ResponseHomeVideoListener;
import com.tec77.bsatahalk.listener.SchoolClassResponseListener;
import com.tec77.bsatahalk.listener.SerialListResponseListener;
import com.tec77.bsatahalk.listener.TopTenListResponseListener;
import com.tec77.bsatahalk.view.activity.HomeActivity;
import com.tec77.bsatahalk.view.activity.LoginActivity;
import com.tec77.bsatahalk.view.activity.splashSliderActivity;
import com.victor.loading.rotate.RotateLoading;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static com.tec77.bsatahalk.utils.Const.HOST;


/**
 * Created by Nagwa on 23/02/2018.
 */

public class FastNetworkManger {

    private Context context;
    private SharedPref sharedPref;

    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build();


    public FastNetworkManger(Context context) {
        this.context = context;
        sharedPref = SharedPref.getInstance(context);
    }

    public void signUp(final RegistrationRequest requestBody) {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(context);
        String pleaseWait = context.getString(R.string.Dialog_please_wait);
        dialog.setMessage(pleaseWait);
        dialog.show();
        dialog.setCancelable(false);
        String url = HOST + "signup";
        AndroidNetworking.post(url)
                .addApplicationJsonBody(requestBody)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsObject(GeneralResponse.class, new ParsedRequestListener<GeneralResponse>() {
                    @Override
                    public void onResponse(GeneralResponse response) {
                        if (response.isSuccess()) {
                            Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context, LoginActivity.class));
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorBody() != null && anError.getErrorBody().contains("this email is already found"))
                            Toast.makeText(context, context.getString(R.string.not_valid_email), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(context, context.getString(R.string.timeOut), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });
    }

    public void login(final LoginRequest requestBody) {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(context);
        String pleaseWait = context.getString(R.string.Dialog_please_wait);
        dialog.setMessage(pleaseWait);
        dialog.show();
        dialog.setCancelable(false);
        String url = HOST + "signin";
        AndroidNetworking.post(url)
                .addApplicationJsonBody(requestBody)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsObject(LoginResponse.class, new ParsedRequestListener<LoginResponse>() {
                    @Override
                    public void onResponse(LoginResponse response) {
                        if (response.isSuccess()) {
                            sharedPref.putInt("id", response.getStudent().getId());
                            sharedPref.putString("userName", response.getStudent().getName());
                            sharedPref.putString("profile", response.getStudent().getUser_image());
                            sharedPref.putString("token", "Bearer " + response.getStudent().getToken());
                            sharedPref.putBoolean("otherLogin",false);
                            sharedPref.setLoggedin(true);
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 404 && anError.getErrorBody().contains("invalid email or password"))
                            Toast.makeText(context, context.getString(R.string.invalid_email_or_pass), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(context, context.getString(R.string.timeOut), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });
    }

    public void getTopTen(final TopTenListResponseListener callBack, final RotateLoading loading) {
        loading.start();
        String url = HOST + "getTopTen";
        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Authorization", sharedPref.getString("token"))
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsObject(TopTenResponse.class, new ParsedRequestListener<TopTenResponse>() {
                    @Override
                    public void onResponse(TopTenResponse response) {
                        if (response.isSuccess()) {
                            callBack.topTenList(response.getTopTen());
                            loading.stop();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        //Toast.makeText(context, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        if (anError.getErrorBody() != null && anError.getErrorBody().contains("token_expired")) {
                            jumpToLogin();
                        } else
                            Toast.makeText(context, context.getString(R.string.timeOut), Toast.LENGTH_SHORT).show();
                        loading.stop();

                    }
                });
    }

    public void getSerialList(int page, int limit, final SerialListResponseListener callBack,
                              final RotateLoading loading, String listType) {
//        final ProgressDialog dialog;
//        dialog = new ProgressDialog(context);
//        String pleaseWait = context.getString(R.string.Dialog_please_wait);
//        dialog.setMessage(pleaseWait);
//        dialog.show();
        //dialog.setCancelable(false);
        loading.start();
        String url = HOST + listType + "?page=" + page + "&limit=" + limit;
        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .addHeaders("Authorization", sharedPref.getString("token"))
                .build()
                .getAsObject(SerialListResponse.class, new ParsedRequestListener<SerialListResponse>() {

                    @Override
                    public void onResponse(SerialListResponse response) {
                        if (response.isSuccess()) {
                            callBack.serialList(response.getData(), response.getPageCount());
                            loading.stop();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorBody() != null && anError.getErrorBody().contains("token_expired")) {
                            jumpToLogin();
                        } else
                            Toast.makeText(context, context.getString(R.string.timeOut), Toast.LENGTH_SHORT).show();
                        loading.stop();

                    }
                });
    }

    public void getSchoolClass(final SchoolClassResponseListener callBack, final RotateLoading loading) {
//        final ProgressDialog dialog;
//        dialog = new ProgressDialog(context);
//        String pleaseWait = context.getString(R.string.Dialog_please_wait);
//        dialog.setMessage(pleaseWait);
//        dialog.show();
//        dialog.setCancelable(false);
        loading.start();
        String url = HOST + "categorys";
        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .addHeaders("Authorization", sharedPref.getString("token"))
                .build()
                .getAsObject(SchoolClassesResponse.class, new ParsedRequestListener<SchoolClassesResponse>() {
                    @Override
                    public void onResponse(SchoolClassesResponse response) {
                        if (response.isSuccess()) {
                            callBack.categories(response.getCategory());
                            loading.stop();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorBody() != null && anError.getErrorBody().contains("token_expired")) {
                            jumpToLogin();
                        } else
                            Toast.makeText(context, context.getString(R.string.timeOut), Toast.LENGTH_SHORT).show();
                        loading.stop();

                    }
                });

    }


    public void addRate(final LessonRateRequest requestBody, final LessonRateResponseListener callBack) {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(context);
        String pleaseWait = context.getString(R.string.Dialog_please_wait);
        dialog.setMessage(pleaseWait);
        // dialog.show();
        dialog.setCancelable(false);
        String url = HOST + "rateLesson";
        AndroidNetworking.post(url)
                .addApplicationJsonBody(requestBody)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .addHeaders("Authorization", sharedPref.getString("token"))
                .build()
                .getAsObject(GeneralResponse.class, new ParsedRequestListener<GeneralResponse>() {
                    @Override
                    public void onResponse(GeneralResponse response) {
                        if (response.isSuccess()) {
                            callBack.responseLessonRate(requestBody.getDegree());
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorBody() != null && anError.getErrorBody().contains("token_expired")) {
                            jumpToLogin();
                        } else
                            Toast.makeText(context, context.getString(R.string.timeOut), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });

    }


    public void requestFbLogin(RequestOtherLogin requestBody, final FbLoginFailedListener callBack, final ProgressDialog dialog)
    {
        String url = HOST + "facebookSignin";
        AndroidNetworking.post(url)
                .addApplicationJsonBody(requestBody)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(LoginResponse.class, new ParsedRequestListener<LoginResponse>() {
                    @Override
                    public void onResponse(LoginResponse response) {
                        if (response.isSuccess()) {
                            //Toast.makeText(context, context.getString(R.string.ratedSuccessfully), Toast.LENGTH_SHORT).show();
                            sharedPref.putInt("id", response.getStudent().getId());
                            sharedPref.putString("userName", response.getStudent().getName());
                            sharedPref.putString("profile", response.getStudent().getUser_image());
                            sharedPref.putString("token", "Bearer " + response.getStudent().getToken());
                            sharedPref.setLoggedin(true);
                            //mark to facebook or google login
                            sharedPref.putBoolean("otherLogin",true);
                            callBack.failedFbLogin(true);
                            if (dialog != null)
                                dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (dialog != null)
                            dialog.dismiss();
                        if (anError.getErrorBody() != null && anError.getErrorBody().contains("this email is already found"))
                            Toast.makeText(context, "this email is already found", Toast.LENGTH_SHORT).show();
                        else if (anError.getErrorBody() != null && anError.getErrorBody().contains("token_expired")) {
                            jumpToLogin();
                        } else
                            Toast.makeText(context, context.getString(R.string.error_fb_login), Toast.LENGTH_SHORT).show();
                        callBack.failedFbLogin(false);
                        //dialog.dismiss();
                    }
                });

    }

    public void getProfile(final ProfileResponseListener callBack) {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(context);
        String pleaseWait = context.getString(R.string.Dialog_please_wait);
        dialog.setMessage(pleaseWait);
        dialog.show();
        dialog.setCancelable(false);
        String url = HOST + "users/" + sharedPref.getInt("id");
        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .addHeaders("Authorization", sharedPref.getString("token"))
                .build()
                .getAsObject(LoginResponse.class, new ParsedRequestListener<LoginResponse>() {
                    @Override
                    public void onResponse(LoginResponse response) {
                        if (response.isSuccess()) {
                            callBack.profileResponse(response.getStudent());
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorBody() != null && anError.getErrorBody().contains("token_expired")) {
                            jumpToLogin();
                        } else
                            Toast.makeText(context, context.getString(R.string.timeOut), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });
    }

    public void getYearList(int page, int limit, final SerialListResponseListener callBack,
                            final RotateLoading loading, int categoryId, int yearId, int semester) {
        loading.start();
        String url = HOST + "getStudentList/category/" + categoryId + "/year/" + yearId + "/term/" + semester +
                "?page=" + page + "&limit=" + limit;
        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Authorization", sharedPref.getString("token"))
                .build()
                .getAsObject(SerialListResponse.class, new ParsedRequestListener<SerialListResponse>() {

                    @Override
                    public void onResponse(SerialListResponse response) {
                        if (response.isSuccess()) {
                            callBack.serialList(response.getData(), response.getPageCount());
                            loading.stop();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorBody() != null && anError.getErrorBody().contains("token_expired")) {
                           jumpToLogin();
                        } else
                            Toast.makeText(context, context.getString(R.string.timeOut), Toast.LENGTH_SHORT).show();
                        loading.stop();

                    }
                });
    }

    public void getLessonQuiz(final LessonQuizResponseListener callBack, int lessonId, final RotateLoading loading) {

        loading.start();
        String url = HOST + "getLessonQuestion/" + lessonId;
        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Authorization", sharedPref.getString("token"))
                .build()
                .getAsObject(QuizResponse.class, new ParsedRequestListener<QuizResponse>() {

                    @Override
                    public void onResponse(QuizResponse response) {
                        if (response.isSuccess()) {
                            callBack.lessonQuizList(response.getQuiz());
                            loading.stop();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorBody() != null && anError.getErrorBody().contains("token_expired")) {
                            jumpToLogin();
                        } else
                            Toast.makeText(context, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        loading.stop();

                    }
                });

    }

    public void PushFireBaseToken(final PushFireBaseTokenRequest body) {
//        final ProgressDialog dialog;
//        dialog = new ProgressDialog(context);
//        String pleaseWait = context.getString(R.string.Dialog_please_wait);
//        dialog.setMessage(pleaseWait);
//        dialog.show();
//        dialog.setCancelable(false);
        String url = HOST + "pushToken";
        AndroidNetworking.post(url)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Authorization", sharedPref.getString("token"))
                .addApplicationJsonBody(body)
                .build()
                .getAsObject(GeneralResponse.class, new ParsedRequestListener<GeneralResponse>() {
                    @Override
                    public void onResponse(GeneralResponse response) {
                        if (response.isSuccess()) {
                            //Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_SHORT).show();
                            sharedPref.putString("old_token", body.getToken());
                            //callBack.tokenPushed();
                            //dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorBody() != null && anError.getErrorBody().contains("token_expired")) {
                            jumpToLogin();
                        } else
                            Toast.makeText(context, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        //dialog.dismiss();
                    }
                });
    }

    public void updateFireBaseToken(UpdateFireBaseTokenRequest body) {
        String url = HOST + "updatePushToken";
        AndroidNetworking.post(url)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Authorization", sharedPref.getString("token"))
                .addApplicationJsonBody(body)
                .build()
                .getAsObject(GeneralResponse.class, new ParsedRequestListener<GeneralResponse>() {
                    @Override
                    public void onResponse(GeneralResponse response) {
                        if (response.isSuccess()) {

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorBody() != null && anError.getErrorBody().contains("token_expired")) {
                            jumpToLogin();
                        } else
                            Toast.makeText(context, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void deleteFireBaseToken(int id) {
        String url = HOST + "deletePushToken/"+id;
        AndroidNetworking.delete(url)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Authorization", sharedPref.getString("token"))
                .build()
                .getAsObject(GeneralResponse.class, new ParsedRequestListener<GeneralResponse>() {
                    @Override
                    public void onResponse(GeneralResponse response) {
                        if (response.isSuccess()) {

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorBody() != null && anError.getErrorBody().contains("token_expired")) {
                            jumpToLogin();
                        } else
                            Toast.makeText(context, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void addQuizDegree(final AddQuizDegreeRequest body, final QuizDegreeResponseListener callBack) {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(context);
        String pleaseWait = context.getString(R.string.Dialog_please_wait);
        dialog.setMessage(pleaseWait);
        dialog.show();
        dialog.setCancelable(false);
        String url = HOST + "addDegree";
        AndroidNetworking.post(url)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Authorization", sharedPref.getString("token"))
                .addApplicationJsonBody(body)
                .build()
                .getAsObject(GeneralResponse.class, new ParsedRequestListener<GeneralResponse>() {

                    @Override
                    public void onResponse(GeneralResponse response) {
                        if (response.isSuccess()) {
                            callBack.quizDegreeResponse(body.getQuiz_id());
                            Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorBody() != null && anError.getErrorBody().contains("token_expired")) {
                            jumpToLogin();
                        } else if (anError.getErrorBody() != null && anError.getErrorBody().contains("this student  is already take this exam")) {
                            Toast.makeText(context, context.getString(R.string.question_answer_befor), Toast.LENGTH_SHORT).show();
                            callBack.quizDegreeResponse(body.getQuiz_id());
                        } else {
                            Toast.makeText(context, context.getString(R.string.timeOut), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();

                    }
                });


    }

    public void sendMessage(ContactUsRequest body, final EditText messageTxt) {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(context);
        String pleaseWait = context.getString(R.string.Dialog_please_wait);
        dialog.setMessage(pleaseWait);
        dialog.show();
        dialog.setCancelable(false);
        String url = HOST + "studentQuestion";
        AndroidNetworking.post(url)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Authorization", sharedPref.getString("token"))
                .addApplicationJsonBody(body)
                .build()
                .getAsObject(GeneralResponse.class, new ParsedRequestListener<GeneralResponse>() {

                    @Override
                    public void onResponse(GeneralResponse response) {
                        if (response.isSuccess()) {
                            Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_SHORT).show();
                            messageTxt.setText("");
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorBody() != null && anError.getErrorBody().contains("token_expired")) {
                            jumpToLogin();
                        } else {
                            Toast.makeText(context, context.getString(R.string.timeOut), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();

                    }
                });


    }

    private void jumpToLogin() {
        sharedPref.setLoggedin(false);
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(context, splashSliderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void getAllQuestions(final RotateLoading loading, int page, final AllQuestionsListner allQuestionsListner) {
//        final ProgressDialog dialog;
//        dialog = new ProgressDialog(context);
//        String pleaseWait = context.getString(R.string.Dialog_please_wait);
//        dialog.setMessage(pleaseWait);
//        dialog.show();
//        dialog.setCancelable(false);
        loading.start();
        String url = HOST + "studentQuestion/" + sharedPref.getInt("id") + "?page=" + page + "&limit=10";
        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Authorization", sharedPref.getString("token"))
                .build()
                .getAsObject(ResponseAllQuestion.class, new ParsedRequestListener<ResponseAllQuestion>() {
                    @Override
                    public void onResponse(ResponseAllQuestion response) {
                        if (response.isSuccess()) {
                            allQuestionsListner.allQuestions(response.getData(), response.getPageCount());
                            loading.setVisibility(View.GONE);
                            loading.stop();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorBody() != null && anError.getErrorBody().contains("token_expired")) {
                            jumpToLogin();
                        } else {
                            Toast.makeText(context, context.getString(R.string.timeOut), Toast.LENGTH_SHORT).show();
                        }
                        loading.setVisibility(View.GONE);
                        loading.stop();

                    }
                });

    }


    public void getAnswerQuestion(int questionId, final AnswerQuestionListner answerQuestionListner) {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(context);
        String pleaseWait = context.getString(R.string.Dialog_please_wait);
        dialog.setMessage(pleaseWait);
        dialog.show();
        dialog.setCancelable(false);
        String url = HOST + "studentQuestion/" + questionId + "/answer";
        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Authorization", sharedPref.getString("token"))
                .build()
                .getAsObject(ResponseGetAnswer.class, new ParsedRequestListener<ResponseGetAnswer>() {
                    @Override
                    public void onResponse(ResponseGetAnswer response) {
                        if (response.getCode() == 200) {
                            answerQuestionListner.sendQuestion(response.getAnswer());
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorBody() != null && anError.getErrorBody().contains("token_expired")) {
                            jumpToLogin();
                        } else {
                            Toast.makeText(context, context.getString(R.string.timeOut), Toast.LENGTH_SHORT).show();
                        }
//                        loading.setVisibility(View.GONE);
                        dialog.dismiss();

                    }
                });

    }

    public void getLessonConclusion(int lessonId, final LessonConclusionResponseListener callBack, final RotateLoading loading) {
        loading.start();
        String url = HOST + "getLessonPhoto/" + lessonId;
        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Authorization", sharedPref.getString("token"))
                .build()
                .getAsObject(ResponseLessonConclusion.class, new ParsedRequestListener<ResponseLessonConclusion>() {
                    @Override
                    public void onResponse(ResponseLessonConclusion response) {
                        if (response.getCode() == 200) {
                            callBack.lessonConclusion(response.getLessonPhoto());
                            loading.stop();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorBody() != null && anError.getErrorBody().contains("token_expired")) {
                            jumpToLogin();
                        } else {
                            Toast.makeText(context, context.getString(R.string.timeOut), Toast.LENGTH_SHORT).show();
                        }
                        loading.stop();

                    }
                });

    }

    public void getChooseQuestion(int quizId, final ResponseChooseQuestionListener callBack, final RotateLoading loading) {

        loading.start();
        String url = HOST + "getChooseQuestion/" + quizId;
        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Authorization", sharedPref.getString("token"))
                .build()
                .getAsObject(ResponseChooseQuestion.class, new ParsedRequestListener<ResponseChooseQuestion>() {
                    @Override
                    public void onResponse(ResponseChooseQuestion response) {
                        if (response.isSuccess()) {
                            callBack.chooseQuestionList(response.getQuiz());
                            loading.stop();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorBody() != null && anError.getErrorBody().contains("token_expired")) {
                            jumpToLogin();
                        } else {
                            Toast.makeText(context, context.getString(R.string.timeOut), Toast.LENGTH_SHORT).show();
                        }
                        loading.stop();
                    }
                });

    }

    public void editProfile(RequestEditProfile body) {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(context);
        String pleaseWait = context.getString(R.string.Dialog_please_wait);
        dialog.setMessage(pleaseWait);
        dialog.show();
        dialog.setCancelable(false);
        String url = HOST + "updateProfile";
        AndroidNetworking.post(url)
                .setPriority(Priority.MEDIUM)
                .addApplicationJsonBody(body)
                .addHeaders("Authorization", sharedPref.getString("token"))
                .build()
                .getAsObject(LoginResponse.class, new ParsedRequestListener<LoginResponse>() {
                    @Override
                    public void onResponse(LoginResponse response) {
                        if (response.getCode() == 200) {
                            Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            sharedPref.putString("userName", response.getStudent().getName());
                            sharedPref.putString("profile", response.getStudent().getUser_image());
                           // sharedPref.putString("token", "Bearer " + response.getStudent().getToken());
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("from", "profile");
                            context.startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorBody() != null && anError.getErrorBody().contains("token_expired")) {
                            jumpToLogin();
                        } else if (anError.getErrorBody() != null && anError.getErrorBody().contains("this email is already found")) {
                            Toast.makeText(context, context.getString(R.string.used_email), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, context.getString(R.string.timeOut), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();

                    }
                });


    }

    public void changePassword(RequestChangePassword body) {

        final ProgressDialog dialog;
        dialog = new ProgressDialog(context);
        String pleaseWait = context.getString(R.string.Dialog_please_wait);
        dialog.setMessage(pleaseWait);
        dialog.show();
        dialog.setCancelable(false);
        String url = HOST + "changePassword";
        AndroidNetworking.post(url)
                .setPriority(Priority.MEDIUM)
                .addApplicationJsonBody(body)
                .addHeaders("Authorization", sharedPref.getString("token"))
                .build()
                .getAsObject(GeneralResponse.class, new ParsedRequestListener<GeneralResponse>() {
                    @Override
                    public void onResponse(GeneralResponse response) {
                        if (response.getCode() == 200) {
                            Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("from", "profile");
                            context.startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorBody() != null && anError.getErrorBody().contains("token_expired")) {
                            jumpToLogin();
                        } else if (anError.getErrorBody() != null && anError.getErrorBody().contains("password not match")) {
                            Toast.makeText(context, context.getString(R.string.password_not_matched), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, context.getString(R.string.timeOut), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();

                    }
                });

    }

    public void getHomeVideoContent(final ResponseHomeVideoListener callBack,
                                    final RotateLoading loading) {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(context);
        String pleaseWait = context.getString(R.string.Dialog_please_wait);
        dialog.setMessage(pleaseWait);
        dialog.show();
        dialog.setCancelable(true);
        String url = HOST + "getProgram";
        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .addHeaders("Authorization", sharedPref.getString("token"))
                .build()
                .getAsObject(ResponseHomeVideo.class, new ParsedRequestListener<ResponseHomeVideo>() {
                    @Override
                    public void onResponse(ResponseHomeVideo response) {
                        if (response.isSuccess()) {
                            callBack.videoContent(response.getProgram().getVideoText(),response.getProgram().getVideoUrl());
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorBody() != null && anError.getErrorBody().contains("token_expired")) {
                            jumpToLogin();
                        } else
                            Toast.makeText(context, context.getString(R.string.timeOut), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });

    }

    public void forgetPassRequest(ForgetPassRequest body){
        final ProgressDialog dialog;
        dialog = new ProgressDialog(context);
        String pleaseWait = context.getString(R.string.Dialog_please_wait);
        dialog.setMessage(pleaseWait);
        dialog.show();
        dialog.setCancelable(false);
        String url = HOST + "rest-password";
        AndroidNetworking.post(url)
                .setPriority(Priority.MEDIUM)
                .addApplicationJsonBody(body)
                .addHeaders("Authorization", sharedPref.getString("token"))
                .build()
                .getAsObject(GeneralResponse.class, new ParsedRequestListener<GeneralResponse>() {
                    @Override
                    public void onResponse(GeneralResponse response) {
                        if (response.getCode() == 200) {
                            Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorBody() != null && anError.getErrorBody().contains("token_expired")) {
                            jumpToLogin();
                        }else if(anError.getErrorBody().contains("this email is not found")){
                            Toast.makeText(context, context.getString(R.string.email_not_found), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, context.getString(R.string.timeOut), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();

                    }
                });
    }
}

