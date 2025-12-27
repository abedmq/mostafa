package com.example.localvideoaudioplayer.ui.audio;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.localvideoaudioplayer.R;
import com.example.localvideoaudioplayer.data.local.entity.AudioEntity;

import java.util.ArrayList;
import java.util.List;

public class AudioLibraryActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private List<AudioEntity> current = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_library);
        ListView listView = findViewById(R.id.audio_list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);

        AudioLibraryViewModel viewModel = new ViewModelProvider(this, new AudioLibraryViewModel.Factory(getApplication()))
                .get(AudioLibraryViewModel.class);
        viewModel.getAudioFiles().observe(this, audios -> {
            current = audios;
            adapter.clear();
            for (AudioEntity entity : audios) {
                adapter.add(entity.title);
            }
        });
    }
}

