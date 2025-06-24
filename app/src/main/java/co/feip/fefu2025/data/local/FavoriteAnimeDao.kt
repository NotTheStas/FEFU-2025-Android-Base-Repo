package co.feip.fefu2025.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteAnimeDao {

    @Query("SELECT * FROM favorite_anime ORDER BY title ASC")
    fun getFavoriteAnimeList(): Flow<List<FavoriteAnimeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(anime: FavoriteAnimeEntity)

    @Query("DELETE FROM favorite_anime WHERE id = :animeId")
    suspend fun removeFavorite(animeId: Int)

    @Query("SELECT * FROM favorite_anime WHERE id = :animeId")
    fun getFavoriteById(animeId: Int): Flow<FavoriteAnimeEntity?>

    @Query("SELECT * FROM favorite_anime WHERE id = :animeId")
    suspend fun getFavoriteByIdOnce(animeId: Int): FavoriteAnimeEntity?
}