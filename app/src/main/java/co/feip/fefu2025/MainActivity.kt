package co.feip.fefu2025

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.savedstate.SavedStateRegistryOwner
import co.feip.fefu2025.data.local.AppDatabase
import co.feip.fefu2025.data.repository.AnimeRepositoryImpl
import co.feip.fefu2025.domain.repository.AnimeRepository
import co.feip.fefu2025.domain.usecase.*
import co.feip.fefu2025.presentation.animedetails.AnimeDetailsViewModel
import co.feip.fefu2025.presentation.animedetails.AnimeScreen
import co.feip.fefu2025.presentation.animedetails.FavoriteDetailsScreen
import co.feip.fefu2025.presentation.favorites.FavoritesScreen
import co.feip.fefu2025.presentation.favorites.FavoritesViewModel
import co.feip.fefu2025.presentation.mainscreen.AnimeHomeScreen
import co.feip.fefu2025.presentation.mainscreen.MainViewModel
import co.feip.fefu2025.presentation.recommendations.RecommendationsScreen
import co.feip.fefu2025.presentation.search.SearchScreen
import co.feip.fefu2025.presentation.search.SearchViewModel


abstract class BaseViewModelFactory(protected val context: Context) : ViewModelProvider.Factory {
    protected val repository: AnimeRepository by lazy {
        val dao = AppDatabase.getDatabase(context).favoriteAnimeDao()
        AnimeRepositoryImpl(dao)
    }
}

class MainViewModelFactory(context: Context) : BaseViewModelFactory(context) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val getAnimeListUseCase = GetAnimeListUseCase(repository)
        return MainViewModel(getAnimeListUseCase) as T
    }
}

class AnimeDetailsViewModelFactory(
    owner: SavedStateRegistryOwner,
    context: Context,
    private val defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    private val repository: AnimeRepository by lazy {
        val dao = AppDatabase.getDatabase(context.applicationContext).favoriteAnimeDao()
        AnimeRepositoryImpl(dao)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
        return AnimeDetailsViewModel(
            getAnimeDetailsUseCase = GetAnimeDetailsUseCase(repository),
            getAnimeRecommendationsUseCase = GetAnimeRecommendationsUseCase(repository),
            getFavoriteAnimeByIdUseCase = GetFavoriteAnimeByIdUseCase(repository),
            isAnimeFavoriteUseCase = IsAnimeFavoriteUseCase(repository),
            addFavoriteUseCase = AddFavoriteUseCase(repository),
            removeFavoriteUseCase = RemoveFavoriteUseCase(repository),
            savedStateHandle = handle
        ) as T
    }
}

class SearchViewModelFactory(context: Context) : BaseViewModelFactory(context) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val searchAnimeUseCase = SearchAnimeUseCase(repository)
        return SearchViewModel(searchAnimeUseCase) as T
    }
}

class FavoritesViewModelFactory(context: Context) : BaseViewModelFactory(context) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val getFavoriteAnimeListUseCase = GetFavoriteAnimeListUseCase(repository)
        return FavoritesViewModel(getFavoriteAnimeListUseCase) as T
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
    val context = LocalContext.current.applicationContext

    val bottomNavItems = listOf(
        Screen.MainScreen,
        Screen.FavoritesScreen
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val showBottomBar = bottomNavItems.any { it.route == currentDestination?.route }

            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.MainScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.MainScreen.route) {
                val mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(context))
                val uiState by mainViewModel.animeListState.collectAsState()
                val isLoadingNextPage by mainViewModel.isLoadingNextPage.collectAsState()
                val canLoadMore by mainViewModel.canLoadMore.collectAsState()

                AnimeHomeScreen(
                    uiState = uiState,
                    onRetry = { mainViewModel.fetchAnimeList() },
                    onLoadNextPage = { mainViewModel.loadNextPage() },
                    onAnimeClick = { animeId ->
                        navController.navigate(Screen.AnimeDetailsScreen.createRoute(animeId))
                    },
                    onSearchClick = {
                        navController.navigate(Screen.SearchScreen.route)
                    },
                    isLoadingNextPage = isLoadingNextPage,
                    canLoadMore = canLoadMore
                )
            }

            composable(route = Screen.FavoritesScreen.route) {
                val favoritesViewModel: FavoritesViewModel = viewModel(factory = FavoritesViewModelFactory(context))
                FavoritesScreen(
                    viewModel = favoritesViewModel,
                    onAnimeClick = { animeId ->
                        navController.navigate(Screen.FavoriteDetailsScreen.createRoute(animeId))
                    }
                )
            }

            composable(
                route = Screen.FavoriteDetailsScreen.route,
                arguments = listOf(navArgument("animeId") { type = NavType.IntType })
            ) { backStackEntry ->
                val animeId = backStackEntry.arguments?.getInt("animeId") ?: 0
                val factory = AnimeDetailsViewModelFactory(owner = backStackEntry, context = context, defaultArgs = backStackEntry.arguments)
                val detailsViewModel: AnimeDetailsViewModel = viewModel(factory = factory)

                FavoriteDetailsScreen(navController = navController, viewModel = detailsViewModel, animeId = animeId)
            }

            composable(
                route = Screen.AnimeDetailsScreen.route,
                arguments = listOf(navArgument("animeId") { type = NavType.IntType }),
                deepLinks = listOf(navDeepLink { uriPattern = "mysuperapp://anime/{animeId}"; action = Intent.ACTION_VIEW })
            ) { backStackEntry ->
                val factory = AnimeDetailsViewModelFactory(owner = backStackEntry, context = context, defaultArgs = backStackEntry.arguments)
                val detailsViewModel: AnimeDetailsViewModel = viewModel(factory = factory)
                val detailsUiState by detailsViewModel.animeDetailsState.collectAsState()
                val recommendationsState by detailsViewModel.recommendationsState.collectAsState()
                val isFavorite by detailsViewModel.isFavorite.collectAsState()

                AnimeScreen(
                    detailsUiState = detailsUiState,
                    recommendations = recommendationsState,
                    isFavorite = isFavorite,
                    onToggleFavorite = { detailsViewModel.toggleFavoriteStatus() },
                    onRetryDetails = { detailsViewModel.loadAnimeDetails() },
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
                arguments = listOf(navArgument("animeId") { type = NavType.IntType })
            ) { backStackEntry ->
                val animeIdForRecommendations = backStackEntry.arguments?.getInt("animeId")
                val factory = AnimeDetailsViewModelFactory(owner = backStackEntry, context = context, defaultArgs = backStackEntry.arguments)
                val viewModel: AnimeDetailsViewModel = viewModel(factory = factory)
                val recommendationsState by viewModel.recommendationsState.collectAsState()

                if (animeIdForRecommendations == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Ошибка: ID аниме для рекомендаций не найден.")
                    }
                } else {
                    RecommendationsScreen(
                        recommendations = recommendationsState,
                        navController = navController,
                        onAnimeClick = { clickedAnimeId ->
                            navController.navigate(Screen.AnimeDetailsScreen.createRoute(clickedAnimeId)) {
                                popUpTo(Screen.AnimeDetailsScreen.createRoute(animeIdForRecommendations)) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }

            composable(route = Screen.SearchScreen.route) {
                val searchViewModel: SearchViewModel = viewModel(factory = SearchViewModelFactory(context))
                val query by searchViewModel.query.collectAsState()
                val searchResultsState by searchViewModel.searchResults.collectAsState()
                val isLoadingNextPage by searchViewModel.isLoadingNextPage.collectAsState()
                val canLoadMore by searchViewModel.canLoadMore.collectAsState()

                SearchScreen(
                    query = query,
                    uiState = searchResultsState,
                    onQueryChange = { searchViewModel.updateQuery(it) },
                    onRetry = { searchViewModel.retrySearch() },
                    onAnimeClick = { animeId ->
                        navController.navigate(Screen.AnimeDetailsScreen.createRoute(animeId))
                    },
                    navController = navController,
                    onLoadNextPage = { searchViewModel.loadNextSearchPage() },
                    isLoadingNextPage = isLoadingNextPage,
                    canLoadMore = canLoadMore
                )
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object MainScreen : Screen("main_screen", "Главная", Icons.Default.Home)
    object FavoritesScreen : Screen("favorites_screen", "Избранное", Icons.Default.Favorite)

    object AnimeDetailsScreen : Screen("anime_details/{animeId}", "Детали", Icons.Default.Home) {
        fun createRoute(animeId: Int) = "anime_details/$animeId"
    }

    object FavoriteDetailsScreen : Screen("favorite_details/{animeId}", "Детали", Icons.Default.Favorite) {
        fun createRoute(animeId: Int) = "favorite_details/$animeId"
    }

    object RecommendationsScreen : Screen("recommendations/{animeId}", "Рекомендации", Icons.Default.Home) {
        fun createRoute(animeId: Int) = "recommendations/$animeId"
    }

    object SearchScreen : Screen("search_screen", "Поиск", Icons.Default.Home)
}