package co.feip.fefu2025.presentation.animedetails

import android.content.Context
import android.graphics.Color as AndroidColor
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

import co.feip.fefu2025.ui.layouts.CustomFlexBoxLayout
import co.feip.fefu2025.presentation.elements.AnimeGenreView
import co.feip.fefu2025.presentation.elements.AnimeCard
import co.feip.fefu2025.R
import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.presentation.common.ErrorView
import co.feip.fefu2025.presentation.common.UiState

@Composable
fun AnimeScreen(
    detailsUiState: UiState<Anime>,
    recommendations: List<Anime>,
    onRetryDetails: () -> Unit,
    onRecommendationsHeaderClick: () -> Unit,
    onRecommendationClick: (Int) -> Unit
) {
    when (detailsUiState) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is UiState.Success -> {
            val anime = detailsUiState.data
            AnimeScreenContent(
                anime = anime,
                recommendations = recommendations,
                onRecommendationsHeaderClick = onRecommendationsHeaderClick,
                onRecommendationClick = onRecommendationClick
            )
        }
        is UiState.Error -> {
            ErrorView(
                message = detailsUiState.message,
                onRetry = onRetryDetails
            )
        }
    }
}

@Composable
private fun AnimeScreenContent(
    anime: Anime?,
    recommendations: List<Anime>,
    onRecommendationsHeaderClick: () -> Unit,
    onRecommendationClick: (Int) -> Unit
) {
    if (anime == null) {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxWidth().height(330.dp).clip(RoundedCornerShape(12.dp))) {
            Image(
                painter = painterResource(id = anime.image),
                contentDescription = anime.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = anime.title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Год выпуска: ${anime.year}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Количество эпизодов: ${anime.episodesCount}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Рейтинг: ${anime.rating}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        AndroidView(
            factory = { context: Context ->
                CustomFlexBoxLayout(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }.also { flexBoxLayout ->
                    anime.genres.forEach { genre ->
                        val animeView = AnimeGenreView(flexBoxLayout.context).apply {
                            setGenreName(genre)
                            setBackgroundColor(AndroidColor.LTGRAY)
                        }
                        flexBoxLayout.addView(animeView)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()){
            Text(
                text = "Описание",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = anime.description,
                style = MaterialTheme.typography.bodyLarge.copy(
                    textIndent = TextIndent(firstLine = 12.sp)
                ),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()){
            Text(
                text = "Может понравиться:",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.clickable(onClick = onRecommendationsHeaderClick)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            contentPadding = PaddingValues(vertical = 8.dp),
            modifier = Modifier.fillMaxWidth().height(350.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(recommendations) { recommendedAnime ->
                Box(modifier = Modifier.clickable { onRecommendationClick(recommendedAnime.id) }) {
                    AnimeCard(
                        title = recommendedAnime.title,
                        rating = recommendedAnime.rating,
                        genres = recommendedAnime.genres,
                        imageResId = recommendedAnime.image
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun AnimeScreenPreview() {
    val sampleAnime = Anime(
        id = 1,
        title = "Комбатанты будут высланы!",
        description = "Описание...",
        rating = 7.1f,
        genres = listOf("Экшен", "Комедия", "Фэнтези"),
        image = R.drawable.sentouin_haken_shimasu,
        year = 2021,
        episodesCount = 12
    )
    val sampleRecommendations = listOf(
        Anime(2, "Атака титанов", "", listOf("Экшен", "Драма"), 8.5f, R.drawable.shingeki_no_kyojin, 2013, 25),
        Anime(3, "Семья шпиона", "", listOf("Экшен", "Комедия"), 8.4f, R.drawable.spy_x_family, 2022, 12),
    )

    MaterialTheme {
        AnimeScreen(
            detailsUiState = UiState.Success(sampleAnime),
            recommendations = sampleRecommendations,
            onRetryDetails = {},
            onRecommendationsHeaderClick = {},
            onRecommendationClick = {}
        )
    }
}
