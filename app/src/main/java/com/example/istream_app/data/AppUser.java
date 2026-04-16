package com.example.istream_app.data;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/*
 * This entity represents one user in our Room database.
 * Each username must be unique, so I added an index with unique = true.
 */
@Entity(
        tableName = "app_users",
        indices = {@Index(value = {"username"}, unique = true)}
)
public class AppUser {

    @PrimaryKey(autoGenerate = true)
    public int userId;

    public String fullName;
    public String username;
    public String password;

    public AppUser(String fullName, String username, String password) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
    }
}