package sk.dominikjezik.cryptolit.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import sk.dominikjezik.cryptolit.R
import sk.dominikjezik.cryptolit.adapters.CoinsAdapter
import sk.dominikjezik.cryptolit.adapters.FavouriteCoinsAdapter
import sk.dominikjezik.cryptolit.databinding.FragmentHomeBinding
import sk.dominikjezik.cryptolit.models.Coin
import sk.dominikjezik.cryptolit.utilities.Response
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

        binding.swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.indigo_500));
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchCoins()
        }

       this.displayLoading()

        viewModel.coins.observe(viewLifecycleOwner) {
            it.data?.let {
                this.displayCoinsList()
                binding.rvCoinsList.adapter = CoinsAdapter(it)
            }
            if (it !is Response.Waiting) {
                binding.swipeRefreshLayout.isRefreshing = false;
            }
        }

        viewModel.favouriteCoins.observe(viewLifecycleOwner) { coins ->
            this.displayFavouriteCoins()
            binding.rvFavouriteCoins.adapter = FavouriteCoinsAdapter(coins) { coin ->
                onItemClickListener(coin)
                Log.d("TEST", "test")
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemClickListener(coin: Coin) {
        val bundle = bundleOf("coin_id" to coin.id)
        findNavController().navigate(R.id.action_navigation_home_to_coinDetailsFragment, bundle)
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