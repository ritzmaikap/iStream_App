package com.example.istream_app.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

/*
 * I am creating a DAO (Data Access Object) interface.
 * This is where I define all the database operations related to users.
 */
@Dao
public interface AppUserDao {

    @Insert
    long insertUser(AppUser user);
    // I am inserting a new user into the database
    // This returns the row ID of the inserted user

    @Query("SELECT * FROM app_users WHERE username = :username AND password = :password LIMIT 1")
    AppUser loginUser(String username, String password);
    // I am checking login by matching username and password
    // If a match is found, I return the user object, otherwise null

    @Query("SELECT * FROM app_users WHERE username = :username LIMIT 1")
    AppUser getUserByUsername(String username);
    // I am fetching a user based on the username
    // This helps in checking if a username already exists during sign up

    @Query("SELECT * FROM app_users WHERE userId = :userId LIMIT 1")
    AppUser getUserById(int userId);
    // I am fetching a user using the unique userId
}