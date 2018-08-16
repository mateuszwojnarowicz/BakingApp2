package com.mateusz.bakingapp2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.mateusz.bakingapp2.Model.Recipe;
import com.mateusz.bakingapp2.Model.Step;
import com.mateusz.bakingapp2.Utilities.Constants;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DetailFragment extends Fragment {
    @BindView(R.id.fragment_detail_text_view_title)
    TextView textViewTitle;
    @BindView(R.id.fragment_detail_text_view_description)
    TextView textViewDescription;
    @BindView(R.id.fragment_detail_text_view_step_number)
    TextView textViewStep;
    @BindView(R.id.fragment_detail_button_previous)
    Button buttonPrevoius;
    @BindView(R.id.fragment_detail_button_next)
    Button buttonNext;
    @BindView(R.id.fragment_detail_exo_player)
    SimpleExoPlayerView videoView;

    private Recipe mData;
    private Unbinder mUnbinder = null;
    private int mPosition = 0;
    private Step mStep = null;
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    SharedPreferences sharedPreferences;
    Handler mainHandler;
    SimpleExoPlayer player;

    public DetailFragment() {
        // Required empty public constructor
    }

    public void setData(Recipe data){
        mData=data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        updateFields();

        buttonPrevoius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition--;
                savePosition(mPosition, getActivity());
                player.stop();
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition++;
                savePosition(mPosition, getActivity());
            }
        });
        return rootView;

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(!getActivity().getResources().getBoolean(R.bool.isTablet)){
        // Checking the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //First Hide other objects (listview or recyclerview), better hide them using Gone.
            textViewTitle.setVisibility(View.GONE);
            textViewDescription.setVisibility(View.GONE);
            textViewStep.setVisibility(View.GONE);
            buttonNext.setVisibility(View.GONE);
            buttonPrevoius.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            videoView.setLayoutParams(params);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {


            textViewTitle.setVisibility(View.VISIBLE);
            textViewDescription.setVisibility(View.VISIBLE);
            textViewStep.setVisibility(View.VISIBLE);
            buttonNext.setVisibility(View.VISIBLE);
            buttonPrevoius.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.WRAP_CONTENT;
            videoView.setLayoutParams(params);
        }
    }

    }

    public static void savePosition(int position, Context mContext){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("position", position);
        editor.apply();
    }

    public static int loadPosition(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, context.MODE_PRIVATE);
        int position = sharedPreferences.getInt("position", 0);
        return position;
    }

    private void updateFields(){
        mPosition = loadPosition(getActivity());
        mStep = mData.getListSteps().get(mPosition);
        if(mStep.getStringThumbnailURL().isEmpty()&&mStep.getStringVideoURL().isEmpty()){
            videoView.setVisibility(View.GONE);
        } else {
            if(isOnline()){
                videoView.setVisibility(View.VISIBLE);
                playerInit();
                videoView.setPlayer(player);
                if(!mStep.getStringVideoURL().isEmpty()){
                    player.prepare(getMediaSource(mStep.getStringVideoURL()));
                } else {
                    player.prepare(getMediaSource(mStep.getStringThumbnailURL()));
                }

            }
        }
        if(mPosition==0){
            buttonPrevoius.setActivated(false);
            buttonPrevoius.setVisibility(View.INVISIBLE);
        } else if (mPosition>0&&mPosition<mData.getListSteps().size()-1) {
            buttonPrevoius.setActivated(true);
            buttonNext.setActivated(true);
            buttonPrevoius.setVisibility(View.VISIBLE);
            buttonNext.setVisibility(View.VISIBLE);

        } else {
            buttonNext.setActivated(false);
            buttonNext.setVisibility(View.INVISIBLE);
        }
        textViewTitle.setText(mStep.getStringShortDescription());
        textViewDescription.setText(mStep.getStringDescription());
        textViewStep.setText("STEP "+String.valueOf(mPosition)+"/"+String.valueOf(mData.getListSteps().size()-1));
    }

    private MediaSource getMediaSource(String s){
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                Util.getUserAgent(getActivity(), "BakingApp2"), bandwidthMeter);
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(s));
        return videoSource;
    }



    boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, getActivity().MODE_PRIVATE);
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if(key.equals("position")){
                    updateFields();
                }
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
        mainHandler = new Handler();
    }

    private void playerInit(){
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector =
            new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
        releasePlayer();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void releasePlayer() {
        player.stop();
        player.release();
        player = null;
    }




}
