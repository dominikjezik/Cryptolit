package sk.dominikjezik.cryptolit.data

import androidx.room.Database
import androidx.room.RoomDatabase
import sk.dominikjezik.cryptolit.models.StoredCoin

@Database(
    entities = [StoredCoin::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun coinDAO(): CoinDAO
}