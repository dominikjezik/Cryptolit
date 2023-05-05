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
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val coinsRepository: CoinsRepository
) : ViewModel() {
    private lateinit var _allCoins: List<Coin>
    private val _coinsToDisplay = MutableLiveData<Response<List<Coin>>>()
    private val _favouriteCoins = MutableLiveData<List<Coin>>()

    var selectedAvailableCoins = MutableLiveData(true)
    val coinsToDisplay: LiveData<Response<List<Coin>>> = _coinsToDisplay
    val favouriteCoins: LiveData<List<Coin>> = _favouriteCoins

    init {
        fetchCoins()
    }

    fun toggleAvailableCoinsList(value: Boolean) {
        if (selectedAvailableCoins.value == value) {
            return
        }
        selectedAvailableCoins.postValue(value)

        if (value) {
            this._coinsToDisplay.postValue(Response.Success(_allCoins))
        } else {
            viewModelScope.launch {
                filterWatchlistCoins()
            }
        }
    }

    fun fetchCoins() = viewModelScope.launch {
        _coinsToDisplay.postValue(Response.Waiting());

        val coins = coinsRepository.getCoins("")//getCoinInfo("bitcoin,ethereum,decentraland")

        if (coins.isSuccessful) {
            _allCoins = coins.body()!!
            filterFavouriteCoins()
            _coinsToDisplay.postValue(Response.Success(_allCoins))
        } else {
            _coinsToDisplay.postValue(Response.Error(coins.message()))
        }

    }

    private suspend fun filterFavouriteCoins() {
        // TODO: zabezpecit ze chybajuce coiny su dodatocne stiahnute zo servera
        val storedFavouriteCoins = coinsRepository.getStoredFavouriteCoins()
        val list = _allCoins.filter { coin ->
            storedFavouriteCoins.any { storedCoin -> storedCoin.coinId == coin.id }
        }
        this._favouriteCoins.postValue(list)
    }

    private suspend fun filterWatchlistCoins() {
        // TODO: zabezpecit ze chybajuce coiny su dodatocne stiahnute zo servera
        val storedWatchlistCoins = coinsRepository.getStoredWatchlistCoins()
        val list = _allCoins.filter { coin ->
            storedWatchlistCoins.any { storedCoin -> storedCoin.coinId == coin.id }
        }
        this._coinsToDisplay.postValue(Response.Success(list))
    }

}