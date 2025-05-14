package co.feip.fefu2025.data.mapper

import co.feip.fefu2025.data.remote.dto.AnimeDataDto
import co.feip.fefu2025.domain.model.Anime

fun AnimeDataDto.toDomain(): Anime {
    return Anime(
        id = this.mal_id,
        title = if (this.title_english?.isNotBlank() == true) this.title_english else this.title,
        description = this.synopsis,
        genres = this.genres?.mapNotNull { it.name } ?: emptyList(),
        rating = this.score ?: 0.0f,
        image = 0,
        imageUrl = this.images?.jpg?.image_url,
        year = this.year,
        episodesCount = this.episodes
    )
}