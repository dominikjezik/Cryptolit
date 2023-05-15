package sk.dominikjezik.cryptolit.models

import androidx.room.Entity

/**
 * Trieda reprezentuje kryptomenu, ktorá bola uložená do databázy.
 *
 * @property coinId
 * @property type
 */
@Entity(
    tableName = "coins",
    primaryKeys = ["coinId", "type"]
)
data class StoredCoin(
    val coinId: String,
    val type: StoredCoinType
)