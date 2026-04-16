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

public class VideoPlayerActivity extends AppCompatActivity {

    private YouTubePlayerView youtubePlayerView;
    private Button btnBackToHome, btnLogoutFromPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video_player);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.videoPlayerRoot), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        youtubePlayerView = findViewById(R.id.youtubePlayerView);
        btnBackToHome = findViewById(R.id.btnBackToHome);
        btnLogoutFromPlayer = findViewById(R.id.btnLogoutPlayer);

        getLifecycle().addObserver(youtubePlayerView);

        String url = getIntent().getStringExtra("youtube_url");

        if (TextUtils.isEmpty(url)) {
            Toast.makeText(this, "No URL received", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String videoId = extractYoutubeVideoId(url);

        if (TextUtils.isEmpty(videoId)) {
            Toast.makeText(this, "Invalid YouTube URL", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {

            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0);
            }

            @Override
            public void onError(@NonNull YouTubePlayer youTubePlayer,
                                @NonNull PlayerConstants.PlayerError error) {

                Toast.makeText(VideoPlayerActivity.this,
                        "Unable to play this video",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnBackToHome.setOnClickListener(v -> finish());

        btnLogoutFromPlayer.setOnClickListener(v -> {
            AuthKeeper.logout(VideoPlayerActivity.this);

            Intent intent = new Intent(VideoPlayerActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private String extractYoutubeVideoId(String url) {
        try {
            if (url.contains("watch?v=")) {
                String id = url.split("v=")[1];
                int amp = id.indexOf("&");
                return amp != -1 ? id.substring(0, amp) : id;
            }

            if (url.contains("youtu.be/")) {
                String id = url.split("youtu.be/")[1];
                int q = id.indexOf("?");
                return q != -1 ? id.substring(0, q) : id;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}