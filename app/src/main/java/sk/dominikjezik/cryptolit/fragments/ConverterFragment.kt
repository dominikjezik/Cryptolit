package sk.dominikjezik.cryptolit.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import sk.dominikjezik.cryptolit.R
import sk.dominikjezik.cryptolit.adapters.CoinsAdapter
import sk.dominikjezik.cryptolit.databinding.FragmentConverterBinding
import sk.dominikjezik.cryptolit.utilities.Response
import sk.dominikjezik.cryptolit.utilities.ResponseError
import sk.dominikjezik.cryptolit.viewmodels.ConverterViewModel

@AndroidEntryPoint
class ConverterFragment : Fragment() {

    private var _binding: FragmentConverterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ConverterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConverterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        binding.autoCompleteFrom.setOnItemClickListener { parent, view, position, id ->
            viewModel.changeFromExchangeRate(parent.getItemAtPosition(position) as String)
        }

        binding.autoCompleteTo.setOnItemClickListener { parent, view, position, id ->
            viewModel.changeToExchangeRate(parent.getItemAtPosition(position) as String)
        }


        viewModel.fetchExchangeRates()

        viewModel.response.observe(viewLifecycleOwner) { response ->

            response.data?.let {
                binding.cpiLoadingIndicator.visibility = View.GONE

                val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, it.map { it.key.uppercase() }.sorted() )
                binding.autoCompleteFrom.setAdapter(arrayAdapter)
                binding.autoCompleteTo.setAdapter(arrayAdapter)

                binding.autoCompleteFrom.setText("eur", false)
                binding.autoCompleteTo.setText("btc", false)

                viewModel.changeFromExchangeRate("eur")
                viewModel.changeToExchangeRate("btc")
            }

            if (response is Response.Waiting) {
                binding.cpiLoadingIndicator.visibility = View.VISIBLE
            }

            if (response is Response.Error) {
                binding.cpiLoadingIndicator.visibility = View.GONE

                val msg = when (response.errorType) {
                    ResponseError.TOO_MANY_REQUESTS -> getString(R.string.too_many_requests)
                    ResponseError.NO_INTERNET_CONNECTION -> getString(R.string.no_internet_connection)
                    else -> getString(R.string.an_error_occurred)
                }

                Snackbar.make(binding.root, msg, Snackbar.LENGTH_INDEFINITE)
                    .setAction("refresh") { viewModel.fetchExchangeRates() }.show()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}