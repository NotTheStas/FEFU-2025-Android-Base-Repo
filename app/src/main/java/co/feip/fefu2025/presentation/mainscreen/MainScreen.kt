package co.feip.fefu2025.presentation.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment

import co.feip.fefu2025.R
import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.presentation.elements.AnimeCard
import co.feip.fefu2025.presentation.common.ErrorView
import co.feip.fefu2025.presentation.common.UiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeHomeScreen(
    uiState: UiState<List<Anime>>,
    onRetry: () -> Unit,
    onAnimeClick: (Int) -> Unit,
    onSearchClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onSearchClick)
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = { },
                readOnly = true,
                placeholder = { Text("Найти...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Поиск",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                singleLine = true,
                enabled = false
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Success -> {
                val animeList = uiState.data
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(bottom = 16.dp),
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
                                imageResId = anime.image
                            )
                        }
                    }
                }
            }
            is UiState.Error -> {
                ErrorView(message = uiState.message, onRetry = onRetry)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    val sampleAnimeList = listOf(
        Anime(1, "Комбатанты...", "", listOf("Экшен", "Комедия"), 7.1f, R.drawable.sentouin_haken_shimasu, 2021, 12),
        Anime(2, "Атака...", "", listOf("Экшен", "Драма"), 8.5f, R.drawable.shingeki_no_kyojin, 2013, 25),
    )
    MaterialTheme {
        AnimeHomeScreen(uiState = UiState.Success(sampleAnimeList), onRetry = {}, onAnimeClick = {}, onSearchClick = {})
    }
}
