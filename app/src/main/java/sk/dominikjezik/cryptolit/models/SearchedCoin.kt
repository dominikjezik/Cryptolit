package sk.dominikjezik.cryptolit.models

import java.io.Serializable

/**
 * Trieda reprezentuje vyhľadávanú kryptomenu, ktorá prišla
 * ako výsledok API volania search. Do atribútu large sa
 * ukladá URL obrázka danej kryptomeny.
 */
data class SearchedCoin(
    val id: String,
    val symbol: String,
    val name: String,
    val large: String,
    val market_cap_rank: Int?
): Serializable {
    val marketCapRank: String?
        get() = if(market_cap_rank != null) "#$market_cap_rank" else null
}
