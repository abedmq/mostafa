package com.example.localvideoaudioplayer.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "download_tasks")
public class DownloadTaskEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String url;
    public String fileName;
    public String status;
    public int progress;
    public long createdAt;

    public DownloadTaskEntity(@NonNull String id, String url, String fileName, String status, int progress, long createdAt) {
        this.id = id;
        this.url = url;
        this.fileName = fileName;
        this.status = status;
        this.progress = progress;
        this.createdAt = createdAt;
    }
}

