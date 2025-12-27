package com.example.localvideoaudioplayer.ui.tasks;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.localvideoaudioplayer.data.local.AppDatabase;
import com.example.localvideoaudioplayer.data.local.entity.DownloadTaskEntity;

import java.util.List;

public class TasksViewModel extends AndroidViewModel {

    private final LiveData<List<DownloadTaskEntity>> tasks;

    public TasksViewModel(@NonNull Application application) {
        super(application);
        tasks = AppDatabase.getInstance(application).downloadTaskDao().getAll();
    }

    public LiveData<List<DownloadTaskEntity>> getTasks() {
        return tasks;
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
            if (modelClass.isAssignableFrom(TasksViewModel.class)) {
                return (T) new TasksViewModel(application);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}

