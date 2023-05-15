package sk.dominikjezik.cryptolit.utilities

/**
 * Koncept sealed tried bol prevzaty z tohoto clanku a je bezne pouzivany na riesenie sietovej komunikacie.
 * Navyse je pridana trieda Waiting, ktora reprezentuje stav kedy cakame na odpoved zo servera a Error
 * preberá ako parameter typ chyby, ktorá nastala.
 *
 * @param T
 * @property data
 * @property errorType
 */
// https://proandroiddev.com/modeling-retrofit-responses-with-sealed-classes-and-coroutines-9d6302077dfe
sealed class Response<T>(
    val data: T? = null,
    val errorType: ResponseError? = null
) {
    class Success<T>(data: T): Response<T>(data)
    class Error<T>(type: ResponseError): Response<T>(null, type)
    class Waiting<T> : Response<T>()
}
