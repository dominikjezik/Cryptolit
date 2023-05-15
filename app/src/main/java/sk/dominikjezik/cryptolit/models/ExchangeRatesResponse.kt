package sk.dominikjezik.cryptolit.models

/**
 * Trieda reprezentu odpoveď API volania na získanie výmenných kurzov.
 *
 * @property rates
 */
data class ExchangeRatesResponse(
    val rates: Map<String, ExchangeRate>
)
