package sk.dominikjezik.cryptolit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding.widget.RxTextView
import dagger.hilt.android.AndroidEntryPoint
import sk.dominikjezik.cryptolit.R
import sk.dominikjezik.cryptolit.adapters.CoinsResultAdapter
import sk.dominikjezik.cryptolit.databinding.FragmentSearchBinding
import sk.dominikjezik.cryptolit.models.SearchedCoin
import sk.dominikjezik.cryptolit.utilities.Response
import sk.dominikjezik.cryptolit.utilities.ResponseError
import sk.dominikjezik.cryptolit.viewmodels.SearchViewModel
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var coinsResultAdapter: CoinsResultAdapter

    /**
     * Metóda nastaví data binding, vytvorí príslušný adaptér
     * pre recycler view, nastaví automatické odosielanie
     * požiadavky api po skončení písania
     * a nastaví observer.
     */
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

        setupObservers()

        return root
    }


    /**
     * Metóda nastaví príslušné akcie, ktoré sa majú vykonať ak sa zmenia
     * pozorované atribúty z viewmodelu. Ak sa na výsledok čaká zobrazí
     * indikátor čakania, ak nastala chyba zobrazí o tom správu. Ak
     * prídu dáta z API zobrazí ich, v prípade, že sa nenašli
     * žiadne výsledky zobrazí o tom správu.
     */
    private fun setupObservers() {
        viewModel.coinsResult.observe(viewLifecycleOwner) { response ->
            if (response is Response.Waiting) {
                displayLoadingIndicator()
            }
            else if (response is Response.Error) {
                val msg = when (response.errorType) {
                    ResponseError.TOO_MANY_REQUESTS -> getString(R.string.too_many_requests)
                    ResponseError.NO_INTERNET_CONNECTION -> getString(R.string.no_internet_connection)
                    else -> getString(R.string.an_error_occurred)
                }

                displayMessage(msg)
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
    }


    /**
     * Metóda zobrazí zoznam výsledkov a skryje
     * indikátor načítavania a skryje správu.
     */
    private fun displayRecyclerViewWithResults() {
        binding.cpiLoadingIndicator.visibility = View.GONE
        binding.rvResultsList.visibility = View.VISIBLE
        binding.txtMsg.visibility = View.GONE
    }


    /**
     * Metóda zobrazí indikátor načítavania a skryje
     * zoznam výsledkov a skryje správu.
     */
    private fun displayLoadingIndicator() {
        binding.cpiLoadingIndicator.visibility = View.VISIBLE
        binding.rvResultsList.visibility = View.GONE
        binding.txtMsg.visibility = View.GONE
    }


    /**
     * Metóda zobrazí správu a skryje zoznam
     * výsledkov a indikátor načítavania.
     */
    private fun displayMessage(msg: String) {
        binding.cpiLoadingIndicator.visibility = View.GONE
        binding.rvResultsList.visibility = View.GONE
        binding.txtMsg.visibility = View.VISIBLE
        binding.txtMsg.text = msg
    }


    /**
     * Metóda sa spustí pri stlačení na položku vo výsledkoch
     * vyhľadávania a otvorí nový fragment s detailami
     * o kryptomene.
     */
    private fun onItemClickListener(coin: SearchedCoin) {
        val bundle = bundleOf("searched_coin" to coin)
        findNavController().navigate(R.id.action_navigation_search_to_coinDetailsFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}