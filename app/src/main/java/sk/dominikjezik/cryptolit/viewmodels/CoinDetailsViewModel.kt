package sk.dominikjezik.cryptolit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sk.dominikjezik.cryptolit.models.Coin
import sk.dominikjezik.cryptolit.models.CoinChartResponse
import sk.dominikjezik.cryptolit.repositories.CoinsRepository
import sk.dominikjezik.cryptolit.utilities.Response
import javax.inject.Inject

@HiltViewModel
class CoinDetailsViewModel @Inject constructor(
    private val coinsRepository: CoinsRepository
)  : ViewModel() {
    private val _coinChartData = MutableLiveData<Response<CoinChartResponse>>()

    lateinit var coin: Coin
    val coinChartData: LiveData<Response<CoinChartResponse>> = _coinChartData

    fun fetchCoinChartData() = viewModelScope.launch {
        _coinChartData.postValue(Response.Waiting())

        val data = coinsRepository.getCoinChartData(coin.id, 1)

        if (data.isSuccessful) {
            _coinChartData.postValue(Response.Success(data.body()!!))
        } else {
            _coinChartData.postValue(Response.Error(data.message()))
        }

    }

}