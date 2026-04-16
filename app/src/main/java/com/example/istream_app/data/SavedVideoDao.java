package com.example.istream_app.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/*
 * I am creating a DAO for saved video operations.
 * This interface contains all the database queries related to videos.
 */
@Dao
public interface SavedVideoDao {

    @Insert
    long insertVideo(SavedVideo savedVideo);
    // I am inserting a new video into the database
    // This returns the row ID of the inserted video

    @Query("SELECT * FROM saved_videos WHERE ownerUserId = :userId ORDER BY videoId DESC")
    List<SavedVideo> getVideosByUserId(int userId);
    // I am fetching all videos for a specific user
    // I am sorting them in descending order so the latest video appears first

    @Query("SELECT * FROM saved_videos WHERE ownerUserId = :userId AND videoUrl = :url LIMIT 1")
    SavedVideo findDuplicateVideo(int userId, String url);
    // I am checking if the same video URL already exists for this user
    // This helps me avoid saving duplicate videos in the playlist
}