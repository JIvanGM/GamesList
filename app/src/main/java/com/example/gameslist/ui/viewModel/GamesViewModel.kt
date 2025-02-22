package com.example.gameslist.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameslist.data.models.Game
import com.example.gameslist.domain.GetGames
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(
    private val getGamesD: GetGames,
) : ViewModel() {
    private val _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>> get() = _games

    init {
        getGames()
    }

    private fun getGames() {
        viewModelScope.launch {
            try {
                val games = getGamesD()
                _games.value = games
            } catch (_: Exception) {
            }
        }
    }
}
