package com.tec77.bsatahalk.view.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.request.LessonRateRequest;
import com.tec77.bsatahalk.api.response.SerialListResponse;
import com.tec77.bsatahalk.database.SharedPref;
import com.tec77.bsatahalk.listener.LessonRateResponseListener;
import com.tec77.bsatahalk.utils.CheckConnection;
import com.tec77.bsatahalk.utils.Const;

import static com.tec77.bsatahalk.utils.Const.YOUTUBE_API_KEY;

//TODO:arabic item xml
public class Lesson_Activity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener,
        View.OnClickListener, LessonRateResponseListener {

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private MyPlayerStateChangeListener playerStateChangeListener;
    private MyPlaybackEventListener playbackEventListener;
    private YouTubePlayer youTubePlayer1;
    private Toolbar toolbar;
    private SerialListResponse.LessonPartModel lesson;
    private TextView title, lessonTitle;
    private LinearLayout underStandLinear, notUnderStandLinear, underStandLinearClicked, notUnderStandLinearClicked;
    private SharedPref pref;
    private Button shareOnFbBtn, startQuizBtn, haveQuestionBtn;
    private CallbackManager callbackManager;
    private LoginManager manager;
    private ShareDialog shareDialog;
    private ImageView backImg;
    private String[] stringYoutubeUrlArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lession_);
        initViews();
        actionViews();

    }

    private void initViews() {
        lesson = (SerialListResponse.LessonPartModel) getIntent().getSerializableExtra("lesson");
        youTubeView = findViewById(R.id.LessonActivity_youtube_view);
        youTubeView.initialize(YOUTUBE_API_KEY, this);
        playerStateChangeListener = new MyPlayerStateChangeListener();
        playbackEventListener = new MyPlaybackEventListener();
        pref = SharedPref.getInstance(this);
        title = findViewById(R.id.LessonActivity_TextView_title);
        title.setText(getString(R.string.lesson));
        lessonTitle = findViewById(R.id.LessonActivity_text_lessonTitle);
        lessonTitle.setText(lesson.getName());
        underStandLinear = findViewById(R.id.LessonActivity_linear_understand);
        underStandLinearClicked = findViewById(R.id.LessonActivity_linear_understand_clicked);
        shareOnFbBtn = findViewById(R.id.LessonActivity_Btn_share);
        backImg = findViewById(R.id.LessonActivity_imageView_back);
        startQuizBtn = findViewById(R.id.LessonActivity_Btn_startQuiz);
//        understandImg = findViewById(R.id.LessonActivity_Img_understand);
//        notUnderstandImg = findViewById(R.id.LessonActivity_Img_notUnderstand);
        notUnderStandLinear = findViewById(R.id.LessonActivity_linear_not_understand);
        notUnderStandLinearClicked = findViewById(R.id.LessonActivity_linear_not_understand_clicked);
        haveQuestionBtn = findViewById(R.id.LessonActivity_Btn_haveQuestion);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        toolbar = findViewById(R.id.LessonActivity_Toolbar_toolbar);
//        Intent intent = new Intent(Lesson_Activity.this, QuizActivity.class);
//        startActivity(intent);
//        getActionBar(toolbar)
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setTitle(null);
    }

    private void actionViews() {
        haveQuestionBtn.setOnClickListener(this);
        underStandLinear.setOnClickListener(this);
        notUnderStandLinear.setOnClickListener(this);
        shareOnFbBtn.setOnClickListener(this);
        startQuizBtn.setOnClickListener(this);
        backImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //LoginManager.getInstance().logInWithPublishPermissions();
        if (view.getId() == underStandLinear.getId()) {
            handelRateRequest(true);
        } else if (view.getId() == notUnderStandLinear.getId())
            handelRateRequest(false);
        else if (view.getId() == shareOnFbBtn.getId()) {
            if (CheckConnection.getInstance().checkInternetConnection(this))
                handelFbShare();
            else
                Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();

        } else if (view.getId() == startQuizBtn.getId()) {
            Const.staticQuestionMarkTxtList.clear();
            Intent intent = new Intent(Lesson_Activity.this, QuizActivity2.class);
            intent.putExtra("lessonId", lesson.getId());
            startActivity(intent);
        } else if (view.getId() == haveQuestionBtn.getId()) {
            Intent intent = new Intent(Lesson_Activity.this, LessonStudentQuestionActivity.class);
//            intent.putExtra("from","contactUs");
            intent.putExtra("lessonName", lesson.getName());
            startActivity(intent);
        } else if (view.getId() == backImg.getId())
            onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (youTubePlayer1 != null) {
            youTubePlayer1.release();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (youTubePlayer1 != null)
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                youTubePlayer1.setFullscreen(false);
        youTubeView.initialize(YOUTUBE_API_KEY, this);
    }

    @Override
    public void onStop() {
        if (youTubePlayer1 != null) {
            youTubePlayer1.release();
        }
        youTubePlayer1 = null;
        super.onStop();
    }

    private void handelFbShare() {
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(Lesson_Activity.this, getString(R.string.shared_success), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(Lesson_Activity.this, getString(R.string.shared_cencel), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Lesson_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        shareVideoOnFaceBook();
//        List<String> permissionNeeds = Arrays.asList("publish_actions");
//        manager = LoginManager.getInstance();
//        manager.logInWithPublishPermissions(this, permissionNeeds);
//        manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Toast.makeText(Lesson_Activity.this, "Facebook login success!", Toast.LENGTH_SHORT).show();
//
//                shareVideoOnFaceBook();
//            }
//
//            @Override
//            public void onCancel() {
//                Toast.makeText(Lesson_Activity.this, "Facebook login canceled!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                Toast.makeText(Lesson_Activity.this, "Facebook login error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

        // Read more: http://mrbool.com/integrating-android-applications-with-facebook/33934#ixzz5AEHnSKXq
    }

    private void handelRateRequest(boolean lessonRate) {
        underStandLinear.setVisibility(View.VISIBLE);
        underStandLinearClicked.setVisibility(View.GONE);
        notUnderStandLinear.setVisibility(View.VISIBLE);
        notUnderStandLinearClicked.setVisibility(View.GONE);
        LessonRateRequest body = new LessonRateRequest();
        body.setLessonId(lesson.getId());
        body.setStudentId(pref.getInt("id"));
        body.setDegree(lessonRate);
        if (CheckConnection.getInstance().checkInternetConnection(this))
            new FastNetworkManger(this).addRate(body, this);
        else
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void responseLessonRate(boolean responseRate) {
        if (responseRate) {
            //notUnderstand.setVisibility(View.GONE);
            underStandLinear.setVisibility(View.GONE);
            underStandLinearClicked.setVisibility(View.VISIBLE);
        } else {
            notUnderStandLinear.setVisibility(View.GONE);
            notUnderStandLinearClicked.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer1 = youTubePlayer;
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        //youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE | youTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);

        if (!b) {
            stringYoutubeUrlArray = lesson.getUrl().split("&");
//            youTubePlayer.cueVideo(lesson.getUrl()); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
            youTubePlayer.cueVideo(stringYoutubeUrlArray[0]);
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            // String error = String.format(getString(R.string.player_error),youTubeInitializationResult.toString());
            //Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(YOUTUBE_API_KEY, this);
        }
        if (callbackManager != null)
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    private void showMessage(String message) {
        //  Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    private void shareVideoOnFaceBook() {

        ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                .setQuote(getString(R.string.for_more) + " \n " + "https://m.facebook.com/basstnhalk/")
                .setContentUrl(handelVideoUrl())
                .build();
        if (shareDialog.canShow(ShareLinkContent.class)) {
            shareDialog.show(shareLinkContent);
        }
//        ShareVideo shareVideo = new ShareVideo.Builder()
//                .setLocalUrl(handelVideoUrl())
//                .build();
//        ShareVideoContent content = new ShareVideoContent.Builder()
//                .setVideo(shareVideo)
//                //.setContentTitle(lesson.getName())
//                .setContentTitle("test")
//                .setContentDescription("بسطتهالك")
//                .build();
//        //shareOnFbBtn.setShareContent(content);
//        ShareApi.share(content, null);
//        // ShareDialog.show(Lesson_Activity.this, content);
    }

    private Uri handelVideoUrl() {
        return Uri.parse("https://www.youtube.com/watch?v=" + lesson.getUrl());
        //return Uri.parse("https://www.youtube.com/watch?v=fhWaJi1Hsfo");
    }


    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            // Called when playback starts, either due to user action or call to play().
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                youTubePlayer1.setFullscreen(true);
            showMessage("Playing");
        }

        @Override
        public void onPaused() {
//            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
//                youTubePlayer1.setFullscreen(false);
            // Called when playback is paused, either due to user action or call to pause().
            showMessage("Paused");
        }

        @Override
        public void onStopped() {
//            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
//                youTubePlayer1.setFullscreen(false);
            // Called when playback stops for a reason other than being paused.
            showMessage("Stopped");
        }

        @Override
        public void onBuffering(boolean b) {
            // Called when buffering starts or ends.
        }

        @Override
        public void onSeekTo(int i) {
            // youTubePlayer1.seekRelativeMillis(i);
            // youTubePlayer1.seekToMillis(i);
            // Called when a jump in playback position occurs, either
            // due to user scrubbing or call to seekRelativeMillis() or seekToMillis()
        }
    }

    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        @Override
        public void onLoading() {
            // Called when the player is loading a video
            // At this point, it's not ready to accept commands affecting playback such as play() or pause()
        }

        @Override
        public void onLoaded(String s) {
            // Called when a video is done loading.
            // Playback methods such as play(), pause() or seekToMillis(int) may be called after this callback.
        }

        @Override
        public void onAdStarted() {
            // Called when playback of an advertisement starts.
        }

        @Override
        public void onVideoStarted() {
            // Called when playback of the video starts.
        }

        @Override
        public void onVideoEnded() {
            // Called when the video reaches its end.
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

            // Called when an error occurs.
        }
    }

}