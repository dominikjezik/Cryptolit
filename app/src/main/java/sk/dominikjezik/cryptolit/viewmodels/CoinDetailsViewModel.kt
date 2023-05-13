package sk.dominikjezik.cryptolit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sk.dominikjezik.cryptolit.models.Coin
import sk.dominikjezik.cryptolit.models.CoinChartResponse
import sk.dominikjezik.cryptolit.models.StoredCoinType
import sk.dominikjezik.cryptolit.repositories.CoinsRepository
import sk.dominikjezik.cryptolit.utilities.Response
import javax.inject.Inject

@HiltViewModel
class CoinDetailsViewModel @Inject constructor(
    private val coinsRepository: CoinsRepository
)  : ViewModel() {
    private val _coinChartData = MutableLiveData<Response<CoinChartResponse>>()
    val coinChartData: LiveData<Response<CoinChartResponse>> = _coinChartData

    private lateinit var _coin: Coin
    var coin: Coin
        get() = _coin
        set(value) {
            _coin = value
            fetchCoinChartData()
            fetchCoinType()
        }

    private var selectedPeriod = "1"

    private var _isFavourite = MutableLiveData(false)
    val isFavourite: LiveData<Boolean> = _isFavourite

    private var _isWatchlisted = MutableLiveData(false)
    val isWatchlisted: LiveData<Boolean> = _isWatchlisted

    private var _priceToDisplay = MutableLiveData(0f)
    val priceToDisplay: LiveData<Float> = _priceToDisplay

    private fun fetchCoinChartData() = viewModelScope.launch {
        _coinChartData.postValue(Response.Waiting())

        val data = coinsRepository.getCoinChartData(coin.id, selectedPeriod)

        if (data.isSuccessful) {
            _coinChartData.postValue(Response.Success(data.body()!!))
            _priceToDisplay.postValue(data.body()!!.prices.last()[1])
        } else {
            _coinChartData.postValue(Response.Error(data.message()))
        }
    }

    private fun fetchCoinType() = viewModelScope.launch {
        val storedCoin = coinsRepository.findCoinById(coin.id)

        storedCoin.forEach {
            if (it.type == StoredCoinType.FAVOURITE) {
                _isFavourite.postValue(true)
            } else if (it.type == StoredCoinType.WATCHLIST) {
                _isWatchlisted.postValue(true)
            }
        }
    }

    fun toggleSelectedPeriod(period: Int) {
        if (selectedPeriod == period.toString()) {
            return
        }
        selectedPeriod = period.toString()
        fetchCoinChartData()
    }

    fun toggleSelectedPeriodToMax() {
        if (selectedPeriod == "max") {
            return
        }
        selectedPeriod = "max"
        fetchCoinChartData()
    }

    fun toggleFavouriteCoin() = viewModelScope.launch {
        if (isFavourite.value == true) {
            coinsRepository.deleteFavouriteCoin(coin.id)
            _isFavourite.postValue(false)
        } else {
            coinsRepository.insertFavouriteCoin(coin.id)
            _isFavourite.postValue(true)
        }
    }

    fun toggleWatchlistCoin() = viewModelScope.launch {
        if (isWatchlisted.value == true) {
            coinsRepository.deleteWatchlistCoin(coin.id)
            _isWatchlisted.postValue(false)
        } else {
            coinsRepository.insertWatchlistCoin(coin.id)
            _isWatchlisted.postValue(true)
        }
    }

}