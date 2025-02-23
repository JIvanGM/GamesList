package com.example.gameslist.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameslist.data.models.Game
import com.example.gameslist.domain.GetGames
import com.example.gameslist.data.repository.local.LocalGameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(
    private val getGamesD: GetGames,
    private val localGameRepository: LocalGameRepository,
) : ViewModel() {
    private val _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>> get() = _games

    private val _game = MutableLiveData<Game>()
    val game: LiveData<Game> get() = _game

    init {
        getGames()
    }

    private fun getGames() {
        viewModelScope.launch {
            try {
                val games = getGamesD()
                localGameRepository.insertAllGames(games)
                _games.value = localGameRepository.getAllGames()
            } catch (exception: Exception) {
                exception.toString()
            }
        }
    }

    fun getAllDBGames() {
        viewModelScope.launch {
            _games.value = localGameRepository.getAllGames()
        }
    }

    fun getGameById(gameId: Int) {
        viewModelScope.launch {
            val game = localGameRepository.getGameById(gameId)
            _game.value = game
        }
    }

    fun deleteGameById(id: Int) {
        viewModelScope.launch {
            localGameRepository.deleteGameById(id)
            getAllDBGames()
        }
    }

    fun searchGame(query: String) {
        viewModelScope.launch {
            _games.value = localGameRepository.searchGame(query)
        }
    }
}
