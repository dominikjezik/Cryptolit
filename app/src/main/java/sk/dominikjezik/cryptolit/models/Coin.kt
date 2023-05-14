package sk.dominikjezik.cryptolit.models

import java.io.Serializable
import java.text.DecimalFormat

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
    fun currentPrice(): String {
        val decimalFormat = DecimalFormat("#.##########")
        val decimalPlaces = if (current_price < 1) 8 else if(current_price < 10) 4 else 2
        decimalFormat.maximumFractionDigits = decimalPlaces
        return decimalFormat.format(current_price)
    }
}