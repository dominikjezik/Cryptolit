package sk.dominikjezik.cryptolit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sk.dominikjezik.cryptolit.models.SearchedCoin
import sk.dominikjezik.cryptolit.repositories.CoinsRepository
import sk.dominikjezik.cryptolit.utilities.Response
import sk.dominikjezik.cryptolit.utilities.ResponseError
import sk.dominikjezik.cryptolit.utilities.handleIfNotSuccessful
import sk.dominikjezik.cryptolit.utilities.handleNetworkCall
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val coinsRepository: CoinsRepository
) : ViewModel() {
    private val _coinsResult = MutableLiveData<Response<List<SearchedCoin>>>()
    val coinsResult: LiveData<Response<List<SearchedCoin>>> = _coinsResult
    var searchQuery: String = ""

    /**
     * Metóda po spustení nastavý výsledky vyhľadávania na Waiting,
     * z repozitára pomocou API získa vyhľadávané výsledky na
     * základe parametra query a výsledky vloží do zoznamu
     * výsledkov ako Success. V prípade chyby nastaví
     * výsledky na error a nastaví chybovú správu.
     *
     * @param query hľadaný výraz
     */
    fun fetchResults(query: String) = viewModelScope.launch {
        searchQuery = query
        _coinsResult.postValue(Response.Waiting())

        handleNetworkCall(_coinsResult) {
            val coins = coinsRepository.getSearchResults(query)

            if (!handleIfNotSuccessful(coins, _coinsResult)) {
                return@handleNetworkCall
            }

            _coinsResult.postValue(Response.Success(coins.body()!!.coins))
        }

    }

    /**
     * Metóda vyčistí zoznam nájdených výsledkov.
     */
    fun clearResults() {
        searchQuery = ""
        _coinsResult.postValue(Response.Success(listOf()))
    }

}