package co.feip.fefu2025.domain.model

data class Anime(
    val id: Int,
    val title: String,
    val description: String,
    val genres: List<String>,
    val rating: Float,
    val image: Int,
    val year: Int,
    val episodesCount: Int,
)