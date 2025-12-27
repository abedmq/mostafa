package com.example.localvideoaudioplayer.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.localvideoaudioplayer.data.local.AppDatabase;
import com.example.localvideoaudioplayer.data.local.dao.VideoDao;
import com.example.localvideoaudioplayer.data.local.entity.VideoEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MediaStoreRepository {
    private final Context context;
    private final VideoDao videoDao;
    private final MutableLiveData<List<String>> foldersLiveData = new MutableLiveData<>(new ArrayList<>());

    public MediaStoreRepository(Context context) {
        this.context = context.getApplicationContext();
        this.videoDao = AppDatabase.getInstance(context).videoDao();
    }

    public LiveData<List<String>> getFolders() {
        return foldersLiveData;
    }

    public LiveData<List<VideoEntity>> getVideosByFolder(String folder) {
        return videoDao.getByFolder(folder);
    }

    public void scanVideos() {
        List<VideoEntity> videos = new ArrayList<>();
        Set<String> folders = new HashSet<>();
        ContentResolver resolver = context.getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_MODIFIED,
                MediaStore.Video.Media.RELATIVE_PATH
        };

        try (Cursor cursor = resolver.query(uri, projection, null, null, null)) {
            if (cursor != null) {
                int idCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                int nameCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                int durationCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
                int sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
                int modifiedCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED);
                int pathCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RELATIVE_PATH);
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idCol);
                    String title = cursor.getString(nameCol);
                    long duration = cursor.getLong(durationCol);
                    long size = cursor.getLong(sizeCol);
                    long modified = cursor.getLong(modifiedCol);
                    String relativePath = cursor.getString(pathCol);
                    String folder = !TextUtils.isEmpty(relativePath) ? relativePath : "Unknown";
                    Uri contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                    VideoEntity entity = new VideoEntity(String.valueOf(id), title, contentUri.toString(), folder, duration, size, modified);
                    videos.add(entity);
                    folders.add(folder);
                }
            }
        }
        videoDao.clear();
        videoDao.upsert(videos);
        foldersLiveData.postValue(new ArrayList<>(folders));
    }
}

