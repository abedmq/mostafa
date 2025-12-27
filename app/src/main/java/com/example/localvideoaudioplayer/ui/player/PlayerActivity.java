package com.example.localvideoaudioplayer.ui.player;

import android.app.PictureInPictureParams;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Rational;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.Player;
import androidx.media3.ui.PlayerView;

import com.example.localvideoaudioplayer.R;
import com.example.localvideoaudioplayer.service.PlaybackService;

public class PlayerActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "extra_title";
    private PlayerView playerView;
    private PlayerViewModel viewModel;
    private final Player.Listener listener = new Player.Listener() {};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        playerView = findViewById(R.id.player_view);
        TextView titleView = findViewById(R.id.video_title);
        ImageButton pipButton = findViewById(R.id.btn_pip);
        ImageButton audioOnlyButton = findViewById(R.id.btn_audio_only);

        viewModel = new ViewModelProvider(this, new PlayerViewModel.Factory(getApplication())).get(PlayerViewModel.class);
        Uri uri = getIntent().getData();
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        titleView.setText(title);
        viewModel.prepare(uri);

        viewModel.getPlayer().addListener(listener);
        playerView.setPlayer(viewModel.getPlayer());

        pipButton.setOnClickListener(v -> enterPipMode());
        audioOnlyButton.setOnClickListener(v -> {
            viewModel.toggleAudioOnly();
            v.setSelected(viewModel.isAudioOnly());
        });
    }

    private void enterPipMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PictureInPictureParams params = new PictureInPictureParams.Builder()
                    .setAspectRatio(new Rational(16, 9))
                    .build();
            enterPictureInPictureMode(params);
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        enterPipMode();
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        View controls = findViewById(R.id.controls_container);
        controls.setVisibility(isInPictureInPictureMode ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.pauseIfNeeded();
    }

    @Override
    protected void onDestroy() {
        viewModel.getPlayer().removeListener(listener);
        super.onDestroy();
    }
}

