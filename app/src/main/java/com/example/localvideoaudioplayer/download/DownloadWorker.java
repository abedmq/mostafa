package com.example.localvideoaudioplayer.download;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.localvideoaudioplayer.R;
import com.example.localvideoaudioplayer.data.local.AppDatabase;
import com.example.localvideoaudioplayer.data.local.entity.DownloadTaskEntity;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class DownloadWorker extends Worker {

    public static final String KEY_URL = "key_url";
    public static final String KEY_FILE_NAME = "key_file_name";

    public DownloadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String url = getInputData().getString(KEY_URL);
        String fileName = getInputData().getString(KEY_FILE_NAME);
        if (url == null || fileName == null) return Result.failure();
        String taskId = UUID.randomUUID().toString();
        AppDatabase.getInstance(getApplicationContext()).downloadTaskDao()
                .upsert(new DownloadTaskEntity(taskId, url, fileName, "running", 0, System.currentTimeMillis()));
        try {
            setForegroundAsync(createForegroundInfo(fileName));
            URL connectionUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) connectionUrl.openConnection();
            connection.connect();
            int length = connection.getContentLength();
            InputStream input = new BufferedInputStream(connection.getInputStream());
            java.io.File outputDir = getApplicationContext().getExternalFilesDir(null);
            java.io.File outputFile = new java.io.File(outputDir, fileName);
            FileOutputStream output = new FileOutputStream(outputFile);
            byte[] data = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                if (length > 0) {
                    int progress = (int) ((total * 100) / length);
                    AppDatabase.getInstance(getApplicationContext()).downloadTaskDao().updateProgress(taskId, progress, "running");
                }
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
            AppDatabase.getInstance(getApplicationContext()).downloadTaskDao().updateProgress(taskId, 100, "completed");
            Data outputData = new Data.Builder()
                    .putString("path", outputFile.getAbsolutePath())
                    .build();
            return Result.success(outputData);
        } catch (Exception e) {
            AppDatabase.getInstance(getApplicationContext()).downloadTaskDao().updateProgress(taskId, 0, "failed");
            return Result.failure();
        }
    }

    private ForegroundInfo createForegroundInfo(String fileName) {
        android.app.Notification notification = new androidx.core.app.NotificationCompat.Builder(getApplicationContext(), "downloads")
                .setContentTitle(getApplicationContext().getString(R.string.action_download))
                .setContentText(fileName)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true)
                .build();
        return new ForegroundInfo(101, notification);
    }
}
