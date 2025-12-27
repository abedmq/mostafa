package com.example.localvideoaudioplayer.converter;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.arthenica.ffmpegkit.FFmpegKit;
import com.arthenica.ffmpegkit.ReturnCode;
import com.example.localvideoaudioplayer.data.local.AppDatabase;
import com.example.localvideoaudioplayer.data.local.entity.AudioEntity;

import java.io.File;
import java.util.UUID;

public class AudioConverter {

    public interface Listener {
        void onProgress(String message);

        void onCompleted(File outputFile);

        void onFailed(String message);
    }

    private final Context context;

    public AudioConverter(Context context) {
        this.context = context.getApplicationContext();
    }

    public void convertToMp3(Uri inputUri, Listener listener) {
        new Thread(() -> {
            try {
                File outputDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "LocalVideoAudioPlayer/Converted");
                if (!outputDir.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    outputDir.mkdirs();
                }
                String outputName = "audio_" + System.currentTimeMillis() + ".mp3";
                File outputFile = new File(outputDir, outputName);
                String cmd = "-i \"" + inputUri + "\" -vn -ar 44100 -ac 2 -b:a 192k \"" + outputFile.getAbsolutePath() + "\"";
                FFmpegKit.executeAsync(cmd, session -> {
                    ReturnCode code = session.getReturnCode();
                    if (ReturnCode.isSuccess(code)) {
                        AppDatabase.getInstance(context).audioDao().insert(
                                new AudioEntity(UUID.randomUUID().toString(), outputName, outputFile.getAbsolutePath(), 0, System.currentTimeMillis()));
                        listener.onCompleted(outputFile);
                    } else {
                        listener.onFailed("Conversion failed: " + code);
                    }
                });
            } catch (Exception e) {
                listener.onFailed(e.getMessage());
            }
        }).start();
    }
}

