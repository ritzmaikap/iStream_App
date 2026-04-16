package com.example.istream_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.istream_app.utils.AuthKeeper;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

/*
 * I am creating a Video Player screen to play YouTube videos.
 * I am using a third-party YouTube Player library to load and play videos.
 */
public class VideoPlayerActivity extends AppCompatActivity {

    // I am declaring UI components
    private YouTubePlayerView youtubePlayerView;
    private Button btnBackToHome, btnLogoutFromPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // I am enabling edge-to-edge UI
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video_player);

        // I am adjusting UI padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.videoPlayerRoot), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        // I am linking UI elements
        youtubePlayerView = findViewById(R.id.youtubePlayerView);
        btnBackToHome = findViewById(R.id.btnBackToHome);
        btnLogoutFromPlayer = findViewById(R.id.btnLogoutPlayer);

        // I am attaching the player lifecycle to activity lifecycle
        getLifecycle().addObserver(youtubePlayerView);

        // I am receiving the YouTube URL passed from Dashboard
        String url = getIntent().getStringExtra("youtube_url");

        // I am checking if URL is missing
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(this, "No URL received", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // I am extracting the video ID from the URL
        String videoId = extractYoutubeVideoId(url);

        // I am validating extracted video ID
        if (TextUtils.isEmpty(videoId)) {
            Toast.makeText(this, "Invalid YouTube URL", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // I am adding a listener to control YouTube player behavior
        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {

            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                // I am loading the video using extracted video ID
                youTubePlayer.loadVideo(videoId, 0);
            }

            @Override
            public void onError(@NonNull YouTubePlayer youTubePlayer,
                                @NonNull PlayerConstants.PlayerError error) {

                // I am showing error message if video fails to load
                Toast.makeText(VideoPlayerActivity.this,
                        "Unable to play this video",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // I am going back to previous screen (Dashboard)
        btnBackToHome.setOnClickListener(v -> finish());

        btnLogoutFromPlayer.setOnClickListener(v -> {
            // I am logging out user and clearing session
            AuthKeeper.logout(VideoPlayerActivity.this);

            // I am navigating to login screen and clearing back stack
            Intent intent = new Intent(VideoPlayerActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    /*
     * I am extracting the video ID from different YouTube URL formats.
     */
    private String extractYoutubeVideoId(String url) {
        try {

            // I am handling standard YouTube URL format
            if (url.contains("watch?v=")) {
                String id = url.split("v=")[1];
                int amp = id.indexOf("&");
                return amp != -1 ? id.substring(0, amp) : id;
            }

            // I am handling shortened YouTube URL format
            if (url.contains("youtu.be/")) {
                String id = url.split("youtu.be/")[1];
                int q = id.indexOf("?");
                return q != -1 ? id.substring(0, q) : id;
            }

        } catch (Exception e) {
            // I am handling any unexpected errors during extraction
            e.printStackTrace();
        }

        // I am returning null if extraction fails
        return null;
    }
}