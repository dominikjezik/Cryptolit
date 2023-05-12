package sk.dominikjezik.cryptolit.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sk.dominikjezik.cryptolit.R
import sk.dominikjezik.cryptolit.databinding.ItemCoinResultBinding
import sk.dominikjezik.cryptolit.models.SearchedCoin

/**
 * Trieda slúži ako adaptér pre RecyclerView používaný
 * v SearchFragment na zobrazenie výsledkov
 * vyhľadávania.
 */
class CoinsResultAdapter(
    private val onItemClickListener: ((SearchedCoin) -> Unit)? = null
) : RecyclerView.Adapter<CoinsResultAdapter.CoinViewHolder>() {

    private var items: List<SearchedCoin> = listOf()

    /**
     * ViewHolder pre položku výsledku kryptomeny.
     */
    class CoinViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemCoinResultBinding

        init {
            binding = ItemCoinResultBinding.bind(view)
        }
    }

    /**
     * Vytvára nový ViewHolder pre každú položku.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinsResultAdapter.CoinViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coin_result, parent, false)

        return CoinViewHolder(view)
    }

    /**
     * Nastavuje obsah každej položky pre príslušnú kryptomenu.
     */
    override fun onBindViewHolder(viewHolder: CoinsResultAdapter.CoinViewHolder, position: Int) {
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

    /**
     * Nastaví zoznam výsledkov vyhľadávania kryptomien.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setCoinsResult(items: List<SearchedCoin>) {
        this.items = items;
        notifyDataSetChanged()
    }

}