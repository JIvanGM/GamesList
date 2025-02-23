package com.example.gameslist.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.gameslist.data.models.Game
import com.example.gameslist.ui.navigation.Screens
import com.example.gameslist.ui.viewModel.GamesViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(gamesViewModel: GamesViewModel, navController: NavController) {
    val games = gamesViewModel.games.observeAsState(listOf()).value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Games App") }
            )
        }
    ) { innerPadding ->
        val listState = rememberLazyListState()
        val scope = rememberCoroutineScope()
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues = innerPadding)
        ) {
            val grouped = games.groupBy { it.title[0] }
            grouped.forEach { initial, games ->
                stickyHeader {
                    CharacterHeader(char = initial, Modifier.fillParentMaxWidth())
                }
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
                Text(text = game.title, fontWeight = FontWeight.Bold)
                Text(text = game.shortDescription, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            IconButton(
                onClick = {
                    gamesViewModel.deleteGameById(game.id)
                },
                modifier = Modifier
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
private fun CharacterHeader(char: Char, modifier: Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(start = 12.dp, top = 6.dp, bottom = 6.dp)
    ) {
        Text(
            text = char.toString(),
            style = TextStyle(color = MaterialTheme.colorScheme.primary, fontSize = 20.sp),
            fontWeight = FontWeight.Bold,
        )
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
            Icon(Icons.Filled.KeyboardArrowUp, "arrow up")
        }
    }
}
