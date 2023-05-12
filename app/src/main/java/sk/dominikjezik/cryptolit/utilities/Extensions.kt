package sk.dominikjezik.cryptolit.utilities

import android.os.Build
import android.os.Bundle
import java.io.Serializable

/**
 * Kedže getSerializableExtra(String) je deprecated a odporúča sa použiť jej druhá verzia
 * ale táto verzia je dostupná až od Android 33, preto takáto pomocná metóda, ktorá použije správnu verziu
 * metódy podľa aktuálneho Androidu.
 * Myšlienka bola použitá z: https://stackoverflow.com/questions/72571804/getserializableextra-and-getparcelableextra-deprecated-what-is-the-alternative
 *
 * @param T
 * @param activity
 * @param name
 * @param clazz
 * @return
 */
fun <T : Serializable?> Bundle?.getSerializableArg(name: String, clazz: Class<T>): T?
{
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        this!!.getSerializable(name, clazz)
    else
        this!!.getSerializable(name) as T?
}