package sk.dominikjezik.cryptolit.utilities

// Koncept sealed tried bol prevzaty z tohoto clanku a je bezne pouzivany na riesenie sietovej komunikacie.
// Navyse je pridana trieda Waiting, ktora reprezentuje stav kedy cakame na odpoved zo servera.
// https://proandroiddev.com/modeling-retrofit-responses-with-sealed-classes-and-coroutines-9d6302077dfe
sealed class Response<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T): Response<T>(data)
    class Error<T>(message: String?): Response<T>(null, message)
    class Waiting<T> : Response<T>()
}


