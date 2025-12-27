package com.example.localvideoaudioplayer.ui.player;

import android.app.Application;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.Player;

import com.example.localvideoaudioplayer.player.PlayerManager;

public class PlayerViewModel extends AndroidViewModel {

    private final PlayerManager playerManager;

    public PlayerViewModel(@NonNull Application application) {
        super(application);
        playerManager = new PlayerManager(application);
        playerManager.initialize();
    }

    public void prepare(Uri uri) {
        playerManager.play(uri, new Bundle());
    }

    public Player getPlayer() {
        return playerManager.getPlayer();
    }

    public void toggleAudioOnly() {
        playerManager.setAudioOnly(!playerManager.isAudioOnly());
    }

    public boolean isAudioOnly() {
        return playerManager.isAudioOnly();
    }

    public void pauseIfNeeded() {
        Player player = playerManager.getPlayer();
        if (player.isPlaying()) {
            player.pause();
        }
    }

    @Override
    protected void onCleared() {
        playerManager.release();
        super.onCleared();
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Application application;

        public Factory(Application application) {
            this.application = application;
        }

        @NonNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(PlayerViewModel.class)) {
                return (T) new PlayerViewModel(application);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}

