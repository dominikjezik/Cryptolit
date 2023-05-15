package sk.dominikjezik.cryptolit.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import sk.dominikjezik.cryptolit.R
import sk.dominikjezik.cryptolit.databinding.FragmentCoinDetailsBinding
import sk.dominikjezik.cryptolit.models.Coin
import sk.dominikjezik.cryptolit.models.CoinChartResponse
import sk.dominikjezik.cryptolit.models.SearchedCoin
import sk.dominikjezik.cryptolit.utilities.*
import sk.dominikjezik.cryptolit.viewmodels.CoinDetailsViewModel

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

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        val coin = arguments.getSerializableArg("coin", Coin::class.java)
        val searchedCoin = arguments.getSerializableArg("searched_coin", SearchedCoin::class.java)

        if (coin != null) {
            viewModel.coin = coin
        } else {
            viewModel.coin = Coin(searchedCoin!!.id, searchedCoin.symbol, searchedCoin.name, 0f, searchedCoin.large, searchedCoin.market_cap_rank)
        }

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        this.setupChart()
        this.setupObservers()

        return root;
    }

    private fun setupObservers() {
        viewModel.coinChartData.observe(viewLifecycleOwner) { response ->
            response.data?.let {
                processResponseIntoChart(it)
            }

            if (response is Response.Error) {
                displayErrorSnackBar(response, binding.root, requireContext(), viewModel::fetchCoinChartData)
            }
        }
    }


    private fun processResponseIntoChart(response: CoinChartResponse) {
        val entries = response.prices.map { Entry(it[0], it[1]) }
        val lineDataSet = LineDataSet(entries, "")

        lineDataSet.lineWidth = 2f
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawValues(false)

        binding.lineChart.data = LineData(lineDataSet)
        binding.lineChart.invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupChart() = binding.lineChart.apply {

        isHighlightPerDragEnabled = true

        setOnTouchListener { v, event ->
            val x = event.x
            val y = event.y
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    val entry: Entry? =
                        getEntryByTouchPoint(x, y)
                    if (entry != null) {
                        val lastPrice = entry.y

                        viewModel.displayPrice(lastPrice)
                    }
                }

                MotionEvent.ACTION_UP -> {
                    // Zrušenie highlightu po uvoľnení prsta
                    highlightValue(null)
                    viewModel.displayDefaultPrice()
                }
            }
            false
        }

        legend.isEnabled = false
        description.isEnabled = false
        isHighlightPerTapEnabled = false
        isHighlightPerDragEnabled = true

        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(false)

        axisRight.setDrawAxisLine(false)
        axisRight.setDrawGridLines(true)
        axisRight.setDrawLabels(false)
        axisRight.gridColor = Color.WHITE

        axisLeft.setDrawAxisLine(false)
        axisLeft.setDrawGridLines(true)
        axisLeft.setLabelCount(2, true)
        axisLeft.textColor = Color.WHITE
        axisLeft.gridColor = Color.WHITE
        axisLeft.gridLineWidth = 0.2f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}