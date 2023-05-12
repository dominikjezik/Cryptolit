package sk.dominikjezik.cryptolit.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import sk.dominikjezik.cryptolit.models.Coin
import sk.dominikjezik.cryptolit.models.CoinChartResponse
import sk.dominikjezik.cryptolit.models.SearchResult

interface CoinGeckoService {

    @GET("ping")
    suspend fun ping(): ResponseBody

    @GET("coins/markets")
    suspend fun getCoins(
        @Query("ids") ids: String,
        @Query("vs_currency") currency: String
    ): Response<List<Coin>>

    @GET("coins/markets")
    suspend fun getCoinInfo(@Query("ids") ids: String): Response<Coin>

    @GET("coins/{id}/market_chart")
    suspend fun getCoinChartData(
        @Path("id") id: String,
        @Query("vs_currency") currency: String,
        @Query("days") days: Int
    ): Response<CoinChartResponse>

    @GET("search")
    suspend fun search(@Query("query") query: String): Response<SearchResult>

}