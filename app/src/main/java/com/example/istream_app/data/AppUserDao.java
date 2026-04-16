package com.example.istream_app.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

/*
 * DAO = Data Access Object
 * This interface contains the database operations related to users.
 */
@Dao
public interface AppUserDao {

    @Insert
    long insertUser(AppUser user);

    @Query("SELECT * FROM app_users WHERE username = :username AND password = :password LIMIT 1")
    AppUser loginUser(String username, String password);

    @Query("SELECT * FROM app_users WHERE username = :username LIMIT 1")
    AppUser getUserByUsername(String username);

    @Query("SELECT * FROM app_users WHERE userId = :userId LIMIT 1")
    AppUser getUserById(int userId);
}