package com.example.localvideoaudioplayer.ui.folder;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.localvideoaudioplayer.data.MediaStoreRepository;

import java.util.List;

public class FolderListViewModel extends AndroidViewModel {

    private final MediaStoreRepository repository;

    public FolderListViewModel(@NonNull Application application) {
        super(application);
        repository = new MediaStoreRepository(application);
    }

    public LiveData<List<String>> getFolders() {
        return repository.getFolders();
    }

    public void scan() {
        new Thread(repository::scanVideos).start();
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
            if (modelClass.isAssignableFrom(FolderListViewModel.class)) {
                return (T) new FolderListViewModel(application);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}

