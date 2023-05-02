package sk.dominikjezik.cryptolit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import sk.dominikjezik.cryptolit.viewmodels.CoinDetailsViewModel
import sk.dominikjezik.cryptolit.databinding.FragmentCoinDetailsBinding
import sk.dominikjezik.cryptolit.models.Coin

@AndroidEntryPoint
class CoinDetailsFragment : Fragment() {

    private var _binding: FragmentCoinDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel : CoinDetailsViewModel by viewModels();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val coinId = arguments?.getString("coin_id")!!

        // asi if ak nie je uz nacitany
        viewModel.fetchCoin(coinId)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        return root;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}