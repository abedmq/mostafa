package com.example.localvideoaudioplayer.ui.folder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.localvideoaudioplayer.R;
import com.example.localvideoaudioplayer.ui.videolist.VideoListActivity;

import java.util.ArrayList;

public class FolderListActivity extends AppCompatActivity {

    private FolderListViewModel viewModel;
    private ArrayAdapter<String> adapter;

    private final ActivityResultLauncher<String> permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            granted -> {
                if (granted) {
                    viewModel.scan();
                } else {
                    Toast.makeText(this, R.string.permission_rationale, Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_list);
        ListView listView = findViewById(R.id.folder_list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String folder = adapter.getItem(position);
            Intent intent = new Intent(this, VideoListActivity.class);
            intent.putExtra(VideoListActivity.EXTRA_FOLDER, folder);
            startActivity(intent);
        });

        viewModel = new ViewModelProvider(this, new FolderListViewModel.Factory(getApplication())).get(FolderListViewModel.class);
        viewModel.getFolders().observe(this, folders -> {
            adapter.clear();
            if (folders != null) {
                adapter.addAll(folders);
            }
        });
        requestPermissionAndScan();
    }

    private void requestPermissionAndScan() {
        String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                Manifest.permission.READ_MEDIA_VIDEO :
                Manifest.permission.READ_EXTERNAL_STORAGE;
        if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            viewModel.scan();
        } else {
            permissionLauncher.launch(permission);
        }
    }
}

