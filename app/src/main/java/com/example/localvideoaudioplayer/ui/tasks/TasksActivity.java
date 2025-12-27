package com.example.localvideoaudioplayer.ui.tasks;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.localvideoaudioplayer.R;
import com.example.localvideoaudioplayer.data.local.entity.DownloadTaskEntity;

import java.util.ArrayList;
import java.util.List;

public class TasksActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private List<DownloadTaskEntity> current = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        ListView listView = findViewById(R.id.tasks_list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);
        TasksViewModel viewModel = new ViewModelProvider(this, new TasksViewModel.Factory(getApplication())).get(TasksViewModel.class);
        viewModel.getTasks().observe(this, tasks -> {
            current = tasks;
            adapter.clear();
            for (DownloadTaskEntity entity : tasks) {
                adapter.add(entity.fileName + " (" + entity.status + " " + entity.progress + "%)");
            }
        });
    }
}

