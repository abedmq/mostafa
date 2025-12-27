package com.example.localvideoaudioplayer.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.localvideoaudioplayer.data.local.entity.VideoEntity;

import java.util.List;

@Dao
public interface VideoDao {
    @Query("SELECT * FROM videos ORDER BY folder, title")
    LiveData<List<VideoEntity>> getAll();

    @Query("SELECT * FROM videos WHERE folder = :folder ORDER BY title")
    LiveData<List<VideoEntity>> getByFolder(String folder);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(List<VideoEntity> videos);

    @Query("DELETE FROM videos")
    void clear();
}

