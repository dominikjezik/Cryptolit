package sk.dominikjezik.cryptolit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sk.dominikjezik.cryptolit.R
import sk.dominikjezik.cryptolit.databinding.ItemCoinBinding
import sk.dominikjezik.cryptolit.models.Coin

class CoinsAdapter(private val items: List<Coin>) :
    RecyclerView.Adapter<CoinsAdapter.CoinViewHolder>() {

    class CoinViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemCoinBinding

        init {
            binding = ItemCoinBinding.bind(view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinsAdapter.CoinViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coin, parent, false)

        return CoinViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: CoinsAdapter.CoinViewHolder, position: Int) {
        viewHolder.binding.coin = items[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = items.size

}