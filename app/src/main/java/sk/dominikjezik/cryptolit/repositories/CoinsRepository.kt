package sk.dominikjezik.cryptolit.repositories

import sk.dominikjezik.cryptolit.api.CoinGeckoService
import sk.dominikjezik.cryptolit.data.CoinDAO
import sk.dominikjezik.cryptolit.models.StoredCoin
import sk.dominikjezik.cryptolit.models.StoredCoinType
import javax.inject.Inject

class CoinsRepository @Inject constructor(
    private val coinGeckoService: CoinGeckoService,
    private val coinDAO: CoinDAO
){

    suspend fun getCoins(id: String) = coinGeckoService.getCoins(id, "eur")

    suspend fun getCoinInfo(id: String) = coinGeckoService.getCoinInfo(id)

    suspend fun getCoinChartData(id: String, days: Int ) = coinGeckoService.getCoinChartData(id, "eur", days)

    suspend fun getStoredFavouriteCoins() = coinDAO.getFavouriteCoins()

    suspend fun getStoredWatchlistCoins() = coinDAO.getWatchlistCoins()

    suspend fun findCoinById(coinId: String) = coinDAO.findCoinById(coinId)

    suspend fun insertFavouriteCoin(coinId: String) = coinDAO.insertCoin(StoredCoin(coinId, StoredCoinType.FAVOURITE))

    suspend fun insertWatchlistCoin(coinId: String) = coinDAO.insertCoin(StoredCoin(coinId, StoredCoinType.WATCHLIST))

    suspend fun deleteFavouriteCoin(coinId: String) = coinDAO.deleteCoin(StoredCoin(coinId, StoredCoinType.FAVOURITE))

    suspend fun deleteWatchlistCoin(coinId: String) = coinDAO.deleteCoin(StoredCoin(coinId, StoredCoinType.WATCHLIST))

}