package sk.dominikjezik.cryptolit.models

import androidx.room.Entity

@Entity(
    tableName = "coins",
    primaryKeys = ["coinId", "type"]
)
data class StoredCoin(
    val coinId: String,
    val type: StoredCoinType
)