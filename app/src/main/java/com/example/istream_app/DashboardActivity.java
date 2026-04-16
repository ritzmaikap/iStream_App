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
 * This is the Home Screen / Dashboard screen.
 * User can:
 * 1. Enter a YouTube URL
 * 2. Play it
 * 3. Save it to playlist
 * 4. Open playlist
 * 5. Logout
 */
public class DashboardActivity extends AppCompatActivity {

    private EditText editYoutubeUrl;
    private Button btnPlay;
    private Button btnAddToPlaylist;
    private Button btnMyPlaylist;
    private Button btnLogout;

    private LocalVaultDatabase database;
    private SavedVideoDao savedVideoDao;
    private int loggedInUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If user is not logged in, do not allow access to dashboard
        if (!AuthKeeper.isLoggedIn(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dashboardRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        database = LocalVaultDatabase.getInstance(this);
        savedVideoDao = database.savedVideoDao();
        loggedInUserId = AuthKeeper.getLoggedInUserId(this);

        editYoutubeUrl = findViewById(R.id.editYoutubeUrl);
        btnPlay = findViewById(R.id.btnPlayVideo);
        btnAddToPlaylist = findViewById(R.id.btnAddToPlaylist);
        btnMyPlaylist = findViewById(R.id.btnOpenLibrary);
        btnLogout = findViewById(R.id.btnLogoutDashboard);

        /*
         * If a URL comes from playlist screen, prefill it here.
         */
        String incomingUrl = getIntent().getStringExtra("selected_video_url");
        if (incomingUrl != null) {
            editYoutubeUrl.setText(incomingUrl);
        }

        btnPlay.setOnClickListener(v -> playVideo());

        btnAddToPlaylist.setOnClickListener(v -> saveVideoToPlaylist());

        btnMyPlaylist.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, LibraryActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            AuthKeeper.logout(DashboardActivity.this);
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    /*
     * Opens the player screen after validating the entered URL.
     */
    private void playVideo() {
        String url = editYoutubeUrl.getText().toString().trim();

        if (!isValidYoutubeUrl(url)) {
            Toast.makeText(this, "Please enter a valid YouTube URL", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(DashboardActivity.this, VideoPlayerActivity.class);
        intent.putExtra("youtube_url", url);
        startActivity(intent);
    }

    /*
     * Saves the entered video URL for the logged-in user only.
     * This ensures each user's playlist stays separate.
     */
    private void saveVideoToPlaylist() {
        String url = editYoutubeUrl.getText().toString().trim();

        if (!isValidYoutubeUrl(url)) {
            Toast.makeText(this, "Enter a valid YouTube URL before saving", Toast.LENGTH_SHORT).show();
            return;
        }

        SavedVideo existingVideo = savedVideoDao.findDuplicateVideo(loggedInUserId, url);
        if (existingVideo != null) {
            Toast.makeText(this, "Video already saved in your playlist", Toast.LENGTH_SHORT).show();
            return;
        }

        SavedVideo savedVideo = new SavedVideo(loggedInUserId, url);
        savedVideoDao.insertVideo(savedVideo);

        Toast.makeText(this, "Video added to playlist", Toast.LENGTH_SHORT).show();
    }

    /*
     * Basic validation for URL format + YouTube domain check.
     */
    private boolean isValidYoutubeUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }

        if (!URLUtil.isValidUrl(url)) {
            return false;
        }

        return url.contains("youtube.com/watch?v=") || url.contains("youtu.be/");
    }
}