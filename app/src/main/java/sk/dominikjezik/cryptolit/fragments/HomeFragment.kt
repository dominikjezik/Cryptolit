package sk.dominikjezik.cryptolit.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import sk.dominikjezik.cryptolit.adapters.CoinsAdapter
import sk.dominikjezik.cryptolit.adapters.FavouriteCoinsAdapter
import sk.dominikjezik.cryptolit.databinding.FragmentHomeBinding
import sk.dominikjezik.cryptolit.viewmodels.HomeViewModel

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel : HomeViewModel by viewModels();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        this.displayLoading()

        viewModel.coins.observe(viewLifecycleOwner) {
            it.data?.let {
                this.displayCoinsList()
                binding.rvCoinsList.adapter = CoinsAdapter(it)
            }
        }

        viewModel.favouriteCoins.observe(viewLifecycleOwner) {
            this.displayFavouriteCoins()
            binding.rvFavouriteCoins.adapter = FavouriteCoinsAdapter(it)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayLoading() {
        binding.txtFavouriteCoins.visibility = View.GONE
        binding.llSubHeader.visibility = View.GONE
        binding.cpiFavouriteCoinLoadingIndicator.visibility = View.VISIBLE
    }

    private fun displayCoinsList() {
        binding.cpiFavouriteCoinLoadingIndicator.visibility = View.GONE
        binding.llSubHeader.visibility = View.VISIBLE
    }

    private fun displayFavouriteCoins() {
        binding.txtFavouriteCoins.visibility = View.VISIBLE
    }
}