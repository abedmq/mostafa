package com.example.localvideoaudioplayer.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.localvideoaudioplayer.data.local.entity.AudioEntity;

import java.util.List;

@Dao
public interface AudioDao {
    @Query("SELECT * FROM audio_files ORDER BY createdAt DESC")
    LiveData<List<AudioEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AudioEntity audioEntity);

    @Delete
    void delete(AudioEntity audioEntity);
}

