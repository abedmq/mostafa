package com.example.localvideoaudioplayer.player;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.MediaSession;

import com.example.localvideoaudioplayer.service.PlaybackService;

public class PlayerManager {
    private final Context context;
    private ExoPlayer exoPlayer;
    private MediaSession mediaSession;
    private boolean audioOnly;

    public PlayerManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public void initialize() {
        if (exoPlayer != null) return;
        exoPlayer = new ExoPlayer.Builder(context)
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setContentType(androidx.media3.common.C.AUDIO_CONTENT_TYPE_MOVIE)
                        .setUsage(androidx.media3.common.C.USAGE_MEDIA)
                        .build(), true)
                .setHandleAudioBecomingNoisy(true)
                .build();
        mediaSession = new MediaSession.Builder(context, exoPlayer)
                .setSessionActivity(PlaybackService.createContentIntent(context))
                .build();
    }

    public MediaSession getMediaSession() {
        return mediaSession;
    }

    public Player getPlayer() {
        return exoPlayer;
    }

    public void play(Uri uri, @Nullable Bundle extras) {
        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(uri)
                .setMediaMetadata(new androidx.media3.common.MediaMetadata.Builder()
                        .setExtras(extras)
                        .build())
                .build();
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
    }

    public void setPlaybackSpeed(float speed) {
        if (exoPlayer != null) {
            exoPlayer.setPlaybackSpeed(speed);
        }
    }

    public void setAudioOnly(boolean enabled) {
        this.audioOnly = enabled;
    }

    public boolean isAudioOnly() {
        return audioOnly;
    }

    public void release() {
        if (mediaSession != null) {
            mediaSession.release();
            mediaSession = null;
        }
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}

