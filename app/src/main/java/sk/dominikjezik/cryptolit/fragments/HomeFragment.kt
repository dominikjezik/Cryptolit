package sk.dominikjezik.cryptolit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import sk.dominikjezik.cryptolit.adapters.FavouriteCoinsAdapter
import sk.dominikjezik.cryptolit.databinding.FragmentHomeBinding
import sk.dominikjezik.cryptolit.viewmodels.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvFavouriteCoins.adapter = FavouriteCoinsAdapter(listOf("test1", "test2", "test3", "test4", "test5", "test6"));

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}