package co.feip.fefu2025

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

@Composable
fun AnimeScreen(
    imageResId: Int,
    title: String,
    genres: List<String>,
    colors: List<Int>,
    description: String,
    rating: Float,
    releaseYear: Int,
    episodeCount: Int
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
                contentDescription = null,
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
                            setBackgroundColor(colors[index])
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
            color = ComposeColor.Black
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            fontSize = 16.sp,
            color = ComposeColor.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AnimeScreenPreview() {
    val genres = listOf("Драма", "Фантастика", "Триллер", "Психологическое")
    val colors = listOf(Color.LTGRAY, Color.LTGRAY, Color.LTGRAY, Color.LTGRAY)
    AnimeScreen(
        imageResId = R.drawable.test,
        title = "Врата Штейна",
        genres = genres,
        colors = colors,
        description = "Сняв в Акихабаре квартиру, самопровозглашённый сумасшедший учёный Окабэ Ринтаро устроил там «лабораторию» и в компании своей подруги детства Сины Маюри и хакера-отаку Хасиды Итару изобретает «гаджеты будущего». Троица отлично проводит время вместе, работая над совместным проектом — «мобиловолновкой», которой можно управлять с помощью текстовых сообщений.\n" +
                "Вскоре «сотрудники лаборатории» сталкиваются с чередой загадочных инцидентов, которые приводят к открытию, изменившему правила игры: «мобиловолновка» может отправлять электронные письма в прошлое и таким образом изменять историю.",
        rating = 9.07f,
        releaseYear = 2011,
        episodeCount = 24
    )
}