package sk.dominikjezik.cryptolit.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sk.dominikjezik.cryptolit.models.StoredCoin

@Dao
interface CoinDAO {

    /**
     * Vráti všetky uložené coiny, ktoré sú označené ako favourite.
     *
     * @return favourite coins
     */
    @Query("SELECT * FROM coins WHERE type='FAVOURITE'")
    suspend fun getFavouriteCoins(): List<StoredCoin>


    /**
     * Vráti všetky uložené coiny, ktoré sú označené ako watchlist.
     *
     * @return watchlist coins
     */
    @Query("SELECT * FROM coins WHERE type='WATCHLIST'")
    suspend fun getWatchlistCoins(): List<StoredCoin>


    /**
     * Vráti všetky uložené coiny.
     *
     * @return coins
     */
    @Query("SELECT * FROM coins")
    suspend fun getStoredCoins(): List<StoredCoin>


    /**
     * Nájde všetky coiny s daným id. Pozn. návratová hodnota musí byť
     * ako list, pretože jeden coin môže byť uložený aj ako
     * favourite aj ako watchlist.
     *
     * @param coinId
     * @return coins
     */
    @Query("SELECT * FROM coins WHERE coinId = :coinId")
    suspend fun findCoinById(coinId: String): List<StoredCoin>


    /**
     * Metóda uloží daný coin do databázy.
     *
     * @param coin
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoin(coin: StoredCoin)


    /**
     * Metóda odstráni daný coin z databázy.
     *
     * @param coin
     */
    @Delete
    suspend fun deleteCoin(coin: StoredCoin)

}