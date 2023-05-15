package sk.dominikjezik.cryptolit.utilities

/**
 * Enum reprezentuje chyby, ktoré môžu nastať
 * pri sieťovej komunikácií s API.
 */
enum class ResponseError {
    GENERAL_ERROR,
    NO_INTERNET_CONNECTION,
    TOO_MANY_REQUESTS
}