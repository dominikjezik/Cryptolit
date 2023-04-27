package sk.dominikjezik.cryptolit.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface CoinGeckoService {

    @GET("ping")
    suspend fun ping(): ResponseBody

}