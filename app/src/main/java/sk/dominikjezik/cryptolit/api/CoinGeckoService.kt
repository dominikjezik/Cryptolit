package sk.dominikjezik.cryptolit.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import sk.dominikjezik.cryptolit.models.Coin

interface CoinGeckoService {

    @GET("ping")
    suspend fun ping(): ResponseBody

    @GET("coins/markets")
    suspend fun getCoinInfo(@Query("ids") ids: String, @Query("vs_currency") currency: String): Response<List<Coin>>

}