package com.mffa.dev.issbid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class DetailVidActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private static final int REQUEST_NUMBER = 999;
    private YouTubePlayerView mYouTubePlayerView;
    private String urlvid;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_vid);

        urlvid = getIntent().getStringExtra("urlvid");

        mAdView = findViewById(R.id.admob);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice("TEST_EMULATOR")
                .build();
        mAdView.loadAd(adRequest);

        mYouTubePlayerView = (YouTubePlayerView)findViewById(R.id.playeryoutube);
        mYouTubePlayerView.initialize("AIzaSyA5E8Qc7y1j4ID1MBJDKQu4YZccTZGoZ38",this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b){
            youTubePlayer.cueVideo(urlvid);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()){
            youTubeInitializationResult.getErrorDialog(this,REQUEST_NUMBER).show();
        } else {
            String errormessage = String.format(
                    "There was an error intializing the YoutubePlayer (%1$s)",youTubeInitializationResult.toString()
            );
            Toast.makeText(this,errormessage,Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NUMBER){
            mYouTubePlayerView.initialize("AIzaSyA5E8Qc7y1j4ID1MBJDKQu4YZccTZGoZ38",this);
        }
    }
}
