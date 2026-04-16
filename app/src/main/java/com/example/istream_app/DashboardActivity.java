package com.example.istream_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.istream_app.data.LocalVaultDatabase;
import com.example.istream_app.data.SavedVideo;
import com.example.istream_app.data.SavedVideoDao;
import com.example.istream_app.utils.AuthKeeper;

/*
 * I am creating the Dashboard screen.
 * From here, I can play videos, save them, view playlist, and logout.
 */
public class DashboardActivity extends AppCompatActivity {

    // I am declaring UI components
    private EditText editYoutubeUrl;
    private Button btnPlay;
    private Button btnAddToPlaylist;
    private Button btnMyPlaylist;
    private Button btnLogout;

    // I am declaring database-related variables
    private LocalVaultDatabase database;
    private SavedVideoDao savedVideoDao;
    private int loggedInUserId;

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
        setContentView(R.layout.activity_dashboard);

        // I am adjusting UI padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dashboardRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // I am initializing database and DAO
        database = LocalVaultDatabase.getInstance(this);
        savedVideoDao = database.savedVideoDao();

        // I am getting the logged-in user ID from SharedPreferences
        loggedInUserId = AuthKeeper.getLoggedInUserId(this);

        // I am linking UI elements
        editYoutubeUrl = findViewById(R.id.editYoutubeUrl);
        btnPlay = findViewById(R.id.btnPlayVideo);
        btnAddToPlaylist = findViewById(R.id.btnAddToPlaylist);
        btnMyPlaylist = findViewById(R.id.btnOpenLibrary);
        btnLogout = findViewById(R.id.btnLogoutDashboard);

        /*
         * I am checking if a video URL is passed from the playlist screen
         * If yes, I am pre-filling it in the input field
         */
        String incomingUrl = getIntent().getStringExtra("selected_video_url");
        if (incomingUrl != null) {
            editYoutubeUrl.setText(incomingUrl);
        }

        // I am setting button click actions
        btnPlay.setOnClickListener(v -> playVideo());

        btnAddToPlaylist.setOnClickListener(v -> saveVideoToPlaylist());

        btnMyPlaylist.setOnClickListener(v -> {
            // I am opening the playlist (Library) screen
            Intent intent = new Intent(DashboardActivity.this, LibraryActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            // I am logging the user out and clearing session
            AuthKeeper.logout(DashboardActivity.this);

            // I am navigating back to login screen and clearing back stack
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    /*
     * I am validating the URL and opening the video player screen.
     */
    private void playVideo() {
        String url = editYoutubeUrl.getText().toString().trim();

        // I am checking if the entered URL is valid
        if (!isValidYoutubeUrl(url)) {
            Toast.makeText(this, "Please enter a valid YouTube URL", Toast.LENGTH_SHORT).show();
            return;
        }

        // I am passing the URL to VideoPlayerActivity
        Intent intent = new Intent(DashboardActivity.this, VideoPlayerActivity.class);
        intent.putExtra("youtube_url", url);
        startActivity(intent);
    }

    /*
     * I am saving the video URL for the logged-in user.
     * This ensures each user has their own playlist.
     */
    private void saveVideoToPlaylist() {
        String url = editYoutubeUrl.getText().toString().trim();

        // I am validating the URL before saving
        if (!isValidYoutubeUrl(url)) {
            Toast.makeText(this, "Enter a valid YouTube URL before saving", Toast.LENGTH_SHORT).show();
            return;
        }

        // I am checking for duplicate video for this user
        SavedVideo existingVideo = savedVideoDao.findDuplicateVideo(loggedInUserId, url);
        if (existingVideo != null) {
            Toast.makeText(this, "Video already saved in my playlist", Toast.LENGTH_SHORT).show();
            return;
        }

        // I am creating a new SavedVideo object
        SavedVideo savedVideo = new SavedVideo(loggedInUserId, url);

        // I am inserting it into the database
        savedVideoDao.insertVideo(savedVideo);

        Toast.makeText(this, "Video added to playlist", Toast.LENGTH_SHORT).show();
    }

    /*
     * I am validating if the URL is a proper YouTube link.
     */
    private boolean isValidYoutubeUrl(String url) {

        // I am checking if the URL is empty
        if (TextUtils.isEmpty(url)) {
            return false;
        }

        // I am checking if the URL format is valid
        if (!URLUtil.isValidUrl(url)) {
            return false;
        }

        // I am checking if it is a YouTube link
        return url.contains("youtube.com/watch?v=") || url.contains("youtu.be/");
    }
}