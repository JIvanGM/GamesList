package com.example.gameslist.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gameslist.data.models.Game

@Database(
    entities = [Game::class],
    version = 1
)
abstract class GamesDatabase : RoomDatabase() {
    abstract val dao: GamesDao
}