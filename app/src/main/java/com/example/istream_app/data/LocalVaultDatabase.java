package com.example.istream_app.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/*
 * I am creating the main Room database class.
 * This class connects my entities (tables) and DAOs (database operations).
 */
@Database(entities = {AppUser.class, SavedVideo.class}, version = 1, exportSchema = false)
public abstract class LocalVaultDatabase extends RoomDatabase {

    // I am defining abstract methods to access my DAO interfaces
    public abstract AppUserDao appUserDao();
    public abstract SavedVideoDao savedVideoDao();

    // I am creating a singleton instance so only one database object exists in the app
    private static volatile LocalVaultDatabase INSTANCE;

    public static LocalVaultDatabase getInstance(Context context) {

        // I am checking if the instance is null (database not created yet)
        if (INSTANCE == null) {

            // I am using synchronized block to make it thread-safe
            synchronized (LocalVaultDatabase.class) {

                // Double-checking if instance is still null before creating
                if (INSTANCE == null) {

                    // I am building the database using Room
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(), // using application context to avoid memory leaks
                                    LocalVaultDatabase.class, // database class reference
                                    "istream_local_vault_db" // database name
                            )

                            // I am allowing database operations on main thread (only for simplicity, not recommended in real apps)
                            .allowMainThreadQueries()

                            // I am allowing Room to delete and recreate database if schema changes
                            .fallbackToDestructiveMigration()

                            .build(); // finally building the database
                }
            }
        }

        // I am returning the single database instance
        return INSTANCE;
    }
}