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
import dagger.hilt.android.AndroidEntryPoint
import sk.dominikjezik.cryptolit.R
import sk.dominikjezik.cryptolit.adapters.CoinsAdapter
import sk.dominikjezik.cryptolit.adapters.FavouriteCoinsAdapter
import sk.dominikjezik.cryptolit.databinding.FragmentHomeBinding
import sk.dominikjezik.cryptolit.models.Coin
import sk.dominikjezik.cryptolit.utilities.Response
import sk.dominikjezik.cryptolit.utilities.displayErrorSnackBar
import sk.dominikjezik.cryptolit.viewmodels.HomeViewModel

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel : HomeViewModel by viewModels();

    /**
     * Metóda nastaví data binding, nastaví SwipeRefreshLayout,
     * zobrazí načítavanie a nastaví observers.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Nastavenie data bindingu
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        // Nastavenie farby indikátora čakania na odpoveď z API v SwipeRefreshLayoute
        binding.swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.indigo_500));

        // Nastavenie akcie pri obnovení zoznamu v SwipeRefreshLayoute
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchCoins()
        }

        // Zobraz indikátor načítavania
        this.displayLoading()

        // Nastavenie observerov
        this.setupObservers()

        return root
    }


    /**
     * Metóda nastaví observers. Prvý observer získa kryptomeny na zobrazenie v hlavnom zozname
     * a podľa typu odpovede (response) vykoná príslušné akcie. Pri úspešnej odpovedi zobrazí
     * kryptomeny do zoznamu. Ak odpoveď už prišla (nečakáme na odpoveď) tak zruší indikátor
     * v SwipeRefresh layout. Ak je odpoveď chybná skryje indikátor a zobrazí chybovú
     * správu s možnosťou refreshu. Druhý observer zobrazí obľúbené coiny ak je ich
     * počet viac ako 0 do zoznamu.
     *
     */
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
                displayErrorSnackBar(response, binding.root, requireContext(), viewModel::fetchCoins)
            }
        }

        viewModel.favouriteCoins.observe(viewLifecycleOwner) { coins ->
            if (coins.isEmpty()) {
                this.hideFavouriteCoins()
            } else {
                this.displayFavouriteCoins()
                binding.rvFavouriteCoins.adapter = FavouriteCoinsAdapter(coins) { coin ->
                    onItemClickListener(coin)
                }
            }

        }
    }


    /**
     * Metóda sa spustí po kliknutí na položku v zozname kryptomien alebo
     * v zozname obľúbených kryptomien. Parameter coin obsahuje kliknutú
     * kryptomenu. Metóda zobrazí fragment s detailami o príslušnej
     * kryptomene.
     *
     * @param coin
     */
    private fun onItemClickListener(coin: Coin) {
        val bundle = bundleOf("coin" to coin)
        findNavController().navigate(R.id.action_navigation_home_to_coinDetailsFragment, bundle)
    }


    /**
     * Zobrazí indikátor čakania na odpoveď z API a skryje obľúbené kryptomeny.
     */
    private fun displayLoading() {
        binding.txtFavouriteCoins.visibility = View.GONE
        binding.llSubHeader.visibility = View.GONE
        binding.cpiFavouriteCoinLoadingIndicator.visibility = View.VISIBLE
    }


    /**
     * Skryje indikátor čakania na odpoveď z API.
     */
    private fun hideLoading() {
        binding.cpiFavouriteCoinLoadingIndicator.visibility = View.GONE
    }


    /**
     * Zobrazí zoznam kryptomien spolu s titulkom.
     */
    private fun displayCoinsList() {
        binding.cpiFavouriteCoinLoadingIndicator.visibility = View.GONE
        binding.llSubHeader.visibility = View.VISIBLE
    }


    /**
     * Zobrazí zoznam obľúbených kryptomien spolu s titulkom.
     */
    private fun displayFavouriteCoins() {
        binding.txtFavouriteCoins.visibility = View.VISIBLE
        binding.rvFavouriteCoins.visibility = View.VISIBLE
    }


    /**
     * Skryje zoznam obľúbených kryptomien spolu s titulkom.
     */
    private fun hideFavouriteCoins() {
        binding.txtFavouriteCoins.visibility = View.GONE
        binding.rvFavouriteCoins.visibility = View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}