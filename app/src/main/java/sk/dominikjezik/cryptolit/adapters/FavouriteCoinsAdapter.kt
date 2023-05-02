package sk.dominikjezik.cryptolit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sk.dominikjezik.cryptolit.R
import sk.dominikjezik.cryptolit.databinding.ItemFavouriteCoinBinding
import sk.dominikjezik.cryptolit.models.Coin

class FavouriteCoinsAdapter(
    private val items: List<Coin>,
    private val onItemClickListener: ((Coin) -> Unit)? = null
) :
    RecyclerView.Adapter<FavouriteCoinsAdapter.FavouriteCoinViewHolder>() {

    class FavouriteCoinViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemFavouriteCoinBinding

        init {
            binding = ItemFavouriteCoinBinding.bind(view)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteCoinViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favourite_coin, parent, false)

        return FavouriteCoinViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: FavouriteCoinViewHolder, position: Int) {
        val coin = items[position]

        viewHolder.binding.materialCardView.setOnClickListener {
            onItemClickListener?.let { it -> it(coin) }
        }

        viewHolder.binding.coin = coin
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = items.size

}