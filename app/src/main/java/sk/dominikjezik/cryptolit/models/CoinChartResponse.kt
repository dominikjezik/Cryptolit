package sk.dominikjezik.cryptolit.models

/**
 * Trieda reprezentu odpoveď API volania na získanie
 * dát do grafu vývoja ceny kryptomeny.
 *
 * @property prices
 */
data class CoinChartResponse(
    val prices: List<List<Float>>
)
