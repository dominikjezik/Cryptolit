package sk.dominikjezik.cryptolit.adapters

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import sk.dominikjezik.cryptolit.R

/**
 * Binding adaptér slúži na vloženie fotky, ktorá bola stiahnutá pomocou
 * knižnice Glide, pričom uri obrázka sa získa z atribútu imgUri.
 * Funkcia bola prebratá z google kurzu
 *
 * @param imgView
 * @param imgUrl
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri =
            imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo))
            .into(imgView)
    }
}