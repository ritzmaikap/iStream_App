package com.example.istream_app.data;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/*
 * I am creating an Entity class for Room database.
 * This class represents a single user record in my database.
 * I have also ensured that the username is unique by adding an index.
 */
@Entity(
        tableName = "app_users", // I am naming my table as "app_users"
        indices = {@Index(value = {"username"}, unique = true)} // I am making username unique so no duplicate users can exist
)
public class AppUser {

    @PrimaryKey(autoGenerate = true)
    public int userId; // I am using this as a unique ID for each user and letting Room auto-generate it

    public String fullName; // I am storing the full name of the user
    public String username; // I am storing the username (must be unique)
    public String password; // I am storing the password for login purposes

    // I am creating a constructor to initialize user details when a new user is created
    public AppUser(String fullName, String username, String password) {
        this.fullName = fullName; // assigning full name
        this.username = username; // assigning username
        this.password = password; // assigning password
    }
}