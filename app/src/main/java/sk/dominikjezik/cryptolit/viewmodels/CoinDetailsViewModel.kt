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
class CoinDetailsViewModel @Inject constructor(
    private val coinsRepository: CoinsRepository
)  : ViewModel() {
    private val _coin = MutableLiveData<Response<Coin>>()

    val coin: LiveData<Response<Coin>> = _coin

    fun fetchCoin(coinId: String) = viewModelScope.launch {
        _coin.postValue(Response.Waiting());

        val coin = coinsRepository.getCoinInfo(coinId)

        if (coin.isSuccessful) {
            _coin.postValue(Response.Success(coin.body()!!))
        } else {
            _coin.postValue(Response.Error(coin.message()))
        }

    }

}