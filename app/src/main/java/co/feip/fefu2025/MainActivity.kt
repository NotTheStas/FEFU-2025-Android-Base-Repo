package co.feip.fefu2025

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.savedstate.SavedStateRegistryOwner
import co.feip.fefu2025.data.repository.AnimeRepositoryImpl
import co.feip.fefu2025.domain.usecase.GetAnimeDetailsUseCase
import co.feip.fefu2025.domain.usecase.GetAnimeListUseCase
import co.feip.fefu2025.presentation.animedetails.AnimeDetailsViewModel
import co.feip.fefu2025.presentation.animedetails.AnimeScreen
import co.feip.fefu2025.presentation.mainscreen.AnimeHomeScreen
import co.feip.fefu2025.presentation.mainscreen.MainViewModel
import co.feip.fefu2025.presentation.recommendations.RecommendationsScreen


class MainViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = AnimeRepositoryImpl()
        val getAnimeListUseCase = GetAnimeListUseCase(repository)
        return MainViewModel(getAnimeListUseCase) as T
    }
}

class AnimeDetailsViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        val repository = AnimeRepositoryImpl()
        val getAnimeDetailsUseCase = GetAnimeDetailsUseCase(repository)
        val getAnimeListUseCase = GetAnimeListUseCase(repository)
        return AnimeDetailsViewModel(
            getAnimeDetailsUseCase = getAnimeDetailsUseCase,
            getAnimeListUseCase = getAnimeListUseCase,
            savedStateHandle = handle
        ) as T
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AnimeAppNavigation()
                }
            }
        }
    }
}

@Composable
fun AnimeAppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(route = Screen.MainScreen.route) {
            val mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory())
            val animeListState by mainViewModel.animeList.collectAsState()
            AnimeHomeScreen(
                animeList = animeListState,
                onAnimeClick = { animeId ->
                    navController.navigate(Screen.AnimeDetailsScreen.createRoute(animeId))
                }
            )
        }

        composable(
            route = Screen.AnimeDetailsScreen.route,
            arguments = listOf(navArgument("animeId") {
                type = NavType.IntType
            }),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "mysuperapp://anime/{animeId}"
                    action = Intent.ACTION_VIEW
                }
            )
        ) { backStackEntry ->
            val factory = AnimeDetailsViewModelFactory(
                owner = backStackEntry,
                defaultArgs = backStackEntry.arguments
            )
            val detailsViewModel: AnimeDetailsViewModel = viewModel(factory = factory)
            val animeDetailsState by detailsViewModel.animeDetailsState.collectAsState()
            val recommendationsState by detailsViewModel.recommendationsState.collectAsState()

            AnimeScreen(
                anime = animeDetailsState,
                recommendations = recommendationsState,
                onRecommendationsHeaderClick = {
                    val currentAnimeId = backStackEntry.arguments?.getInt("animeId")
                    if (currentAnimeId != null) {
                        navController.navigate(Screen.RecommendationsScreen.createRoute(currentAnimeId))
                    }
                },
                onRecommendationClick = { recommendedAnimeId ->
                    navController.navigate(Screen.AnimeDetailsScreen.createRoute(recommendedAnimeId))
                }
            )
        }

        composable(
            route = Screen.RecommendationsScreen.route,
            arguments = listOf(navArgument("animeId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val factory = AnimeDetailsViewModelFactory(
                owner = backStackEntry,
                defaultArgs = backStackEntry.arguments
            )
            val detailsViewModel: AnimeDetailsViewModel = viewModel(factory = factory)
            val recommendationsState by detailsViewModel.recommendationsState.collectAsState()

            RecommendationsScreen(
                recommendations = recommendationsState,
                navController = navController,
                onAnimeClick = { animeId ->
                    navController.navigate(Screen.AnimeDetailsScreen.createRoute(animeId))
                }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object AnimeDetailsScreen : Screen("anime_details/{animeId}") {
        fun createRoute(animeId: Int) = "anime_details/$animeId"
    }
    object RecommendationsScreen : Screen("recommendations/{animeId}") {
        fun createRoute(animeId: Int) = "recommendations/$animeId"
    }
}