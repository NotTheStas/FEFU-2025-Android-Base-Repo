package co.feip.fefu2025.data.remote

import co.feip.fefu2025.data.remote.dto.JikanResponse
import co.feip.fefu2025.data.remote.dto.AnimeDetailsResponse
import co.feip.fefu2025.data.remote.dto.AnimeRecommendationsResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Path
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("top/anime")
    suspend fun getTopAnime(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 20
    ): JikanResponse

    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int = 20,
        @Query("sfw") sfw: Boolean = true
    ): JikanResponse

    @GET("anime/{id}")
    suspend fun getAnimeDetails(@Path("id") animeId: Int): AnimeDetailsResponse

    @GET("anime/{id}/recommendations")
    suspend fun getAnimeRecommendations(@Path("id") animeId: Int): AnimeRecommendationsResponse

    companion object {
        private const val BASE_URL = "https://api.jikan.moe/v4/"

        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}