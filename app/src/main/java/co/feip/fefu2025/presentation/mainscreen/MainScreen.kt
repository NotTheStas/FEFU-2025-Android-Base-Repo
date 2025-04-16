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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import co.feip.fefu2025.R
import co.feip.fefu2025.presentation.elements.AnimeCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeHomeScreen(animeCatalog: List<AnimeExample>) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

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
    AnimeHomeScreen(
        animeCatalog = listOf(
            AnimeExample("Комбатанты будут высланы!", 7.1f, listOf("Экшен", "Комедия", "Фэнтези"), R.drawable.sentouin_haken_shimasu),
            AnimeExample("Атака титанов", 8.5f, listOf("Экшен", "Сёнен", "Драма"), R.drawable.shingeki_no_kyojin),
            AnimeExample("Семья шпиона", 8.4f, listOf("Экшен", "Сёнен", "Комедия"), R.drawable.spy_x_family),
            AnimeExample("Восхождение героя щита", 7.9f, listOf("Экшен", "Приключения", "Драма"), R.drawable.tate_no_yuusha_no_nariagari),
            AnimeExample("Твоё имя", 8.8f, listOf("Драма"), R.drawable.kimi_no_na_wa),
            AnimeExample("Доктор Стоун", 8.2f, listOf("Сёнен", "Приключения", "Комедия"), R.drawable.dr_stone),
            AnimeExample("Поднятие уровня в одиночку", 8.2f, listOf("Экшен", "Приключения", "Фэнтези"), R.drawable.ore_dake_level_up_na_ken),
            AnimeExample("Тяжкий труд в подземелье", 7.2f, listOf("Комедия", "Фентези"), R.drawable.meikyuu_black_company),
            AnimeExample("Акудама Драйв", 7.9f, listOf("Экшен", "Фантастика", "Триллер"), R.drawable.akudama_drive),
            AnimeExample("Подземелье Вкусностей", 8.7f, listOf("Сэйнэн", "Комедия", "Фэнтези"), R.drawable.dungeon_meshi)
        )
    )
}