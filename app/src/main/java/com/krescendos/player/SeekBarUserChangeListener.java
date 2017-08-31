package com.krescendos.player;

import android.widget.SeekBar;

public class SeekBarUserChangeListener implements SeekBar.OnSeekBarChangeListener {

    private TrackPlayer trackPlayer;

    public SeekBarUserChangeListener(TrackPlayer trackPlayer) {
        this.trackPlayer = trackPlayer;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        trackPlayer.setDragging(true);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        trackPlayer.setDragging(false);
        trackPlayer.seekTo(seekBar.getProgress());
    }
}
