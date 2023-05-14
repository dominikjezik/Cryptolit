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
import sk.dominikjezik.cryptolit.utilities.ResponseError
import java.lang.Exception
import java.net.UnknownHostException
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

    private var selectedFromExchangeRate: String? = null
    private var selectedToExchangeRate: String? = null
    private var fromPrice = ""

    val displayToPrice = MutableLiveData<String>()

    fun fetchExchangeRates() = viewModelScope.launch {
        try {
            _response.postValue(Response.Waiting())

            val rates = coinsRepository.getExchangeRates()

            if (rates.isSuccessful) {
                _response.postValue(Response.Success(rates.body()!!.rates))
                _exchangeRates = rates.body()!!.rates
            } else {
                if (rates.code() == 429) {
                    _response.postValue(Response.Error(ResponseError.TOO_MANY_REQUESTS))
                } else {
                    _response.postValue(Response.Error(ResponseError.GENERAL_ERROR))
                }
            }
        } catch (e: UnknownHostException) {
            e.printStackTrace();
            _response.postValue(Response.Error(ResponseError.NO_INTERNET_CONNECTION))
        } catch (e: Exception) {
            e.printStackTrace();
            _response.postValue(Response.Error(ResponseError.GENERAL_ERROR))
        }
    }

    fun changeFromExchangeRate(id: String) {
        this.selectedFromExchangeRate = id.lowercase()
        this.recalculatePrice()
    }

    fun changeToExchangeRate(id: String) {
        this.selectedToExchangeRate = id.lowercase()
        this.recalculatePrice()
    }

    fun onUserFromInputTextChanged(newText: CharSequence, a: Int, b: Int, c: Int) {
        this.fromPrice = newText.toString()
        this.recalculatePrice()
    }

    private fun recalculatePrice() {
        if (fromPrice.isEmpty() || fromPrice == ".") {
            displayToPrice.postValue("")
            return
        }

        if (
            selectedFromExchangeRate == null ||
            selectedToExchangeRate == null
        ) {
            return
        }

        val exchangeRateFrom = _exchangeRates[selectedFromExchangeRate]!!
        val exchangeRateTo = _exchangeRates[selectedToExchangeRate]!!

        val newPrice = this.fromPrice.toFloat() * exchangeRateTo.value / exchangeRateFrom.value

        val decimalFormat = DecimalFormat("#.########")
        displayToPrice.postValue(decimalFormat.format(newPrice))
    }

}