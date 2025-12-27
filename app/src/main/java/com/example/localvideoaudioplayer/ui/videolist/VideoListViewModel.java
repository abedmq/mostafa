package com.example.localvideoaudioplayer.ui.videolist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.localvideoaudioplayer.data.MediaStoreRepository;
import com.example.localvideoaudioplayer.data.local.entity.VideoEntity;

import java.util.List;

public class VideoListViewModel extends AndroidViewModel {

    private final MediaStoreRepository repository;

    public VideoListViewModel(@NonNull Application application) {
        super(application);
        repository = new MediaStoreRepository(application);
    }

    public LiveData<List<VideoEntity>> getVideos(String folder) {
        return repository.getVideosByFolder(folder);
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
            if (modelClass.isAssignableFrom(VideoListViewModel.class)) {
                return (T) new VideoListViewModel(application);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}

