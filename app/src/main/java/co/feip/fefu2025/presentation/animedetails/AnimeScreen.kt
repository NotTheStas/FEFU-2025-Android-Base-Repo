package co.feip.fefu2025.presentation.animedetails

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

import co.feip.fefu2025.ui.layouts.CustomFlexBoxLayout
import co.feip.fefu2025.presentation.elements.AnimeGenreView
import co.feip.fefu2025.presentation.elements.AnimeCard

import co.feip.fefu2025.data.repository.AnimeRepositoryImpl
import co.feip.fefu2025.domain.usecase.GetAnimeDetailsUseCase
import co.feip.fefu2025.domain.usecase.GetAnimeListUseCase
import kotlinx.coroutines.runBlocking

@Composable
fun AnimeScreen(
    imageResId: Int,
    title: String,
    genres: List<String>,
    colors: List<Int>,
    description: String,
    rating: Float,
    releaseYear: Int,
    episodeCount: Int,
    animeCatalogTest: List<AnimeTest>
) {
    Column(
        modifier = Modifier
            .background(ComposeColor(230, 230, 250))
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(330.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = ComposeColor.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Год выпуска: $releaseYear",
            fontSize = 14.sp,
            color = ComposeColor.DarkGray
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Количество эпизодов: $episodeCount",
            fontSize = 14.sp,
            color = ComposeColor.DarkGray
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Рейтинг: $rating",
            fontSize = 14.sp,
            color = ComposeColor.DarkGray
        )

        Spacer(modifier = Modifier.height(16.dp))

        AndroidView(
            factory = { context: Context ->
                CustomFlexBoxLayout(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }.also { flexBoxLayout ->
                    genres.forEachIndexed { index, genre ->
                        val animeView = AnimeGenreView(flexBoxLayout.context).apply {
                            setGenreName(genre)
                            val colorIndex = if (index < colors.size) index else 0
                            val color = colors.getOrElse(colorIndex) { Color.LTGRAY }
                            setBackgroundColor(color)
                        }
                        flexBoxLayout.addView(animeView)
                    }
                }
            },
            modifier = Modifier.wrapContentWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Описание",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = ComposeColor.Black,
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            fontSize = 16.sp,
            color = ComposeColor.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Может понравиться:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = ComposeColor.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            contentPadding = PaddingValues(start = 4.dp, end = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(animeCatalogTest) { anime ->
                AnimeCard(
                    title = anime.title,
                    rating = anime.rating,
                    genres = anime.genres,
                    imageResId = anime.image
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}

data class AnimeTest(
    val title: String,
    val rating: Float,
    val genres: List<String>,
    val image: Int
)

@Preview(showBackground = true)
@Composable
fun AnimeScreenPreview() {
    val animeIdToShow = 1

    val repository = AnimeRepositoryImpl()
    val getAnimeDetailsUseCase = GetAnimeDetailsUseCase(repository)
    val getAnimeListUseCase = GetAnimeListUseCase(repository)

    val (animeDetails, recommendationsDomain) = runBlocking {
        val details = getAnimeDetailsUseCase(animeIdToShow)
        val allAnime = getAnimeListUseCase()
        val recs = allAnime.filter { it.id != animeIdToShow }
        details to recs
    }

    if (animeDetails == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Аниме с ID $animeIdToShow не найдено в репозитории")
        }
        return
    }

    val recommendationsForPreview = recommendationsDomain.map { domainAnime ->
        AnimeTest(
            title = domainAnime.title,
            rating = domainAnime.rating,
            genres = domainAnime.genres,
            image = domainAnime.image
        )
    }

    val genreColors = List(animeDetails.genres.size) { Color.LTGRAY }

    MaterialTheme {
        AnimeScreen(
            imageResId = animeDetails.image,
            title = animeDetails.title,
            genres = animeDetails.genres,
            colors = genreColors,
            description = animeDetails.description,
            rating = animeDetails.rating,
            releaseYear = animeDetails.year,
            episodeCount = animeDetails.episodesCount,
            animeCatalogTest = recommendationsForPreview
        )
    }
}