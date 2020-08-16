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


class StatFrag1(repo: RepositoryWithCards) : Fragment() {

    val parameter = repo
    val listCreate = mutableListOf<String>()
    val listAccess = mutableListOf<String>()
    val anzahl_access = mutableListOf<Int>()
    val anzaahl_negative_result = mutableListOf<Int>()

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
                val create = fc.create
                val access = fc.access_date
                listCreate.add(create)
                listAccess.add(access)
                anzahl_access.add(fc.access_number)
                anzaahl_negative_result.add(fc.negative_result)
            }

            requireActivity().runOnUiThread {
                //val type: Typeface = Typeface.createFromAsset(context?.assets, "fonts/arial.ttf")
                Test.text = parameter.repository.name + "\n" + listCreate + "\n" + listAccess + "\n" + anzahl_access
                textView2.text=anzaahl_negative_result.toString()
                //textView2.setTypeface(type)
            }
        }
    }

}