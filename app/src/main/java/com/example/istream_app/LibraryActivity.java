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
 * I am creating a screen to display the playlist of the logged-in user.
 * I am showing all saved video URLs and allowing selection to send back to dashboard.
 */
public class LibraryActivity extends AppCompatActivity {

    // I am declaring UI components
    private ListView listViewPlaylist;
    private Button btnBackToDashboard;
    private Button btnLogoutLibrary;

    // I am declaring database and data list
    private LocalVaultDatabase database;
    private List<SavedVideo> savedVideoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // I am checking if user is logged in, otherwise redirecting to login screen
        if (!AuthKeeper.isLoggedIn(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_library);

        // I am adjusting UI padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.libraryRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // I am initializing the database
        database = LocalVaultDatabase.getInstance(this);

        // I am linking UI elements
        listViewPlaylist = findViewById(R.id.listViewPlaylist);
        btnBackToDashboard = findViewById(R.id.btnBackToDashboard);
        btnLogoutLibrary = findViewById(R.id.btnLogoutLibrary);

        // I am loading the playlist data for the current user
        loadPlaylist();

        // I am going back to dashboard when button is clicked
        btnBackToDashboard.setOnClickListener(v -> finish());

        btnLogoutLibrary.setOnClickListener(v -> {
            // I am logging out and clearing session
            AuthKeeper.logout(LibraryActivity.this);

            // I am navigating to login screen and clearing back stack
            Intent intent = new Intent(LibraryActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    /*
     * I am loading only the logged-in user's saved videos from database.
     */
    private void loadPlaylist() {

        // I am getting the current logged-in user's ID
        int currentUserId = AuthKeeper.getLoggedInUserId(this);

        // I am fetching videos specific to this user
        savedVideoList = database.savedVideoDao().getVideosByUserId(currentUserId);

        // I am creating a list of URLs to display in ListView
        List<String> urlList = new ArrayList<>();
        for (SavedVideo video : savedVideoList) {
            urlList.add(video.videoUrl);
        }

        // I am showing a message if no videos are saved
        if (urlList.isEmpty()) {
            Toast.makeText(this, "No saved videos in my playlist", Toast.LENGTH_SHORT).show();
        }

        // I am creating an adapter to bind data to ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                urlList
        );

        // I am setting the adapter to ListView
        listViewPlaylist.setAdapter(adapter);

        // I am handling item click to send selected URL back to dashboard
        listViewPlaylist.setOnItemClickListener((parent, view, position, id) -> {

            // I am getting the selected video URL
            String selectedUrl = urlList.get(position);

            // I am sending this URL back to DashboardActivity
            Intent intent = new Intent(LibraryActivity.this, DashboardActivity.class);
            intent.putExtra("selected_video_url", selectedUrl);
            startActivity(intent);
        });
    }
}