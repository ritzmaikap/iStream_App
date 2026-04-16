package com.example.istream_app.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/*
 * I am creating an Entity class to store saved videos.
 * Each video belongs to a specific user, so I am linking it using a foreign key.
 * This helps me keep playlists separate for each user.
 */
@Entity(
        tableName = "saved_videos", // I am naming my table as "saved_videos"
        foreignKeys = @ForeignKey(
                entity = AppUser.class, // I am linking this table to the AppUser table
                parentColumns = "userId", // this is the primary key in AppUser
                childColumns = "ownerUserId", // this is the column in this table that refers to AppUser
                onDelete = ForeignKey.CASCADE // if a user is deleted, I automatically delete all their saved videos
        )
)
public class SavedVideo {

    @PrimaryKey(autoGenerate = true)
    public int videoId; // I am using this as a unique ID for each saved video

    public int ownerUserId; // I am storing which user this video belongs to
    public String videoUrl; // I am storing the video URL

    // I am creating a constructor to initialize video details when saving a new video
    public SavedVideo(int ownerUserId, String videoUrl) {
        this.ownerUserId = ownerUserId; // assigning user ID
        this.videoUrl = videoUrl; // assigning video URL
    }
}