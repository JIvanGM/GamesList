package com.example.gameslist.domain

import com.example.gameslist.data.models.Game
import com.example.gameslist.data.repository.remote.GamesRepository
import javax.inject.Inject

class GetGames @Inject constructor(private val gamesRepository: GamesRepository) {
    suspend operator fun invoke(): List<Game> {
        return gamesRepository.getGames()
    }
}