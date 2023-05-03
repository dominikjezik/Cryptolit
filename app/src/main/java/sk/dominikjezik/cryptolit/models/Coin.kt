package sk.dominikjezik.cryptolit.models

import java.io.Serializable

data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
    val current_price: Float,
    val image: String
): Serializable