package sk.dominikjezik.cryptolit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sk.dominikjezik.cryptolit.models.ExchangeRate
import sk.dominikjezik.cryptolit.repositories.CoinsRepository
import sk.dominikjezik.cryptolit.utilities.Response
import sk.dominikjezik.cryptolit.utilities.handleIfNotSuccessful
import sk.dominikjezik.cryptolit.utilities.handleNetworkCall
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val coinsRepository: CoinsRepository
) : ViewModel() {
    // dodatočný atribút, kvoli dočasnému offline stavu
    private var _exchangeRates: Map<String, ExchangeRate> = mapOf()
    private val _response = MutableLiveData<Response<Map<String, ExchangeRate>>>()
    val response: LiveData<Response<Map<String, ExchangeRate>>> = _response

    var selectedFromExchangeRate: String = "eur"
    var selectedToExchangeRate: String = "btc"
    private var fromPrice = ""

    private val _displayToPrice = MutableLiveData<String>()
    val displayToPrice: LiveData<String> = _displayToPrice


    /**
     * Stiahne kurzy z API pomocou repozitára, pričom ošetrí chybové stavy.
     */
    fun fetchExchangeRates() = viewModelScope.launch {
        _response.postValue(Response.Waiting())

        handleNetworkCall(_response) {
            val rates = coinsRepository.getExchangeRates()

            if (!handleIfNotSuccessful(rates, _response)) {
                return@handleNetworkCall
            }

            _response.postValue(Response.Success(rates.body()!!.rates))
            _exchangeRates = rates.body()!!.rates
        }

    }


    /**
     * Metóda je volaná pri zmene vybranej meny z fragmentu.
     * Zmení vybranú "from" menu na menu z parametra.
     * Prepočíta cenu.
     *
     * @param id
     */
    fun changeFromExchangeRate(id: String) {
        this.selectedFromExchangeRate = id.lowercase()
        this.recalculatePrice()
    }


    /**
     * Metóda je volaná pri zmene vybranej meny z fragmentu.
     * Zmení vybranú "to" menu na menu z parametra.
     * Prepočíta cenu.
     *
     * @param id
     */
    fun changeToExchangeRate(id: String) {
        this.selectedToExchangeRate = id.lowercase()
        this.recalculatePrice()
    }


    /**
     * Metóda je volaná priamo z xml, keď používateľ zmení zadanú cenu z políčka "from" price.
     * Parameter newText je text zadaný používateľom. Ostatné parametre nie sú využívané,
     * ale kvôli správnemu fungovaniu data bindingu tu musia byť uvedené.
     *
     * @param newText text zadaný používateľom
     * @param start
     * @param before
     * @param count
     */
    fun onUserFromInputTextChanged(newText: CharSequence, start: Int, before: Int, count: Int) {
        this.fromPrice = newText.toString()
        this.recalculatePrice()
    }


    /**
     * Vykoná prepočet podľa zadanej ceny "from" a podľa mien "from" a "to".
     * Skontroluje či zadaná cena nie je prázdny reťazec alebo len bodka.
     * Skontroluje existenciu meny v zozname všetkých mien. Prepočet
     * vykoná ako:
     *    zadaná cena * (cena cieľovej meny v hodnote bitcoin) / (cena aktuálnej meny v hodnote bitcoin)
     * Výsledok naformátuje a zobrazí používateľovi.
     */
    private fun recalculatePrice() {
        if (fromPrice.isEmpty() || fromPrice == ".") {
            _displayToPrice.postValue("")
            return
        }

        if (
            !_exchangeRates.containsKey(selectedFromExchangeRate) ||
            !_exchangeRates.containsKey(selectedToExchangeRate)
        ) {
            return
        }

        val exchangeRateFrom = _exchangeRates[selectedFromExchangeRate]!!
        val exchangeRateTo = _exchangeRates[selectedToExchangeRate]!!

        val newPrice = this.fromPrice.toFloat() * exchangeRateTo.value / exchangeRateFrom.value

        val decimalFormat = DecimalFormat("#.########")
        _displayToPrice.postValue(decimalFormat.format(newPrice))
    }

}