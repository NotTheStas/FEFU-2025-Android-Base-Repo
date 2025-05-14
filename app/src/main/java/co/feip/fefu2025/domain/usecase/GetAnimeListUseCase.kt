package co.feip.fefu2025.domain.usecase

import co.feip.fefu2025.domain.model.PaginatedAnimeResult
import co.feip.fefu2025.domain.repository.AnimeRepository

class GetAnimeListUseCase(private val animeRepository: AnimeRepository) {

    suspend operator fun invoke(page: Int): PaginatedAnimeResult {
        return animeRepository.getAnimeList(page)
    }
}