package sk.dominikjezik.cryptolit.models

data class ExchangeRate(
    val name: String,
    val unit: String,
    val value: Float,
    val type: String
)
