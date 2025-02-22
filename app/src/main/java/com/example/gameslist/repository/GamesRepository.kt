package com.example.gameslist.repository

import com.example.gameslist.data.GamesService
import com.example.gameslist.data.models.Game
import javax.inject.Inject

class GamesRepository @Inject constructor(private val gamesService: GamesService) {
    suspend fun getGames(): List<Game> {
        return gamesService.getGames()
    }
}