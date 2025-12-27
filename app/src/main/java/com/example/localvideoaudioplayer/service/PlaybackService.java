package com.example.localvideoaudioplayer.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.media3.common.Player;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaSessionService;

import com.example.localvideoaudioplayer.player.PlayerManager;
import com.example.localvideoaudioplayer.ui.player.PlayerActivity;

public class PlaybackService extends MediaSessionService {

    private PlayerManager playerManager;
    private MediaSession mediaSession;

    @Override
    public void onCreate() {
        super.onCreate();
        playerManager = new PlayerManager(this);
        playerManager.initialize();
        mediaSession = playerManager.getMediaSession();
    }

    @Nullable
    @Override
    public MediaSession onGetSession(MediaSession.ControllerInfo controllerInfo) {
        return mediaSession;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onDestroy() {
        if (mediaSession != null) {
            mediaSession.getPlayer().release();
            mediaSession.release();
        }
        super.onDestroy();
    }

    public static PendingIntent createContentIntent(Context context) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(context, 1001, intent, PendingIntent.FLAG_IMMUTABLE);
    }
}
