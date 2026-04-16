package com.example.istream_app.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/*
 * DAO for saved video operations.
 */
@Dao
public interface SavedVideoDao {

    @Insert
    long insertVideo(SavedVideo savedVideo);

    @Query("SELECT * FROM saved_videos WHERE ownerUserId = :userId ORDER BY videoId DESC")
    List<SavedVideo> getVideosByUserId(int userId);

    @Query("SELECT * FROM saved_videos WHERE ownerUserId = :userId AND videoUrl = :url LIMIT 1")
    SavedVideo findDuplicateVideo(int userId, String url);
}