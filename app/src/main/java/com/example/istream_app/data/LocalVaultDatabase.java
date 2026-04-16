package com.example.istream_app.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/*
 * This is the main Room database class.
 * It connects our entities and DAOs together.
 */
@Database(entities = {AppUser.class, SavedVideo.class}, version = 1, exportSchema = false)
public abstract class LocalVaultDatabase extends RoomDatabase {

    public abstract AppUserDao appUserDao();
    public abstract SavedVideoDao savedVideoDao();

    private static volatile LocalVaultDatabase INSTANCE;

    public static LocalVaultDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LocalVaultDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    LocalVaultDatabase.class,
                                    "istream_local_vault_db"
                            )

                            // In production, database work should be done in background threads but for now I am using this.
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}