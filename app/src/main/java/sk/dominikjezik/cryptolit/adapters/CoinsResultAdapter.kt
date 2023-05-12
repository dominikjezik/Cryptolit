package sk.dominikjezik.cryptolit.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sk.dominikjezik.cryptolit.R
import sk.dominikjezik.cryptolit.databinding.ItemCoinResultBinding
import sk.dominikjezik.cryptolit.models.SearchedCoin

class CoinsResultAdapter(
    private val onItemClickListener: ((SearchedCoin) -> Unit)? = null
) : RecyclerView.Adapter<CoinsResultAdapter.CoinViewHolder>() {

    private var items: List<SearchedCoin> = listOf()

    class CoinViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemCoinResultBinding

        init {
            binding = ItemCoinResultBinding.bind(view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinsResultAdapter.CoinViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coin_result, parent, false)

        return CoinViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: CoinsResultAdapter.CoinViewHolder, position: Int) {
        val coin = items[position]

        viewHolder.binding.constraintLayout.setOnClickListener {
            onItemClickListener?.let { it -> it(coin) }
        }

        viewHolder.binding.coin = coin
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setCoinsResult(items: List<SearchedCoin>) {
        this.items = items;
        notifyDataSetChanged()
    }

    fun clearCoinsResult() {
        this.setCoinsResult(listOf())
    }

}