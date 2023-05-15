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

    /**
     * Slúži na získanie prvých 100 dostupných coinov podľa market cap rank.
     */
    suspend fun getCoins() = coinGeckoService.getCoins("", "eur")


    /**
     * Získa coiny z API, ktoré sú zadané formou zoznamu uložených coinov.
     * Uložené coiny premapuje na list id coinov.
     *
     * @param coins
     */
    suspend fun getCoins(coins: List<StoredCoin>)
        = coinGeckoService.getCoins(coins.map { coin -> coin.coinId }.joinToString(","), "eur")


    /**
     * Získa ceny v časových okamihoch v zadanom intervale parametera period,
     * ktorý predstavuje dni intervalu. Parameter id je id coinu.
     *
     * @param id
     * @param period
     */
    suspend fun getCoinChartData(id: String, period: String ) = coinGeckoService.getCoinChartData(id, "eur", period)


    /**
     * Metóda slúži na získanie vyhľadávaných coinov, podľa zadaného výrazu.
     *
     * @param query
     */
    suspend fun getSearchResults(query: String) = coinGeckoService.search(query)


    /**
     * Stiahne všetky menové kurzy, ktoré sú v jednotkách BTC (bitcoin).
     */
    suspend fun getExchangeRates() = coinGeckoService.getExchangeRates()


    /**
     * Získa uložené coiny, ktoré sú uložené ako favourite.
     */
    suspend fun getStoredFavouriteCoins() = coinDAO.getFavouriteCoins()


    /**
     * Získa uložené coiny, ktoré sú uložené ako watchlist.
     */
    suspend fun getStoredWatchlistCoins() = coinDAO.getWatchlistCoins()


    /**
     * Získa uložené coiny.
     */
    suspend fun getStoredCoins() = coinDAO.getStoredCoins()


    /**
     * Získa uložené coiny, podľa zadaného id coinu.
     */
    suspend fun findCoinById(coinId: String) = coinDAO.findCoinById(coinId)


    /**
     * Vloží coin podľa zadaného id do databázy ako favourite coin.
     *
     * @param coinId
     */
    suspend fun insertFavouriteCoin(coinId: String) = coinDAO.insertCoin(StoredCoin(coinId, StoredCoinType.FAVOURITE))


    /**
     * Vloží coin podľa zadaného id do databázy ako watchlist coin.
     *
     * @param coinId
     */
    suspend fun insertWatchlistCoin(coinId: String) = coinDAO.insertCoin(StoredCoin(coinId, StoredCoinType.WATCHLIST))


    /**
     * Odstráni coin podľa zadaného id z databázy označený ako favourite coin.
     *
     * @param coinId
     */
    suspend fun deleteFavouriteCoin(coinId: String) = coinDAO.deleteCoin(StoredCoin(coinId, StoredCoinType.FAVOURITE))


    /**
     * Odstráni coin podľa zadaného id z databázy označený ako watchlist coin.
     *
     * @param coinId
     */
    suspend fun deleteWatchlistCoin(coinId: String) = coinDAO.deleteCoin(StoredCoin(coinId, StoredCoinType.WATCHLIST))

}