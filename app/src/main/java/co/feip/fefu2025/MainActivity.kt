package co.feip.fefu2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.graphics.Color
import android.view.View

private lateinit var flexBoxLayout: CustomFlexBoxLayout

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        flexBoxLayout = findViewById(R.id.flexBoxLayout)

        val addButton = findViewById<View>(R.id.addButton)

        addButton.setOnClickListener {
            flexBoxLayout.addView(createAnimeView())
        }
    }

    private fun createAnimeView(): AnimeGenreView {
        val animeView = AnimeGenreView(this)

        val genres = listOf("Комедия", "Повседневность", "Музыка", "Школа", "Дружба", "Фэнтези")
        val colors = listOf(Color.BLUE, Color.RED, Color.rgb(18, 128, 40), Color.rgb(179, 0, 255), Color.MAGENTA, Color.rgb(255, 111, 0))
        val randomIndex = (0..5).random()

        animeView.setGenreName(genres[randomIndex])
        animeView.setBackgroundColor(colors[randomIndex])

        return animeView
    }
}