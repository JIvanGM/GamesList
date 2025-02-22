package com.example.gameslist.data

import com.example.gameslist.data.models.Game
import com.example.gameslist.util.Constants.Companion.GAMES_LIST
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET(GAMES_LIST)
    suspend fun getGames(): Response<List<Game>>
}