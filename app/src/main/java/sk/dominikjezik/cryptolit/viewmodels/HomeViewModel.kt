package sk.dominikjezik.cryptolit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sk.dominikjezik.cryptolit.models.Coin
import sk.dominikjezik.cryptolit.repositories.CoinsRepository
import sk.dominikjezik.cryptolit.utilities.Response
import sk.dominikjezik.cryptolit.utilities.ResponseError.*
import sk.dominikjezik.cryptolit.utilities.handleIfNotSuccessful
import sk.dominikjezik.cryptolit.utilities.handleNetworkCall
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val coinsRepository: CoinsRepository
) : ViewModel() {
    private lateinit var _allCoins: MutableList<Coin>

    private val _coinsToDisplay = MutableLiveData<Response<List<Coin>>>()
    val coinsToDisplay: LiveData<Response<List<Coin>>> = _coinsToDisplay

    private val _favouriteCoins = MutableLiveData<List<Coin>>()
    val favouriteCoins: LiveData<List<Coin>> = _favouriteCoins

    var selectedAvailableCoins = MutableLiveData(true)


    /**
     * Pri vytvorení viewmodelu spustí metódu na získanie kryptomien z API.
     */
    init {
        fetchCoins()
    }


    /**
     * Metóda slúži na zmenu kryptomien, ktoré sú zobrazené v zozname kryptomien.
     * Ak je parameter value true zobrazí všetky dostupné kryptomeny.
     * Ak je parameter value false zobrazí kryptomeny ozanačené
     * ako watchlisted.
     *
     * @param value
     */
    fun toggleAvailableCoinsList(value: Boolean) {
        if (selectedAvailableCoins.value == value) {
            return
        }
        selectedAvailableCoins.postValue(value)

        displayCoinsInList(value)
    }


    
    fun fetchCoins() = viewModelScope.launch {
        _coinsToDisplay.postValue(Response.Waiting());

        handleNetworkCall(_coinsToDisplay) {
            val coins = coinsRepository.getCoins()

            if (!handleIfNotSuccessful(coins, _coinsToDisplay)) {
                return@handleNetworkCall
            }

            _allCoins = (coins.body() as MutableList<Coin>?)!!
            findMissingCoins()
            filterFavouriteCoins()
            _coinsToDisplay.postValue(Response.Success(_allCoins))
            displayCoinsInList(selectedAvailableCoins.value)
        }

    }

    private fun displayCoinsInList(displayAvailableCoins: Boolean? = null) {
        if (displayAvailableCoins == true) {
            this._coinsToDisplay.postValue(Response.Success(_allCoins))
        } else {
            viewModelScope.launch {
                filterWatchlistCoins()
            }
        }
    }

    private suspend fun findMissingCoins() {
        val storedCoins = coinsRepository.getStoredCoins()
        val coinsForDownload = storedCoins.filter { storedCoin ->
            !_allCoins.any { coin -> coin.id == storedCoin.coinId }
        }

        if (coinsForDownload.isEmpty()) {
            return
        }

        try {
            val downloadedCoins = this.coinsRepository.getCoins(coinsForDownload);

            if (downloadedCoins.isSuccessful) {
                this._allCoins.addAll(downloadedCoins.body()!!)
            }

        } catch (_: Exception) { }
    }

    private suspend fun filterFavouriteCoins() {
        val storedFavouriteCoins = coinsRepository.getStoredFavouriteCoins()
        val favouriteCoins = _allCoins.filter { coin ->
            storedFavouriteCoins.any { storedCoin -> storedCoin.coinId == coin.id }
        }
        this._favouriteCoins.postValue(favouriteCoins)
    }

    private suspend fun filterWatchlistCoins() {
        val storedWatchlistCoins = coinsRepository.getStoredWatchlistCoins()
        val watchlistCoins = _allCoins.filter { coin ->
            storedWatchlistCoins.any { storedCoin -> storedCoin.coinId == coin.id }
        }
        this._coinsToDisplay.postValue(Response.Success(watchlistCoins))
    }

}