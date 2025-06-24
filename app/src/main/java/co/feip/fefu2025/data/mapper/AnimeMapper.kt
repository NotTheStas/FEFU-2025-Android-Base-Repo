package co.feip.fefu2025.data.mapper

import co.feip.fefu2025.data.remote.dto.AnimeDataDto
import co.feip.fefu2025.domain.model.Anime

fun AnimeDataDto.toDomain(): Anime {
    return Anime(
        id = this.mal_id,
        title = this.title_english ?: this.title,
        description = this.synopsis,
        genres = this.genres?.map { it.name } ?: emptyList(),
        rating = this.score ?: 0f,
        image = 0,
        imageUrl = this.images?.jpg?.image_url,
        year = this.year,
        episodesCount = this.episodes
    )
}