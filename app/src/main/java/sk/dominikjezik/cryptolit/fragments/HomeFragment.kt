package sk.dominikjezik.cryptolit.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
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

        binding.rvFavouriteCoins.adapter = FavouriteCoinsAdapter(listOf("test1", "test2", "test3", "test4", "test5", "test6"));
        binding.rvCoinsList.adapter = CoinsAdapter(listOf("test1", "test2", "test3", "test4", "test5", "test6", "test1", "test2", "test3", "test4", "test5", "test6"));

        binding.txtAppName.setOnClickListener {
            viewModel.ping()
        }

        viewModel.favouriteCoins.observe(viewLifecycleOwner) {
            Log.d("RESPONSE FRAGMENT", it.data.toString())
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}