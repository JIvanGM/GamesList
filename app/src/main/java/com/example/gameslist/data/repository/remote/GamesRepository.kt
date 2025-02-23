package com.example.gameslist.data.repository.remote

import com.example.gameslist.data.datasource.remote.GamesService
import com.example.gameslist.data.models.Game
import javax.inject.Inject

class GamesRepository @Inject constructor(private val gamesService: GamesService) {
    suspend fun getGames(): List<Game> {
        return gamesService.getGames()
    }
}