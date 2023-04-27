package sk.dominikjezik.cryptolit.repositories

import sk.dominikjezik.cryptolit.api.CoinGeckoService
import javax.inject.Inject

class CoinsRepository @Inject constructor(
    private val coinGeckoService: CoinGeckoService
){

    suspend fun ping() = coinGeckoService.ping();

}