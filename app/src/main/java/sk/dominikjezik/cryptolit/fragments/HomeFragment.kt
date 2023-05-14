package sk.dominikjezik.cryptolit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import sk.dominikjezik.cryptolit.R
import sk.dominikjezik.cryptolit.adapters.CoinsAdapter
import sk.dominikjezik.cryptolit.adapters.FavouriteCoinsAdapter
import sk.dominikjezik.cryptolit.databinding.FragmentHomeBinding
import sk.dominikjezik.cryptolit.models.Coin
import sk.dominikjezik.cryptolit.utilities.Response
import sk.dominikjezik.cryptolit.utilities.ResponseError
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

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        binding.swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.indigo_500));
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchCoins()
        }

        this.displayLoading()
        this.setupObservers()

        return root
    }

    private fun setupObservers() {
        viewModel.coinsToDisplay.observe(viewLifecycleOwner) { response ->
            response.data?.let {
                this.displayCoinsList()
                binding.rvCoinsList.adapter = CoinsAdapter(it) { coin ->
                    onItemClickListener(coin)
                }
            }
            if (response !is Response.Waiting) {
                binding.swipeRefreshLayout.isRefreshing = false;
            }

            if (response is Response.Error) {
                hideLoading()
                val msg = when (response.errorType) {
                    ResponseError.TOO_MANY_REQUESTS -> getString(R.string.too_many_requests)
                    ResponseError.NO_INTERNET_CONNECTION -> getString(R.string.no_internet_connection)
                    else -> getString(R.string.an_error_occurred)
                }
                Snackbar.make(binding.root, msg, Snackbar.LENGTH_INDEFINITE)
                    .setAction("refresh") { viewModel.fetchCoins() }.show()
            }
        }

        viewModel.favouriteCoins.observe(viewLifecycleOwner) { coins ->
            this.displayFavouriteCoins()
            binding.rvFavouriteCoins.adapter = FavouriteCoinsAdapter(coins) { coin ->
                onItemClickListener(coin)
            }
        }
    }

    private fun onItemClickListener(coin: Coin) {
        val bundle = bundleOf("coin" to coin)
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

    private fun hideLoading() {
        binding.cpiFavouriteCoinLoadingIndicator.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}