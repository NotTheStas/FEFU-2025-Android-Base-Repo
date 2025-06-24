package co.feip.fefu2025.presentation.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.feip.fefu2025.R
import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.presentation.elements.AnimeCard

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onAnimeClick: (Int) -> Unit
) {
    val favoritesState by viewModel.favoritesState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        when {
            favoritesState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            favoritesState.animeList.isEmpty() -> {
                Text(
                    text = "Список избранного пуст",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                FavoritesGrid(
                    animeList = favoritesState.animeList,
                    onAnimeClick = onAnimeClick
                )
            }
        }
    }
}

@Composable
fun FavoritesGrid(
    animeList: List<Anime>,
    onAnimeClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp, start = 4.dp, end = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(animeList, key = { anime -> anime.id }) { anime ->
            Box(modifier = Modifier.clickable { onAnimeClick(anime.id) }) {
                AnimeCard(
                    title = anime.title,
                    rating = anime.rating,
                    genres = anime.genres,
                    imageUrl = anime.imageUrl,
                    placeholderResId = R.drawable.test
                )
            }
        }
    }
}