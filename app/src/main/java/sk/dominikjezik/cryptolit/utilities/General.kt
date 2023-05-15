package sk.dominikjezik.cryptolit.utilities

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import sk.dominikjezik.cryptolit.R
import java.lang.Exception
import java.net.UnknownHostException

/**
 * Na základe erroroveho typu responsu vráti príslušnú chybovú správu.
 *
 * @param T
 * @param response
 * @param context
 * @return
 */
fun <T> getErrorMessage(response: Response.Error<T>, context: Context): String {
    return when (response.errorType) {
        ResponseError.TOO_MANY_REQUESTS -> context.getString(R.string.too_many_requests)
        ResponseError.NO_INTERNET_CONNECTION -> context.getString(R.string.no_internet_connection)
        else -> context.getString(R.string.an_error_occurred)
    }
}


/**
 * Na základe erroroveho typu responsu zobrazí Snackbar s chybovou správou.
 *
 * @param T
 * @param response
 * @param view kde sa má snackbar zobrazit
 * @param context
 * @param onClick akcia ktorá sa má spustiť pri stlačení tlačidla refresh
 */
fun <T> displayErrorSnackBar(response: Response.Error<T>, view: View, context: Context, onClick: () -> Unit) {
    Snackbar.make(view, getErrorMessage(response,context), Snackbar.LENGTH_INDEFINITE)
        .setAction("refresh") { onClick() }.show()
}


/**
 * Metóda slúži na ošetrenie sieťového volania (retrofit API call).
 * Ak nastane chyba pripojenia na server, nastaví chybu do
 * odpovede v parametri response.
 *
 * @param T
 * @param response
 * @param networkCall
 */
suspend fun <T> handleNetworkCall(response: MutableLiveData<Response<T>>, networkCall: (suspend () -> Unit)) {
    try {
        networkCall()
    } catch (e: UnknownHostException) {
        e.printStackTrace();
        response.postValue(Response.Error(ResponseError.NO_INTERNET_CONNECTION))
    } catch (e: Exception) {
        e.printStackTrace();
        response.postValue(Response.Error(ResponseError.GENERAL_ERROR))
    }
}


/**
 * Metóda slúži na ošetrenie odpovede zo servera. Ak volanie nebolo úspešné
 * nastaví príslušnú chybu do parametra response.
 *
 * @param T
 * @param S
 * @param retrofitResponse
 * @param response
 * @return volanie bolo úspešné (true/false)
 */
fun <T, S> handleIfNotSuccessful(retrofitResponse: retrofit2.Response<T>, response: MutableLiveData<Response<S>>): Boolean {
    if (!retrofitResponse.isSuccessful) {
        if (retrofitResponse.code() == 429) {
            response.postValue(Response.Error(ResponseError.TOO_MANY_REQUESTS))
        } else {
            response.postValue(Response.Error(ResponseError.GENERAL_ERROR))
        }
    }

    return retrofitResponse.isSuccessful;
}

