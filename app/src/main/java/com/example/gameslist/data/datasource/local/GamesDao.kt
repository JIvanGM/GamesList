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

    @Query("SELECT * From game Where id = :id")
    suspend fun getGameById(id: Int): Game

    @Query("DELETE FROM game WHERE id = :id")
    suspend fun deleteGameById(id: Int)

    @Query("SELECT * FROM game WHERE title LIKE '%' || :query || '%' OR genre LIKE '%' || :query || '%'")
    suspend fun searchGame(query: String): List<Game>
}