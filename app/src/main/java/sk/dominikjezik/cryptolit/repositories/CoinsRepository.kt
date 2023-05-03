package sk.dominikjezik.cryptolit.repositories

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import sk.dominikjezik.cryptolit.api.CoinGeckoService
import sk.dominikjezik.cryptolit.models.CoinChartResponse
import javax.inject.Inject

class CoinsRepository @Inject constructor(
    private val coinGeckoService: CoinGeckoService
){

    suspend fun getCoins(id: String) = coinGeckoService.getCoins(id, "eur")

    suspend fun getCoinInfo(id: String) = coinGeckoService.getCoinInfo(id)

    suspend fun getCoinChartData(id: String, days: Int ) = coinGeckoService.getCoinChartData(id, "eur", days)

}