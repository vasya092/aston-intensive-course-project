package com.example.astoncourseproject.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.astoncourseproject.data.models.character.CharacterEntity
import com.example.astoncourseproject.data.models.episode.EpisodeEntity
import com.example.astoncourseproject.data.models.location.LocationEntity

@Database(entities = [CharacterEntity::class, LocationEntity::class, EpisodeEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun localDataBaseDao(): LocalDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getDatabase(context: Context): LocalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "local_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}