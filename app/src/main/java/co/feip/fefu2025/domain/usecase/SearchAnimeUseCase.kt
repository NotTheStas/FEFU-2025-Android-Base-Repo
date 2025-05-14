package co.feip.fefu2025.domain.usecase

import co.feip.fefu2025.domain.model.PaginatedAnimeResult
import co.feip.fefu2025.domain.repository.AnimeRepository

class SearchAnimeUseCase(private val animeRepository: AnimeRepository) {
    suspend operator fun invoke(query: String, page: Int): PaginatedAnimeResult {
        return animeRepository.searchAnime(query, page)
    }
}