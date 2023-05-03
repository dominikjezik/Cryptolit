package sk.dominikjezik.cryptolit.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import sk.dominikjezik.cryptolit.databinding.FragmentCoinDetailsBinding
import sk.dominikjezik.cryptolit.models.Coin
import sk.dominikjezik.cryptolit.models.CoinChartResponse
import sk.dominikjezik.cryptolit.utilities.getSerializableArg
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

        viewModel.coin = arguments.getSerializableArg("coin", Coin::class.java)
        viewModel.fetchCoinChartData()

        binding.viewmodel = viewModel

        // styling chart
        this.styleChart()

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        viewModel.coinChartData.observe(viewLifecycleOwner) {
            it.data?.let {
                processResponseIntoChart(it)
            }
        }

        return root;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun styleChart() = binding.lineChart.apply {
        legend.isEnabled = false
        description.isEnabled = false
        isHighlightPerTapEnabled = false
        isHighlightPerDragEnabled = false

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

}