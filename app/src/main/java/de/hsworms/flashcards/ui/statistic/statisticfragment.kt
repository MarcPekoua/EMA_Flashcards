package de.hsworms.flashcards.ui.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayoutMediator
import de.hsworms.flashcard.database.FCDatabase
import de.hsworms.flashcard.database.entity.FlashcardNormal
import de.hsworms.flashcard.database.entity.RepositoryWithCards
import de.hsworms.flashcards.R
import de.hsworms.flashcards.ui.edit.RepositoryAdapter
import de.hsworms.flashcards.ui.statistic.StatAdapter
import kotlinx.android.synthetic.main.fragment_gallery.bottomAppBar
import kotlinx.android.synthetic.main.fragment_statistic.*
import kotlinx.android.synthetic.main.header_layout_generic.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class statisticfragment: Fragment() {
    private lateinit var statviewmodel : StatisticViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

    }
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).setSupportActionBar(bottomAppBar)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        statviewmodel=ViewModelProviders.of(this).get(StatisticViewModel::class.java)
        statviewmodel.text.observe(viewLifecycleOwner, Observer {  })
        return inflater.inflate(R.layout.fragment_statistic,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Statview?.adapter = StatAdapter(this)
        TabLayoutMediator(Stathead, Statview){
            Stathead, position -> Statview.setCurrentItem(Stathead.position, true)
            when (position) {
                0 -> {
                    Stathead.setText("StatFrag1")
                }
                1 -> {
                    Stathead.setText("StatFrag2")
                }
                2 -> {
                    Stathead.setText("StatFrag3")
                }

            }
        }.attach()
        statviewmodel.text.observe(viewLifecycleOwner, Observer {  })

        headerHeadlineTextView.text = "Statistik"
        headerSublineTextView.text = ""

        GlobalScope.launch {
            val repos = FCDatabase.getDatabase(requireContext()).repositoryDao().getAllRepositoriesWithCards().toTypedArray()
            requireActivity().runOnUiThread {
                ModulStatSpinner.adapter = RepositoryAdapter(requireContext(), android.R.layout.simple_spinner_item, repos)
            }
        }


    }
}