package com.example.frontend.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// https://developer.android.com/codelabs/basic-android-kotlin-compose-persisting-data-room


@Database(
    entities = [Hyperlink::class],
    version = 1,
    exportSchema = false,
)
abstract class HyperlinkDatabase: RoomDatabase() {
    abstract fun hyperlinkDao(): HyperlinkDao
    companion object {
        @Volatile
        private var Instance: HyperlinkDatabase? = null

        fun getDatabase(context: Context): HyperlinkDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, HyperlinkDatabase::class.java, "hyperlink_database")
                    // Setting this option in your app's database builder means that Room
                    // permanently deletes all data from the tables in your database when it
                    // attempts to perform a migration with no defined migration path.
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
