package de.hsworms.flashcards.ui.statistic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import de.hsworms.flashcard.database.FCDatabase
import de.hsworms.flashcard.database.entity.FlashcardNormal
import de.hsworms.flashcard.database.entity.RepositoryWithCards
import de.hsworms.flashcards.R
import kotlinx.android.synthetic.main.fragment_edit.bottomAppBar
import kotlinx.android.synthetic.main.fragment_stat_frag1.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*

//First Statistic  view
class StatFrag1(repository: RepositoryWithCards) : Fragment() {

    private val parameter = repository
    private val listCard = mutableListOf<FlashcardNormal>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stat_frag1, container, false)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).setSupportActionBar(bottomAppBar)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Abholung aller Karten inm ausgewahlten Modul
        GlobalScope.launch {
            parameter.cards.forEach {
                it.cardId
                it.type
                val fc: FlashcardNormal =
                    FCDatabase.getDatabase(requireContext()).flashcardDao().getOne(it.cardId!!)!! as FlashcardNormal

                listCard.add(fc)

            }

            requireActivity().runOnUiThread {
                setHead()
                setContainHeute()
                setContainMonth()
            }
        }
    }

    private fun setHead(){
        stat_title1.text = parameter.cards.count().toString() + " Karten in " + parameter.repository.name
        stat_title1.textSize = 30F
        stat_title1.textAlignment = View.TEXT_ALIGNMENT_CENTER
    }

    private fun setContainHeute(){
        val calendar = Calendar.getInstance()
        val date = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.time)
        stat_title2.textSize=22F
        var number = 0
        var repetition = 0
        var fail = 0
        listCard.forEach(){
            if(it.access_date == date) {
                number++
                repetition += it.access_number
                fail += it.negative_result
            }
        }
        fail=repetition-fail
        text_stat.text= "Karte gelernt: " + number + "\n" + "Falsch: " + fail + "\n" + "Wiederholungen: " + repetition
        text_stat.textSize=20F
    }

    private fun setContainMonth(){
        val calendar = Calendar.getInstance()
        val date:String = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.time)
        var number = 0
        var repetition = 0
        var fail = 0
        stat_title3.textSize=22F
        val str = date.split("/")

        listCard.forEach(){
            val tmp_date = it.access_date
            val str_date = tmp_date.split("/")
            if(str[0].toInt()==str_date[0].toInt() && str[2].toInt()==str_date[2].toInt()){
                number++
                repetition += it.access_number
                fail += it.negative_result
            }
            else if(str[0].toInt()==str_date[0].toInt()+1  && str[1].toInt()<=str_date[1].toInt() && str[2].toInt()==str_date[2].toInt()){
                number++
                repetition += it.access_number
                fail += it.negative_result
            }
            fail=repetition-fail
            text_stat1.text= "Karte gelernt: " + number + "\n" + "Falsch: " + fail + "\n" + "Wiederholungen: " + repetition
            text_stat1.textSize=20F
        }
    }

}