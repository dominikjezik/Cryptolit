package sk.dominikjezik.cryptolit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sk.dominikjezik.cryptolit.R
import sk.dominikjezik.cryptolit.databinding.ItemCoinBinding
import sk.dominikjezik.cryptolit.models.Coin

/**
 * Trieda slúži ako adaptér pre RecyclerView používaný v HomeFragment
 * na zobrazenie dostupných/watchlist coinov.
 */
class CoinsAdapter(
    private val items: List<Coin>,
    private val onItemClickListener: ((Coin) -> Unit)? = null
) :
    RecyclerView.Adapter<CoinsAdapter.CoinViewHolder>() {

    /**
     * ViewHolder pre položku coinu.
     */
    class CoinViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemCoinBinding

        init {
            binding = ItemCoinBinding.bind(view)
        }
    }


    /**
     * Vytvára nový ViewHolder pre každú položku.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinsAdapter.CoinViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coin, parent, false)

        return CoinViewHolder(view)
    }


    /**
     * Nastavuje obsah každej položky pre príslušnú kryptomenu.
     */
    override fun onBindViewHolder(viewHolder: CoinsAdapter.CoinViewHolder, position: Int) {
        val coin = items[position]

        viewHolder.binding.constraintLayout.setOnClickListener {
            onItemClickListener?.let { it -> it(coin) }
        }

        viewHolder.binding.coin = coin
    }


    /**
     * Vráti počet položiek v zozname.
     */
    override fun getItemCount() = items.size

}