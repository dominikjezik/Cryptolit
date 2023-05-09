package sk.dominikjezik.cryptolit.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sk.dominikjezik.cryptolit.models.StoredCoin

@Dao
interface CoinDAO {

    @Query("SELECT * FROM coins WHERE type='FAVOURITE'")
    suspend fun getFavouriteCoins(): List<StoredCoin>

    @Query("SELECT * FROM coins WHERE type='WATCHLIST'")
    suspend fun getWatchlistCoins(): List<StoredCoin>

    @Query("SELECT * FROM coins WHERE coinId = :coinId")
    suspend fun findCoinById(coinId: String): List<StoredCoin>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoin(coin: StoredCoin)

    @Delete
    suspend fun deleteCoin(coin: StoredCoin)

}