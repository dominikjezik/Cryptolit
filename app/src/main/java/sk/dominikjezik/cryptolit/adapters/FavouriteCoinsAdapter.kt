package sk.dominikjezik.cryptolit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sk.dominikjezik.cryptolit.R
import sk.dominikjezik.cryptolit.databinding.FavouriteCoinItemBinding

class FavouriteCoinsAdapter(private val items: List<String>) :
    RecyclerView.Adapter<FavouriteCoinsAdapter.FavouriteCoinViewHolder>() {


    class FavouriteCoinViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: FavouriteCoinItemBinding

        init {
            binding = FavouriteCoinItemBinding.bind(view)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteCoinViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favourite_coin_item, parent, false)

        return FavouriteCoinViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: FavouriteCoinViewHolder, position: Int) {
        viewHolder.binding.txtTitle.text = items[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = items.size

}