package com.example.localvideoaudioplayer.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.localvideoaudioplayer.data.local.entity.DownloadTaskEntity;

import java.util.List;

@Dao
public interface DownloadTaskDao {
    @Query("SELECT * FROM download_tasks ORDER BY createdAt DESC")
    LiveData<List<DownloadTaskEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(DownloadTaskEntity task);

    @Query("UPDATE download_tasks SET progress = :progress, status = :status WHERE id = :id")
    void updateProgress(String id, int progress, String status);
}

