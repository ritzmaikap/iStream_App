package com.example.istream_app.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/*
 * This entity stores each saved video URL.
 * Each video belongs to one user through ownerUserId.
 * That is how we keep playlists separate for each user.
 */
@Entity(
        tableName = "saved_videos",
        foreignKeys = @ForeignKey(
                entity = AppUser.class,
                parentColumns = "userId",
                childColumns = "ownerUserId",
                onDelete = ForeignKey.CASCADE
        )
)
public class SavedVideo {

    @PrimaryKey(autoGenerate = true)
    public int videoId;

    public int ownerUserId;
    public String videoUrl;

    public SavedVideo(int ownerUserId, String videoUrl) {
        this.ownerUserId = ownerUserId;
        this.videoUrl = videoUrl;
    }
}