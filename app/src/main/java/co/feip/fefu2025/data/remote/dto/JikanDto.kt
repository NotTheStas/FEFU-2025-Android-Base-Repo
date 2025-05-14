package co.feip.fefu2025.data.remote.dto

data class JikanResponse(
    val pagination: PaginationDto,
    val data: List<AnimeDataDto>
)

data class PaginationDto(
    val has_next_page: Boolean,
    val current_page: Int
)

data class AnimeDataDto(
    val mal_id: Int,
    val images: ImagesDto?,
    val title: String,
    val title_english: String?,
    val episodes: Int?,
    val score: Float?,
    val synopsis: String?,
    val year: Int?,
    val genres: List<MalUrlDto>?

)

data class ImagesDto(
    val jpg: ImageTypeDto?
)

data class ImageTypeDto(
    val image_url: String?,
)

data class MalUrlDto(
    val name: String
)

data class AnimeDetailsResponse(
    val data: AnimeDataDto
)

data class AnimeRecommendationsResponse(
    val data: List<RecommendationEntryDto>
)

data class RecommendationEntryDto(
    val entry: AnimeDataDto
)