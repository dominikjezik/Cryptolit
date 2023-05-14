package sk.dominikjezik.cryptolit.models

import java.io.Serializable
import kotlin.math.roundToInt

data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
    val current_price: Float,
    val image: String,
    private val market_cap_rank: Int?
): Serializable {
    val marketCapRank: String?
        get() = if(market_cap_rank != null) "#$market_cap_rank" else null
}