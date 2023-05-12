package sk.dominikjezik.cryptolit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding.widget.RxTextView
import dagger.hilt.android.AndroidEntryPoint
import sk.dominikjezik.cryptolit.R
import sk.dominikjezik.cryptolit.adapters.CoinsResultAdapter
import sk.dominikjezik.cryptolit.databinding.FragmentSearchBinding
import sk.dominikjezik.cryptolit.models.SearchedCoin
import sk.dominikjezik.cryptolit.utilities.Response
import sk.dominikjezik.cryptolit.viewmodels.SearchViewModel
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var coinsResultAdapter: CoinsResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        coinsResultAdapter = CoinsResultAdapter() { coin ->
            onItemClickListener(coin)
        }
        binding.rvResultsList.adapter = coinsResultAdapter

        RxTextView.textChanges(binding.txtFieldSearchInputEditText)
            .debounce(700, TimeUnit.MILLISECONDS)
            .subscribe { textChanged: CharSequence? ->
                if (textChanged?.length == 0) {
                    viewModel.clearResults()
                    return@subscribe
                }
                viewModel.fetchResults(textChanged.toString())
            }

        viewModel.coinsResult.observe(viewLifecycleOwner) { response ->
            if (response is Response.Waiting) {
                displayLoadingIndicator()
            }
            else if (response is Response.Error) {
                binding.cpiLoadingIndicator.visibility = View.GONE
                // TODO
            } else {
                response.data?.let {
                    coinsResultAdapter.setCoinsResult(it)
                    displayRecyclerViewWithResults()

                    if (it.isEmpty() && viewModel.searchQuery.isNotEmpty()) {
                        displayMessage("No results found")
                    }
                }
            }
        }

        return root
    }

    private fun displayRecyclerViewWithResults() {
        binding.cpiLoadingIndicator.visibility = View.GONE
        binding.rvResultsList.visibility = View.VISIBLE
        binding.txtMsg.visibility = View.GONE
    }

    private fun displayLoadingIndicator() {
        binding.cpiLoadingIndicator.visibility = View.VISIBLE
        binding.rvResultsList.visibility = View.GONE
        binding.txtMsg.visibility = View.GONE
    }

    private fun displayMessage(msg: String) {
        binding.cpiLoadingIndicator.visibility = View.GONE
        binding.rvResultsList.visibility = View.GONE
        binding.txtMsg.visibility = View.VISIBLE
        binding.txtMsg.text = msg
    }

    private fun onItemClickListener(coin: SearchedCoin) {
        
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}