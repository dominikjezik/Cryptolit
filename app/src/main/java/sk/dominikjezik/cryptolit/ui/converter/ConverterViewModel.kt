package sk.dominikjezik.cryptolit.ui.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConverterViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is converter Fragment"
    }
    val text: LiveData<String> = _text
}