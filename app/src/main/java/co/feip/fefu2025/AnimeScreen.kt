package co.feip.fefu2025

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
        episodeCount = 24,
        animeCatalogTest = listOf(
            AnimeTest("Комбатанты будут высланы!", 7.1f, listOf("Экшен", "Комедия", "Фэнтези"), R.drawable.sentouin_haken_shimasu),
            AnimeTest("Атака титанов", 8.5f, listOf("Экшен", "Сёнен", "Драма"), R.drawable.shingeki_no_kyojin),
            AnimeTest("Семья шпиона", 8.4f, listOf("Экшен", "Сёнен", "Комедия"), R.drawable.spy_x_family),
            AnimeTest("Восхождение героя щита", 7.9f, listOf("Экшен", "Приключения", "Драма"), R.drawable.tate_no_yuusha_no_nariagari),
            AnimeTest("Твоё имя", 8.8f, listOf("Драма"), R.drawable.kimi_no_na_wa),
            AnimeTest("Доктор Стоун", 8.2f, listOf("Сёнен", "Приключения", "Комедия"), R.drawable.dr_stone),
            AnimeTest("Поднятие уровня в одиночку", 8.2f, listOf("Экшен", "Приключения", "Фэнтези"), R.drawable.ore_dake_level_up_na_ken),
            AnimeTest("Тяжкий труд в подземелье", 7.2f, listOf("Комедия", "Фентези"), R.drawable.meikyuu_black_company),
            AnimeTest("Акудама Драйв", 7.9f, listOf("Экшен", "Фантастика", "Триллер"), R.drawable.akudama_drive),
            AnimeTest("Подземелье Вкусностей", 8.7f, listOf("Сэйнэн", "Комедия", "Фэнтези"), R.drawable.dungeon_meshi)
        )
    )
}