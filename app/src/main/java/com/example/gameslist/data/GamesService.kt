package com.example.gameslist.data

import com.example.gameslist.data.models.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GamesService @Inject constructor(private val apiService: ApiService) {
    suspend fun getGames(): List<Game> {
        return withContext(Dispatchers.IO) {
            val games = apiService.getGames()
            games.body() ?: emptyList()
        }
    }
}