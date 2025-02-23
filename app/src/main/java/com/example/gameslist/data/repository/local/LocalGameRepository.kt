package com.example.gameslist.data.repository.local

import com.example.gameslist.data.datasource.local.GamesDao
import com.example.gameslist.data.models.Game
import javax.inject.Inject

class LocalGameRepository @Inject constructor(
    private val gamesDao: GamesDao,
) : LocalGameRepositoryInterface {

    override suspend fun getAllGames(): List<Game> {
        return gamesDao.getAllGames().sortedBy { it.title }
    }

    override suspend fun insertAllGames(games: List<Game>) {
        return gamesDao.insertAll(games)
    }

    override suspend fun getGameById(gameId: Int): Game {
        return gamesDao.getGameById(gameId)
    }

    override suspend fun deleteGameById(gameId: Int) {
        gamesDao.deleteGameById(gameId)
    }

    override suspend fun searchGame(query: String): List<Game> {
        return gamesDao.searchGame(query).sortedBy { it.title }
    }
}