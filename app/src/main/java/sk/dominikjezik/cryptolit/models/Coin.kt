package sk.dominikjezik.cryptolit.models

data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
    val current_price: Float,
    val image: String
)