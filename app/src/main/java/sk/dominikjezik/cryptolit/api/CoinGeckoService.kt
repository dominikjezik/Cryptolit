package sk.dominikjezik.cryptolit.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import sk.dominikjezik.cryptolit.models.Coin
import sk.dominikjezik.cryptolit.models.CoinChartResponse
import sk.dominikjezik.cryptolit.models.ExchangeRatesResponse
import sk.dominikjezik.cryptolit.models.SearchResult

interface CoinGeckoService {

    /**
     * Slúži na získanie dostupných coinov. Ak je parameter ids prázdny string, potom stiahne
     * prvých 100 coinov podľa market cap rank. Inak je do ids možné vložiť čiarkami
     * oddelené id coinov. Parameter currency predstavuje menu v akej chceme
     * aby boli ceny kryptomeny vyjadrené.
     *
     * @param ids
     * @param currency
     * @return
     */
    @GET("coins/markets")
    suspend fun getCoins(
        @Query("ids") ids: String,
        @Query("vs_currency") currency: String
    ): Response<List<Coin>>


    /**
     * Získa ceny v časových okamihoch v zadanom intervale parametera period,
     * ktorý predstavuje dni intervalu. Parameter id je id coinu a currency
     * predstavuje menu v akej chceme.
     *
     * @param id
     * @param currency
     * @param period
     * @return
     */
    @GET("coins/{id}/market_chart")
    suspend fun getCoinChartData(
        @Path("id") id: String,
        @Query("vs_currency") currency: String,
        @Query("days") period: String
    ): Response<CoinChartResponse>


    /**
     * Metóda slúži na získanie vyhľadávaných coinov, podľa zadaného výrazu.
     *
     * @param query
     * @return
     */
    @GET("search")
    suspend fun search(@Query("query") query: String): Response<SearchResult>


    /**
     * Stiahne všetky menové kurzy, ktoré sú v jednotkách BTC (bitcoin).
     *
     * @return
     */
    @GET("exchange_rates")
    suspend fun getExchangeRates(): Response<ExchangeRatesResponse>

}