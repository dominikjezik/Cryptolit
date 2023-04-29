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
    private val _coins = MutableLiveData<Response<List<Coin>>>()
    private val _favouriteCoins = MutableLiveData<List<Coin>>()

    val coins: LiveData<Response<List<Coin>>> = _coins
    val favouriteCoins: LiveData<List<Coin>> = _favouriteCoins

    init {
        fetchCoins()
    }

    fun fetchCoins() = viewModelScope.launch {
        _coins.postValue(Response.Waiting());

        val coins = coinsRepository.getCoinInfo("")//getCoinInfo("bitcoin,ethereum,decentraland")

        if (coins.isSuccessful) {
            filterFavouriteCoins(coins.body()!!)
            _coins.postValue(Response.Success(coins.body()!!))
        } else {
            _coins.postValue(Response.Error(coins.message()))
        }

    }

    private fun filterFavouriteCoins(coins: List<Coin>) {
        val list = coins.filter { coin -> coin.id in listOf("bitcoin","ethereum","decentraland") }
        this._favouriteCoins.postValue(list)
    }

}