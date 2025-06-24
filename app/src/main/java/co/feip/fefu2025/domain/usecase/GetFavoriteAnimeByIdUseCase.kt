package co.feip.fefu2025.domain.usecase

import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.domain.repository.AnimeRepository

class GetFavoriteAnimeByIdUseCase(private val repository: AnimeRepository) {
    suspend operator fun invoke(id: Int): Anime? {
        return repository.getFavoriteAnimeById(id)
    }
}