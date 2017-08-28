package com.krescendos.player;

import android.widget.SeekBar;

public class SeekBarUserChangeListener implements SeekBar.OnSeekBarChangeListener {

    private TrackPlayer trackPlayer;

    public SeekBarUserChangeListener(TrackPlayer trackPlayer) {
        this.trackPlayer = trackPlayer;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            trackPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
