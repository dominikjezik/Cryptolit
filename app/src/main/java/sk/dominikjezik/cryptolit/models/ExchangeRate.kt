package sk.dominikjezik.cryptolit.models

/**
 * Trieda reprezentu výmenný kurz dannej kryptomeny,
 * ktorý je vyjadrený v jednotkách BTC (bitcoin).
 *
 * @property name
 * @property unit
 * @property value
 * @property type
 */
data class ExchangeRate(
    val name: String,
    val unit: String,
    val value: Float,
    val type: String
)
