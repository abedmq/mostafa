package com.example.localvideoaudioplayer.ui.videolist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.localvideoaudioplayer.R;
import com.example.localvideoaudioplayer.data.local.entity.VideoEntity;
import com.example.localvideoaudioplayer.ui.player.PlayerActivity;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity {

    public static final String EXTRA_FOLDER = "extra_folder";
    private ArrayAdapter<String> adapter;
    private List<VideoEntity> currentVideos = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        String folder = getIntent().getStringExtra(EXTRA_FOLDER);
        setTitle(folder);

        ListView listView = findViewById(R.id.video_list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            VideoEntity video = currentVideos.get(position);
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.setData(Uri.parse(video.path));
            intent.putExtra(PlayerActivity.EXTRA_TITLE, video.title);
            startActivity(intent);
        });

        VideoListViewModel viewModel = new ViewModelProvider(this, new VideoListViewModel.Factory(getApplication()))
                .get(VideoListViewModel.class);
        viewModel.getVideos(folder).observe(this, this::renderVideos);
    }

    private void renderVideos(List<VideoEntity> videos) {
        currentVideos = videos;
        adapter.clear();
        for (VideoEntity entity : videos) {
            adapter.add(entity.title);
        }
    }
}

