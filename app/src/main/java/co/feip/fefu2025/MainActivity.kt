package co.feip.fefu2025

<<<<<<< Updated upstream
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import co.feip.fefu2025.ui.theme.FEFU2025AndroidBaseRepoTheme
=======
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.app.Activity

class MainActivity : Activity() {

    private lateinit var flexBoxLayout: CustomFlexBoxLayout
>>>>>>> Stashed changes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
<<<<<<< Updated upstream
        enableEdgeToEdge()
        setContent {
            FEFU2025AndroidBaseRepoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "FEIP",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
=======
        setContentView(R.layout.main_activity)

        flexBoxLayout = findViewById(R.id.flexBoxLayout)

        val addButton = findViewById<View>(R.id.addButton)

        addButton.setOnClickListener {
            flexBoxLayout.addView(createAnimeView())
>>>>>>> Stashed changes
        }
    }
}

<<<<<<< Updated upstream
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FEFU2025AndroidBaseRepoTheme {
        Greeting("Android")
=======
    private fun createAnimeView(): AnimeGenreView {
        val animeView = AnimeGenreView(this)

        val genres = listOf("Комедия", "Повседневность", "Музыка", "Школа", "Дружба", "Фэнтези")
        val colors = listOf(Color.BLUE, Color.RED, Color.rgb(18, 128, 40), Color.rgb(179, 0, 255), Color.MAGENTA, Color.rgb(255, 111, 0))
        val randomIndex = (0..5).random()

        animeView.setGenreName(genres[randomIndex])
        animeView.setBackgroundColor(colors[randomIndex])

        return animeView
>>>>>>> Stashed changes
    }
}