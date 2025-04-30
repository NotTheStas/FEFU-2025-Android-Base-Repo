package co.feip.fefu2025.presentation.mainscreen

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


import co.feip.fefu2025.presentation.elements.AnimeCard
import co.feip.fefu2025.data.repository.AnimeRepositoryImpl
import co.feip.fefu2025.domain.usecase.GetAnimeListUseCase
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeHomeScreen(animeCatalog: List<AnimeExample>) {
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        TextField(
            value = searchText,
            onValueChange = { newText -> searchText = newText },
            placeholder = {
                Text(
                    text = "Найти...",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Поиск",
                    tint = Color.Gray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .shadow(4.dp, shape = RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF0F0F0),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(animeCatalog) { anime ->
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

data class AnimeExample(
    val title: String,
    val rating: Float,
    val genres: List<String>,
    val image: Int
)

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    val repository = AnimeRepositoryImpl()
    val getAnimeListUseCase = GetAnimeListUseCase(repository)

    val animeListFromUseCase = runBlocking {
        getAnimeListUseCase()
    }

    val animeCatalogForPreview = animeListFromUseCase.map { domainAnime ->
        AnimeExample(
            title = domainAnime.title,
            rating = domainAnime.rating,
            genres = domainAnime.genres,
            image = domainAnime.image
        )
    }

    MaterialTheme {
        AnimeHomeScreen(
            animeCatalog = animeCatalogForPreview
        )
    }
}