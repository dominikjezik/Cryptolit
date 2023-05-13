package sk.dominikjezik.cryptolit.models

import java.io.Serializable
import kotlin.math.roundToInt

data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
    val current_price: Float,
    val image: String,
    private val market_cap_rank: Int?,
    val price_change_percentage_24h: Float
): Serializable {
    val marketCapRank: String?
        get() = if(market_cap_rank != null) "#$market_cap_rank" else null

    val priceChangePercentage24h: String
        get() = String.format("%.2f %%", price_change_percentage_24h)

    val priceChangePercentage24hGrowth: Boolean
        get() = (price_change_percentage_24h * 100.0).roundToInt() / 100.0 >= 0
}