package de.hsworms.flashcards.ui.statistic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.hsworms.flashcard.database.entity.RepositoryWithCards
import de.hsworms.flashcards.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import de.hsworms.flashcard.database.FCDatabase
import de.hsworms.flashcard.database.entity.FlashcardNormal
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_stat_frag2.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StatFrag2(repo: RepositoryWithCards) : Fragment() {
    val parameter = repo
    val string_entry = mutableListOf<String>()
    val float_entry = mutableListOf<Float>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stat_frag2, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Piechart.contentDescription="Statistik Erlente Karte der letzen 30 Tage"
        Piechart.holeRadius = 25F
        Piechart.setTransparentCircleAlpha(0)
        Piechart.centerText = "Lernen"
        Piechart.setCenterTextSize(10F)
        Piechart.setDrawEntryLabels(true)
        addData(Piechart)

    }

    private fun addData(piechart: PieChart?) {
        GlobalScope.launch {
            parameter.cards.forEach {
                it.cardId
                it.type
                val fc: FlashcardNormal =
                    FCDatabase.getDatabase(requireContext()).flashcardDao().getOne(it.cardId!!)!! as FlashcardNormal

            }
        }

    }

}