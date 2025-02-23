package com.example.gameslist.repository.local

import com.example.gameslist.data.models.Game

interface LocalGameRepositoryInterface {
    suspend fun insertAllGames(games: List<Game>)
    suspend fun getAllGames(): List<Game?>
    suspend fun getGameById(gameId: Int): Game?
    suspend fun deleteGameById(gameId: Int)
    suspend fun searchGame(query: String): List<Game?>
}