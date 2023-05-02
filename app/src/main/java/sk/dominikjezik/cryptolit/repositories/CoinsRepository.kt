package sk.dominikjezik.cryptolit.repositories

import sk.dominikjezik.cryptolit.api.CoinGeckoService
import javax.inject.Inject

class CoinsRepository @Inject constructor(
    private val coinGeckoService: CoinGeckoService
){

    suspend fun getCoins(id: String) = coinGeckoService.getCoins(id, "eur")

    suspend fun getCoinInfo(id: String) = coinGeckoService.getCoinInfo(id)


}