package de.hsworms.flashcards.ui.statistic

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.hsworms.flashcard.database.FCDatabase
import de.hsworms.flashcard.database.entity.FlashcardNormal
import de.hsworms.flashcard.database.entity.RepositoryWithCards
import de.hsworms.flashcards.R
import kotlinx.android.synthetic.main.fragment_stat_frag1.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*


class StatFrag1(repo: RepositoryWithCards) : Fragment() {

    val parameter = repo
    val listcard = mutableListOf<FlashcardNormal>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stat_frag1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlobalScope.launch {
            parameter.cards.forEach {
                it.cardId
                it.type
                val fc: FlashcardNormal =
                    FCDatabase.getDatabase(requireContext()).flashcardDao().getOne(it.cardId!!)!! as FlashcardNormal

                listcard.add(fc)

            }

            requireActivity().runOnUiThread {
                sethead()
                set_contain_heute()
                set_contain_month()
            }
        }
    }
    fun sethead(){
        stat_title1.text = parameter.cards.count().toString() + " Karten in " + parameter.repository.name
        stat_title1.textSize = 30F
        stat_title1.textAlignment = View.TEXT_ALIGNMENT_CENTER
    }

    fun set_contain_heute(){
        val calendar = Calendar.getInstance()
        val date = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.time)
        stat_title2.text="Heute"
        stat_title2.textSize=22F
        var number : Int = 0
        var repetition : Int = 0
        var fail: Int = 0
        listcard.forEach(){
            if(it.access_date == date) {
                number++
                repetition += it.access_number
                fail += it.negative_result
            }
        }
        text_stat.text= "Karte gelernt: " + number + "\n" + "Falsch: " + fail + "\n" + "Wiederholungen: " + repetition
        text_stat.textSize=20F
    }

    fun set_contain_month(){
        val calendar = Calendar.getInstance()
        val date:String = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.time)
        var number : Int = 0
        var repetition : Int = 0
        var fail: Int = 0
        stat_title3.text="Letzte 30 Tage"
        stat_title3.textSize=22F
        val str = date.split("/")

        listcard.forEach(){
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
            text_stat1.text= "Karte gelernt: " + number + "\n" + "Falsch: " + fail + "\n" + "Wiederholungen: " + repetition
            text_stat1.textSize=20F
        }
    }

}