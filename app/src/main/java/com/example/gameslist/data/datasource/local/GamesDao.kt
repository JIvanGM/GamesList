package com.example.gameslist.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gameslist.data.models.Game

@Dao
interface GamesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(game: List<Game>)

    @Query("SELECT * FROM game")
    suspend fun getAllGames(): List<Game>

    @Query("Select * From game Where id = :id")
    suspend fun getGameById(id: Int): Game

    @Query("DELETE FROM game WHERE id = :id")
    suspend fun deleteGameById(id: Int)
}