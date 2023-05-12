package sk.dominikjezik.cryptolit.models

data class SearchedCoin(
    val id: String,
    val symbol: String,
    val name: String,
    val large: String,
    private val market_cap_rank: Int?
) {
    val marketCapRank: String?
        get() = if(market_cap_rank != null) "#$market_cap_rank" else null
}
