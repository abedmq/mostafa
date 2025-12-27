package com.example.localvideoaudioplayer.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "audio_files")
public class AudioEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String title;
    public String path;
    public long durationMs;
    public long createdAt;

    public AudioEntity(@NonNull String id, String title, String path, long durationMs, long createdAt) {
        this.id = id;
        this.title = title;
        this.path = path;
        this.durationMs = durationMs;
        this.createdAt = createdAt;
    }
}

