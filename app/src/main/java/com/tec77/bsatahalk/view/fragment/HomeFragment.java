package com.tec77.bsatahalk.view.fragment;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.database.SharedPref;
import com.tec77.bsatahalk.listener.ResponseHomeVideoListener;
import com.tec77.bsatahalk.model.MainCategoryRecyclerModel;
import com.tec77.bsatahalk.utils.CheckConnection;
import com.tec77.bsatahalk.utils.Const;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

import static com.tec77.bsatahalk.utils.Const.YOUTUBE_API_KEY;


public class HomeFragment extends BaseFragment implements View.OnClickListener,
        YouTubePlayer.OnInitializedListener, ResponseHomeVideoListener, YouTubePlayer.OnFullscreenListener {


    private TextView title, ediomFirsr, ediomSecond, vedioTitleTxt;
    private LinearLayout networkFailedLinearLayout, btnLinear, txtLinear;
    private Button refreshConnection;
    private SharedPref sharedPref;
    private View view;
    private ArrayList<MainCategoryRecyclerModel> categoryList = new ArrayList<>();
    private ImageView e3rabLinear, emla2Linear, classLinear, topTenLinear;
    private final String API_KEY = Const.YOUTUBE_API_KEY;
    private String VIDEO_ID, VIDEO_TITLE;
    private YouTubePlayer youTubePlayer1;
    private RotateLoading loading;
    YouTubePlayerSupportFragment youTubePlayerFragment;
    private static final int RECOVERY_REQUEST = 1;
    private boolean youtubeInitSuccess,vedioRequestSuccess;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        //initYoutube();
        actionViews();
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(YOUTUBE_API_KEY, this);
        }
    }

    private void initView() {
        title = getActivity().findViewById(R.id.HomeActivity_TextView_title);
        title.setText(getString(R.string.nav_Home));
        btnLinear = view.findViewById(R.id.HomeFragment_linear_btns);
        //txtLinear = view.findViewById(R.id.TopTenFragment_linear_txts);
        networkFailedLinearLayout = view.findViewById(R.id.HomeFragment_LinearLayout_NetworkFailed);
        refreshConnection = view.findViewById(R.id.HomeFragment_btn_refreshConnection);
        e3rabLinear = view.findViewById(R.id.HomeFragment_linear_e3raab);
        emla2Linear = view.findViewById(R.id.HomeFragment_linear_emlaa);
        classLinear = view.findViewById(R.id.HomeFragment_linear_class);
        topTenLinear = view.findViewById(R.id.HomeFragment_linear_topTen);
        ediomFirsr = view.findViewById(R.id.HomeFragment_ediom_first);
        ediomFirsr.setText("النحوُ أفضَلُ مَا يُقرا وَيقتبَسُ" + "\t* " + "لِأنَّهُ لِكِتَابِ اللّه يُلتَمَسُ");

        ediomSecond = view.findViewById(R.id.HomeFragment_ediom_second);
        ediomSecond.setText("إذَا الفَتَى عَرَفَ الإعرَابَ كَانَ لَهُ" + "\t* " + "مَهَابَةٌ لِأُناسٍ حَولَهُ جَلَسُوا");

        loading = view.findViewById(R.id.HomeFragment_RotateLoading_loading);

        vedioTitleTxt = view.findViewById(R.id.HomeFragment_TextView_videoTitle);
        initCategoryList();

        initYoutube();
        callVideoContentRequest();


    }

    private void callVideoContentRequest() {
        if (CheckConnection.getInstance().checkInternetConnection(getActivity())) {
            networkFailedLinearLayout.setVisibility(View.GONE);
            new FastNetworkManger(getActivity()).getHomeVideoContent(this, loading);
        } else {
            networkFailedLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initYoutube() {

        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_layout, youTubePlayerFragment).commit();
        youTubePlayerFragment.initialize(API_KEY, this);

    }

    private void initCategoryList() {
        categoryList.add(new MainCategoryRecyclerModel(R.drawable.earaab, getString(R.string.serial)));
        categoryList.add(new MainCategoryRecyclerModel(R.drawable.emlaa, getString(R.string.emlaa_grammer)));
        categoryList.add(new MainCategoryRecyclerModel(R.drawable.classes, getString(R.string.classes_title)));
        categoryList.add(new MainCategoryRecyclerModel(R.drawable.top_ten_splash, getString(R.string.topTen)));
        // initCategoryListAdapter();
    }

    private void actionViews() {
        refreshConnection.setOnClickListener(this);
        emla2Linear.setOnClickListener(this);
        e3rabLinear.setOnClickListener(this);
        classLinear.setOnClickListener(this);
        topTenLinear.setOnClickListener(this);

    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubePlayerFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == e3rabLinear.getId()) {
            replaceFragmentFromCategory(new SerialListFragment(), "SerialListFragment");

        } else if (view.getId() == emla2Linear.getId()) {
            replaceFragmentFromCategory(new Emla2SerialFragment(), "Emla2SerialFragment");

        } else if (view.getId() == classLinear.getId()) {
            replaceFragmentFromCategory(new CategoryFragment(), "Emla2SerialFragment");

        } else if (view.getId() == topTenLinear.getId()) {
            replaceFragmentFromCategory(new TopTenFragment(), "TopTenFragment");

        }

    }

    public void replaceFragmentFromCategory(Fragment fragment, String tag) {
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.HomeActivity_FrameLayout_container, fragment)
                .addToBackStack(tag)
                .commit();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.setPlaybackEventListener(new MyPlaybackEventListener());
        youTubePlayer1 = youTubePlayer;
        if (!b) {
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            youtubeInitSuccess =true;
            if(vedioRequestSuccess)
                youTubePlayer.cueVideo(VIDEO_ID);
            //youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
            // youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE | youTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);

            //youTubePlayer1.cueVideo(VIDEO_ID);
            //youTubePlayer.play();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(getActivity(), RECOVERY_REQUEST).show();
        } else {
            String errorMessage = youTubeInitializationResult.toString();
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
            Log.d("errorMessage:", errorMessage);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (youTubePlayer1 != null) {
            youTubePlayer1.release();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(youTubePlayer1!= null && !youTubePlayer1.isPlaying())
//            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (youTubePlayer1 != null)
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                youTubePlayer1.setFullscreen(false);
        // youTubeView.initialize(YOUTUBE_API_KEY, this);
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
    public void videoContent(String videoTitle, String videoUrl) {
       // VIDEO_ID = videoUrl;
        String[] stringYoutubeUrlArray = videoUrl.split("&");
        VIDEO_ID = stringYoutubeUrlArray[0];
        vedioTitleTxt.setText(videoTitle);
        vedioRequestSuccess= true;
        if(youtubeInitSuccess)
        youTubePlayer1.cueVideo(VIDEO_ID);

    }

    @Override
    public void onFullscreen(boolean b) {
//        if (b)
//            youTubePlayer1.play();
//        if(b &&  getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
//                && !youTubePlayer1.isPlaying()){
//           youTubePlayer1.play();
//        }
//        if(!b &&  getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
//                && !youTubePlayer1.isPlaying()){
//            youTubePlayer1.play();
//        }

    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            // Called when playback starts, either due to user action or call to play().
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                youTubePlayer1.setFullscreen(true);


        }

        @Override
        public void onPaused() {
            // Called when playback is paused, either due to user action or call to pause().

        }

        @Override
        public void onStopped() {
            // Called when playback stops for a reason other than being paused.
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

}