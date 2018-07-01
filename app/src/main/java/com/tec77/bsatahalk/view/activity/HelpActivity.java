package com.tec77.bsatahalk.view.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.listener.LessonRateResponseListener;
import com.tec77.bsatahalk.model.MainCategoryRecyclerModel;

import java.util.ArrayList;

import static com.tec77.bsatahalk.utils.Const.YOUTUBE_API_KEY;

public class HelpActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener,
        View.OnClickListener,LessonRateResponseListener {

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private HelpActivity.MyPlayerStateChangeListener playerStateChangeListener;
    private HelpActivity.MyPlaybackEventListener playbackEventListener;
    private YouTubePlayer youTubePlayer1;
//    private RecyclerView topicRecyclerView;
    private Toolbar toolbar;
    private TextView title;
    private ArrayList<MainCategoryRecyclerModel>categoryList = new ArrayList<>();
    private CallbackManager callbackManager;
    private ImageView backImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        initViews();
        actionViews();
    }


    private void initViews() {
        youTubeView = findViewById(R.id.AboutUsActivity_youtube_view);
        youTubeView.initialize(YOUTUBE_API_KEY, this);
        playerStateChangeListener = new MyPlayerStateChangeListener();
        playbackEventListener = new MyPlaybackEventListener();
        backImg = findViewById(R.id.AboutUs_imageView_back);
        callbackManager = CallbackManager.Factory.create();
        toolbar = findViewById(R.id.AboutUs_Toolbar_toolbar);
//        topicRecyclerView = findViewById(R.id.AboutUs_recycler_mainTopic);

        initCategoryList();

    }

    private void initCategoryList() {
        categoryList.add(new MainCategoryRecyclerModel( R.drawable.earaab, getString(R.string.serial)));
        categoryList.add(new MainCategoryRecyclerModel( R.drawable.emlaa, getString(R.string.emlaa_grammer)));
        categoryList.add(new MainCategoryRecyclerModel( R.drawable.classes, getString(R.string.classes_title)));
        categoryList.add(new MainCategoryRecyclerModel( R.drawable.top_ten_splash, getString(R.string.topTen)));
        initCategoryListAdapter();

    }

    private void initCategoryListAdapter() {
       // MainTopicHomeRecyclerAdapter adapter=new MainTopicHomeRecyclerAdapter(categoryList, HelpActivity.this,HelpActivity.this);

//        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(HelpActivity.this, LinearLayoutManager.HORIZONTAL, false);
//        topicRecyclerView.setLayoutManager(horizontalLayoutManager);
        //topicRecyclerView.setAdapter(adapter);
    }

    private void actionViews() {
        backImg.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        if (view.getId() == backImg.getId())
            onBackPressed();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        youTubePlayer1 = youTubePlayer;
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        if (!b) {
           // youTubePlayer.cueVideo(lesson.getUrl()); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
             youTubePlayer.cueVideo("fhWaJi1Hsfo");
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

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

    @Override
    public void responseLessonRate(boolean responseRate) {

    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            // Called when playback starts, either due to user action or call to play().
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                youTubePlayer1.setFullscreen(true);
            //showMessage("Playing");
        }

        @Override
        public void onPaused() {
            // Called when playback is paused, either due to user action or call to pause().
           // showMessage("Paused");
        }

        @Override
        public void onStopped() {
            // Called when playback stops for a reason other than being paused.
           // showMessage("Stopped");
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
