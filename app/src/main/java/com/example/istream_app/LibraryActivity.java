package com.example.istream_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.istream_app.data.LocalVaultDatabase;
import com.example.istream_app.data.SavedVideo;
import com.example.istream_app.utils.AuthKeeper;

import java.util.ArrayList;
import java.util.List;

/*
 * This screen shows the playlist of the currently logged-in user.
 * When the user taps a URL, it is sent back to DashboardActivity.
 */
public class LibraryActivity extends AppCompatActivity {

    private ListView listViewPlaylist;
    private Button btnBackToDashboard;
    private Button btnLogoutLibrary;

    private LocalVaultDatabase database;
    private List<SavedVideo> savedVideoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!AuthKeeper.isLoggedIn(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_library);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.libraryRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        database = LocalVaultDatabase.getInstance(this);

        listViewPlaylist = findViewById(R.id.listViewPlaylist);
        btnBackToDashboard = findViewById(R.id.btnBackToDashboard);
        btnLogoutLibrary = findViewById(R.id.btnLogoutLibrary);

        loadPlaylist();

        btnBackToDashboard.setOnClickListener(v -> finish());

        btnLogoutLibrary.setOnClickListener(v -> {
            AuthKeeper.logout(LibraryActivity.this);
            Intent intent = new Intent(LibraryActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    /*
     * Loads only the current user's saved videos.
     */
    private void loadPlaylist() {
        int currentUserId = AuthKeeper.getLoggedInUserId(this);
        savedVideoList = database.savedVideoDao().getVideosByUserId(currentUserId);

        List<String> urlList = new ArrayList<>();
        for (SavedVideo video : savedVideoList) {
            urlList.add(video.videoUrl);
        }

        if (urlList.isEmpty()) {
            Toast.makeText(this, "No saved videos in your playlist", Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                urlList
        );

        listViewPlaylist.setAdapter(adapter);

        listViewPlaylist.setOnItemClickListener((parent, view, position, id) -> {
            String selectedUrl = urlList.get(position);

            Intent intent = new Intent(LibraryActivity.this, DashboardActivity.class);
            intent.putExtra("selected_video_url", selectedUrl);
            startActivity(intent);
        });
    }
}