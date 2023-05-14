package sk.dominikjezik.cryptolit.models

data class ExchangeRatesResponse(
    val rates: Map<String, ExchangeRate>
)
