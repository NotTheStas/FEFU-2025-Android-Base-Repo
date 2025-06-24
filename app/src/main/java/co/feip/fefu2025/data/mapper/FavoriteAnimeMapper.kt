package co.feip.fefu2025.data.mapper

import co.feip.fefu2025.data.local.FavoriteAnimeEntity
import co.feip.fefu2025.domain.model.Anime

fun FavoriteAnimeEntity.toDomain(): Anime {
    return Anime(
        id = this.id,
        title = this.title,
        description = this.description,
        imageUrl = this.imageUrl,
        rating = this.rating,
        genres = emptyList(),
        image = 0,
        year = null,
        episodesCount = null
    )
}

fun Anime.toFavoriteEntity(): FavoriteAnimeEntity {
    return FavoriteAnimeEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        imageUrl = this.imageUrl,
        rating = this.rating
    )
}