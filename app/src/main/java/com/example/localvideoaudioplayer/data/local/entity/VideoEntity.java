package com.example.localvideoaudioplayer.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "videos")
public class VideoEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String title;
    public String path;
    public String folder;
    public long durationMs;
    public long sizeBytes;
    public long lastModified;

    public VideoEntity(@NonNull String id, String title, String path, String folder, long durationMs, long sizeBytes, long lastModified) {
        this.id = id;
        this.title = title;
        this.path = path;
        this.folder = folder;
        this.durationMs = durationMs;
        this.sizeBytes = sizeBytes;
        this.lastModified = lastModified;
    }
}

