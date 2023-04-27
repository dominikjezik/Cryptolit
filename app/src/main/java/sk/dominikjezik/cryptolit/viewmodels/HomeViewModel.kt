package sk.dominikjezik.cryptolit.viewmodels

import android.util.Log
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

    private val _favouriteCoins = MutableLiveData<Response<List<Coin>>>()
    val favouriteCoins: LiveData<Response<List<Coin>>> = _favouriteCoins

    fun ping() = viewModelScope.launch {
        _favouriteCoins.postValue(Response.Waiting());

        val coins = coinsRepository.getCoinInfo("bitcoin,ethereum")

        if (coins.isSuccessful) {
            _favouriteCoins.postValue(Response.Success(coins.body()!!))
        } else {
            _favouriteCoins.postValue(Response.Error(coins.message()))
        }

    }

}