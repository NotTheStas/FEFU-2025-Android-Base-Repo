package co.feip.fefu2025.domain.usecase

import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.domain.repository.AnimeRepository

class AddFavoriteUseCase(private val animeRepository: AnimeRepository) {
    suspend operator fun invoke(anime: Anime) {
        animeRepository.addAnimeToFavorites(anime)
    }
}