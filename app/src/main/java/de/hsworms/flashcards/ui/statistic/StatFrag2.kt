package de.hsworms.flashcards.ui.statistic

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import de.hsworms.flashcard.database.entity.RepositoryWithCards
import de.hsworms.flashcards.R
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import de.hsworms.flashcard.database.FCDatabase
import de.hsworms.flashcard.database.entity.FlashcardNormal
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_stat_frag2.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*

class StatFrag2(repository: RepositoryWithCards) : Fragment() {
    private val parameter = repository
    private val floatEntry = mutableListOf<Float>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stat_frag2, container, false)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).setSupportActionBar(bottomAppBar)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Piechart.description.text="\n\n\nStatistik Erlente Karte der letzen 30 Tage"
        Piechart.description.textSize=15F
        Piechart.holeRadius = 20F
        Piechart.setTransparentCircleAlpha(0)
        Piechart.centerText = "Lernen"
        Piechart.setCenterTextSize(10F)
        Piechart.setDrawEntryLabels(true)
        addData()

        //On click auf einem Element im Pie Chart
        Piechart.setOnChartValueSelectedListener(object: OnChartValueSelectedListener{
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                Log.d("Value :"+ e?.getData().toString(), "test")
                GlobalScope.launch {
                    val fc: FlashcardNormal = FCDatabase.getDatabase(requireContext()).flashcardDao().getOne(e?.data.toString().toLong()) as FlashcardNormal
                    requireActivity().runOnUiThread(){
                        val text: String = "Frage: "+ fc.front + "\n" + fc.accessNumber + " Wiederholt" + "\n" + fc.result + " geschafft."
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                    }
                }

            }

            override fun onNothingSelected() {
                return
            }

        })

    }

    /*
        Set Data fürs Char
     */
    private fun addData() {
        val yEntry = mutableListOf<PieEntry>()
        val colors = mutableListOf<Int>()
        val calendar = Calendar.getInstance()
        val date:String = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.time)
        val str = date.split("/")

        GlobalScope.launch {
            parameter.cards.forEach {
                it.cardId
                it.type
                val fc: FlashcardNormal =
                    FCDatabase.getDatabase(requireContext()).flashcardDao().getOne(it.cardId!!)!! as FlashcardNormal
                val tmp_date = fc.access_date
                val str_date = tmp_date.split("/")
                if(str[0].toInt()==str_date[0].toInt() && str[2].toInt()==str_date[2].toInt()){
                    val temp: Float
                    if(fc.accessNumber!=0) {
                        temp= fc.accessNumber.toFloat()
                    }else{
                        temp = 1F
                    }
                    floatEntry.add(temp)
                    yEntry.add(PieEntry(temp,fc.cardId))
                }
                else if(str[0].toInt()==str_date[0].toInt()+1  && str[1].toInt()<=str_date[1].toInt() && str[2].toInt()==str_date[2].toInt()) {
                    val temp: Float
                    if(fc.accessNumber!=0) {
                        // temp = ((fc.access_number - fc.negative_result).toFloat() / fc.access_number)
                        temp= fc.accessNumber.toFloat()
                    }else{
                        temp = 1F
                    }
                    floatEntry.add(temp)
                    yEntry.add(PieEntry(temp,fc.cardId))
                }

            }

            val pieDataSet = PieDataSet(yEntry, "Erfolgsquotient")
            pieDataSet.sliceSpace = 2F
            pieDataSet.valueTextSize= 15F


            colors.add(Color.RED)
            colors.add(Color.BLUE)
            colors.add(Color.MAGENTA)
            colors.add(Color.YELLOW)
            colors.add(Color.GREEN)
            colors.add(Color.CYAN)

            pieDataSet.colors = colors

            val pieData = PieData(pieDataSet)


            requireActivity().runOnUiThread {
                val legend: Legend = Piechart.legend
                legend.form = Legend.LegendForm.CIRCLE

                Piechart!!.data = pieData
                Piechart.invalidate()
            }

        }

    }

}