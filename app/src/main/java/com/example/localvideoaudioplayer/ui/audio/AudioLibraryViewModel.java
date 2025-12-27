package com.example.localvideoaudioplayer.ui.audio;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.localvideoaudioplayer.data.local.AppDatabase;
import com.example.localvideoaudioplayer.data.local.entity.AudioEntity;

import java.util.List;

public class AudioLibraryViewModel extends AndroidViewModel {
    private final LiveData<List<AudioEntity>> audioFiles;

    public AudioLibraryViewModel(@NonNull Application application) {
        super(application);
        audioFiles = AppDatabase.getInstance(application).audioDao().getAll();
    }

    public LiveData<List<AudioEntity>> getAudioFiles() {
        return audioFiles;
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
            if (modelClass.isAssignableFrom(AudioLibraryViewModel.class)) {
                return (T) new AudioLibraryViewModel(application);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}

