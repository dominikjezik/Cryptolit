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
import sk.dominikjezik.cryptolit.utilities.handleIfNotSuccessful
import sk.dominikjezik.cryptolit.utilities.handleNetworkCall
import java.text.DecimalFormat
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
    private val decimalFormat = DecimalFormat("#.##########")

    private var _isFavourite = MutableLiveData(false)
    val isFavourite: LiveData<Boolean> = _isFavourite

    private var _isWatchlisted = MutableLiveData(false)
    val isWatchlisted: LiveData<Boolean> = _isWatchlisted

    private var _priceToDisplay = MutableLiveData("0")
    val priceToDisplay: LiveData<String> = _priceToDisplay

    private var _priceChangePercentage = MutableLiveData("0%")
    val priceChangePercentage: LiveData<String> = _priceChangePercentage

    private var _priceChangePercentageGrowth = MutableLiveData(true)
    val priceChangePercentageGrowth: LiveData<Boolean> = _priceChangePercentageGrowth


    /**
     * Stiahne vývoj cien v čase z API pomocou repozitára, pričom ošetrí chybové stavy.
     * Dáta uloží do príslušného live data, pričom tiež nastaví aktuálnu cenu a
     * percentuálnu zmenu za dané časové obdobie, ktoré sa vypočíta ako:
     *     (posledná cena / prvá cena - 1) * 100
     * Percentá naformátuje a nastavý atribút percentage growth podľa
     * toho či je precentuálna zmena kladná alebo záporná.
     */
    fun fetchCoinChartData() = viewModelScope.launch {
        _coinChartData.postValue(Response.Waiting())

        handleNetworkCall(_coinChartData) {
            val data = coinsRepository.getCoinChartData(coin.id, selectedPeriod)

            if (!handleIfNotSuccessful(data, _coinChartData)) {
                return@handleNetworkCall
            }

            _coinChartData.postValue(Response.Success(data.body()!!))
            displayPrice(data.body()!!.prices.last()[1])

            val percent = (data.body()!!.prices.last()[1] / data.body()!!.prices.first()[1] - 1) * 100
            _priceChangePercentageGrowth.postValue(percent>=0)
            _priceChangePercentage.postValue(String.format("%.2f %%", percent))
        }
    }


    /**
     * Metóda zistí či je v databáze uložený daný coinu a nastaví
     * príslušné live data, ktoré služia na zmenu ikoniek
     * na uloženie coinu do favourite alebo watchlist.
     */
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


    /**
     * Metóda zobrazí naformátovanú cenu podľa zadaného parametra.
     * Desatinné miesta volí na základe hodnoty ceny, kvôli
     * peknému formátovaniu.
     *
     * @param price
     */
    fun displayPrice(price: Float) {
        val decimalPlaces = if (price < 1) 8 else if(price < 10) 4 else 2
        decimalFormat.maximumFractionDigits = decimalPlaces
        this._priceToDisplay.postValue(decimalFormat.format(price))
    }


    /**
     * Metóda zobrazí predvolenú cenu (aktuálnu cenu).
     */
    fun displayDefaultPrice() {
        this._coinChartData.value?.let {
            displayPrice(it.data!!.prices.last()[1])
        }
    }


    /**
     * Zmení hodnotu zvoleného časového obdobia, pričom
     * stiahne z API nové dáta pre dané časové obdobie.
     *
     * @param period časové obdobie v dňoch
     */
    fun toggleSelectedPeriod(period: Int) {
        if (selectedPeriod == period.toString()) {
            return
        }
        selectedPeriod = period.toString()
        fetchCoinChartData()
    }


    /**
     * Metóda nastaví časové obdobie na maximálne možné
     * a stiahne nové dáta z API.
     */
    fun toggleSelectedPeriodToMax() {
        if (selectedPeriod == "max") {
            return
        }
        selectedPeriod = "max"
        fetchCoinChartData()
    }


    /**
     * Uloží/odstráni aktuálny coin do favourite coinov uložených v datábaze.
     */
    fun toggleFavouriteCoin() = viewModelScope.launch {
        if (isFavourite.value == true) {
            coinsRepository.deleteFavouriteCoin(coin.id)
            _isFavourite.postValue(false)
        } else {
            coinsRepository.insertFavouriteCoin(coin.id)
            _isFavourite.postValue(true)
        }
    }


    /**
     * Uloží/odstráni aktuálny coin do watchlist coinov uložených v datábaze.
     */
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