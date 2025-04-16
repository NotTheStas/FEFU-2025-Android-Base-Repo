package co.feip.fefu2025.presentation.elements

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
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
import co.feip.fefu2025.R

@Composable
fun AnimeCard(
    imageResId: Int,
    title: String,
    genres: List<String>,
    rating: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.45f)
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(ComposeColor(230, 230, 250))
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.BottomEnd)
                        .background(
                            ComposeColor.Black.copy(alpha = 0.8f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = ComposeColor.Yellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$rating",
                        fontSize = 12.sp,
                        color = ComposeColor.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = ComposeColor.Black,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            AndroidView(
                factory = { context: Context ->
                    CustomFlexBoxLayout(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    }.also { flexBoxLayout ->
                        genres.forEachIndexed { index, genre ->
                            val animeView = AnimeGenreView(flexBoxLayout.context).apply {
                                setGenreName(genre)
                                setBackgroundColor(Color.LTGRAY)
                            }
                            flexBoxLayout.addView(animeView)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimeCardPreview() {
    val genres = listOf("Драма", "Фантастика", "Триллер")
    AnimeCard(
        imageResId = R.drawable.test,
        title = "Врата Штейна",
        genres = genres,
        rating = 9.07f
    )
}