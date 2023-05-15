package sk.dominikjezik.cryptolit.models

/**
 * Trieda reprezentu odpoveď API volania na získanie vyhľadávaných výsledkov.
 *
 * @property coins
 */
data class SearchResult(
    val coins: List<SearchedCoin>
)
