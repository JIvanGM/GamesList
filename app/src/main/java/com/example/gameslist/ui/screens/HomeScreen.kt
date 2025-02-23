package com.example.gameslist.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.gameslist.data.models.Game
import com.example.gameslist.ui.navigation.Screens
import com.example.gameslist.ui.viewModel.GamesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(gamesViewModel: GamesViewModel, navController: NavController) {
    val games = gamesViewModel.games.observeAsState(listOf()).value
    var expanded by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    var historyItems = remember { mutableStateListOf("aion") }

    Scaffold { innerPadding ->
        val listState = rememberLazyListState()
        val scope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
        ) {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth(),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = query,
                        onQueryChange = {
                            query = it
                        },
                        onSearch = {
                            if (query.isNotEmpty()) {
                                gamesViewModel.searchGame(query)
                                historyItems.add(query)
                            } else {
                                gamesViewModel.getAllDBGames()
                            }
                            expanded = false
                        },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        placeholder = { Text("Buscar") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "search icon"
                            )
                        },
                        trailingIcon = {
                            if (expanded) {
                                Icon(
                                    Icons.Default.Close,
                                    modifier = Modifier.clickable {
                                        if (query.isNotEmpty()) {
                                            query = ""
                                        } else {
                                            expanded = false
                                        }
                                        gamesViewModel.getAllDBGames()
                                    },
                                    contentDescription = "close icon"
                                )
                            }
                        },
                    )
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
            ) {
                historyItems.forEach {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 14.dp)
                        .clickable {
                            query = it
                            gamesViewModel.searchGame(query)
                            expanded = false
                        }
                    ) {
                        Icon(
                            Icons.Default.History,
                            modifier = Modifier.padding(end = 10.dp),
                            contentDescription = "History icon"
                        )
                        Text(text = it)
                    }
                }
            }

            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(games) { game ->
                    GameCard(
                        game = game,
                        navController = navController,
                        gamesViewModel = gamesViewModel
                    )
                }
            }
        }

        val showButton by remember {
            derivedStateOf {
                listState.firstVisibleItemIndex > 0
            }
        }
        AnimatedVisibility(
            visible = showButton,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            ScrollToTopButton(onClick = {
                scope.launch {
                    listState.animateScrollToItem(0)
                }
            })
        }
    }
}

@Composable
fun GameCard(game: Game, navController: NavController, gamesViewModel: GamesViewModel) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 7.dp),
        shape = RoundedCornerShape(7.dp),
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                navController.navigate(Screens.Details.route + "/${game.id}")
            }
    ) {
        Row {
            Image(
                painter = rememberImagePainter(game.thumbnail),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = game.title + " - " + game.genre, fontWeight = FontWeight.Bold)
                Text(text = game.shortDescription, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            IconButton(
                onClick = {
                    gamesViewModel.deleteGameById(game.id)
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(.3f)
                    .padding(8.dp)
                    .background(Color.Red, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        }
    }
}


@Composable
fun ScrollToTopButton(onClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp), Alignment.BottomCenter
    ) {
        Button(
            onClick = { onClick() },
            modifier = Modifier
                .shadow(10.dp, shape = CircleShape)
                .clip(shape = CircleShape)
                .size(65.dp),
        ) {
            Icon(Icons.Default.KeyboardArrowUp, "arrow up")
        }
    }
}
