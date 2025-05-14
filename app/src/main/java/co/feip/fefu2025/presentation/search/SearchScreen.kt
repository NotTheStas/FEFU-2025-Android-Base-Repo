package co.feip.fefu2025.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import co.feip.fefu2025.R
import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.presentation.common.ErrorView
import co.feip.fefu2025.presentation.common.UiState
import co.feip.fefu2025.presentation.elements.AnimeCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    query: String,
    uiState: UiState<List<Anime>>,
    onQueryChange: (String) -> Unit,
    onRetry: () -> Unit,
    onAnimeClick: (Int) -> Unit,
    navController: NavController,
    onLoadNextPage: () -> Unit,
    isLoadingNextPage: Boolean,
    canLoadMore: Boolean
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .padding(end = 8.dp),
                        placeholder = { Text("Поиск аниме...") },
                        singleLine = true,
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                IconButton(onClick = { onQueryChange("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Очистить")
                                }
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        keyboardController?.hide()
                        navController.navigateUp()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is UiState.Success -> {
                    val results = uiState.data
                    val listState = rememberLazyGridState()

                    if (query.isBlank() && results.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Введите название для поиска", color = MaterialTheme.colorScheme.outline)
                        }
                    } else if (results.isEmpty() && query.isNotBlank() && !isLoadingNextPage && uiState !is UiState.Loading) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Ничего не найдено по запросу \"$query\"", color = MaterialTheme.colorScheme.outline)
                        }
                    } else {
                        LazyVerticalGrid(
                            state = listState,
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(results, key = { anime -> anime.id }) { anime ->
                                Box(modifier = Modifier.clickable { onAnimeClick(anime.id) }) {
                                    AnimeCard(
                                        title = anime.title,
                                        rating = anime.rating,
                                        genres = anime.genres,
                                        imageUrl = anime.imageUrl,
                                        placeholderResId = R.drawable.test
                                    )
                                }
                            }

                            if (isLoadingNextPage && canLoadMore) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                        }

                        val reachedBottom = remember {
                            derivedStateOf {
                                val layoutInfo = listState.layoutInfo
                                val visibleItemsInfo = layoutInfo.visibleItemsInfo
                                if (layoutInfo.totalItemsCount == 0) {
                                    false
                                } else {
                                    val lastVisibleItem = visibleItemsInfo.lastOrNull()
                                    lastVisibleItem != null &&
                                            lastVisibleItem.index >= layoutInfo.totalItemsCount - 1 - (2*2) &&
                                            canLoadMore &&
                                            !isLoadingNextPage
                                }
                            }
                        }

                        LaunchedEffect(reachedBottom.value) {
                            if (reachedBottom.value) {
                                onLoadNextPage()
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    ErrorView(
                        message = uiState.message,
                        onRetry = onRetry,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}