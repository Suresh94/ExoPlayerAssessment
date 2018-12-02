package com.example.nextbrain.assessment.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nextbrain.assessment.R;
import com.example.nextbrain.assessment.adapters.MoviesAdapter;
import com.example.nextbrain.assessment.adapters.MoviesSuggestionAdapter;
import com.example.nextbrain.assessment.model.Req;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity implements MoviesSuggestionAdapter.LocationAdapterInterface {
    PlayerView playerView;
    ExoPlayer player;
    RecyclerView rvSuggestions;
    TextView player_title,player_description;
    MoviesSuggestionAdapter moviesAdapter;
    ArrayList<Req> dataList;
    int pos,currentWindow;
    long  playbackPosition;
    boolean playWhenReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        player_description = findViewById(R.id.player_description);
        player_title = findViewById(R.id.player_title);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        pos = args.getInt("position");

        dataList = (ArrayList<Req>) args.getSerializable("ARRAYLIST");

        Log.e("PlayerActivity", new Gson().toJson(dataList));
        rvSuggestions = findViewById(R.id.playerlist);
        rvSuggestions.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        moviesAdapter = new MoviesSuggestionAdapter(dataList, getApplicationContext());
        rvSuggestions.setAdapter(moviesAdapter);
        moviesAdapter.CallLocationAdapterInterface(PlayerActivity.this);

        initializePlayer(pos);
    }

    private void initializePlayer(int position) {

        playerView = findViewById(R.id.video_view);
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);
        Uri uri = Uri.parse(dataList.get(position).getUrl());

        MediaSource mediaSource = buildMediaSource(uri, position);

        player.prepare(mediaSource, true, true);
        player.setPlayWhenReady(playWhenReady);
        player_title.setText(dataList.get(position).getTitle());
        player_description.setText(dataList.get(position).getDescription());
      //  player.seekTo(currentWindow, playbackPosition);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            //initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
       /* hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }*/
    }


    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }


    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
       /* playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);*/
    }

    private MediaSource buildMediaSource(Uri uri, int position) {
        // these are reused for both media sources we create below
        DefaultExtractorsFactory extractorsFactory =
                new DefaultExtractorsFactory();
        DefaultHttpDataSourceFactory dataSourceFactory =
                new DefaultHttpDataSourceFactory("user-agent");

        ExtractorMediaSource videoSource =
                new ExtractorMediaSource.Factory(
                        new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                        createMediaSource(uri);

        Uri audioUri = Uri.parse(dataList.get(position).getUrl());
        ExtractorMediaSource audioSource =
                new ExtractorMediaSource.Factory(
                        new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                        createMediaSource(audioUri);
        return new ConcatenatingMediaSource(audioSource, videoSource);
    }

    @Override
    public void selectedCountryValue(int position) {
        releasePlayer();
        initializePlayer(position);
    }
}
