package com.krescendos.player;

import android.widget.SeekBar;

public class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

    private TrackPlayer trackPlayer;

    public SeekBarChangeListener(TrackPlayer trackPlayer){
        this.trackPlayer = trackPlayer;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        trackPlayer.seekTo(progress);
    }
}
