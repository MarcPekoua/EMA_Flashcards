package de.hsworms.flashcards.ui.statistic

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import de.hsworms.flashcard.database.FCDatabase
import de.hsworms.flashcard.database.entity.RepositoryWithCards
import de.hsworms.flashcards.R
import de.hsworms.flashcards.ui.edit.RepositoryAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_statistic.*
import kotlinx.android.synthetic.main.fragment_statistic.bottomAppBar
import kotlinx.android.synthetic.main.fragment_statistic.bottomAppBarFab
import kotlinx.android.synthetic.main.header_layout_generic.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//Statistik Hauptseite

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

        bottomAppBar.setOnClickListener {
            findNavController().navigate(R.id.nav_home)
        }

        statviewmodel.text.observe(viewLifecycleOwner, Observer {  })

        headerHeadlineTextView.text = "Statistik"
        headerSublineTextView.text = ""


        GlobalScope.launch {
            val repos = FCDatabase.getDatabase(requireContext()).repositoryDao().getAllRepositoriesWithCards().toTypedArray()
            requireActivity().runOnUiThread {
                ModulStatSpinner.adapter = RepositoryAdapter(requireContext(), android.R.layout.simple_spinner_item, repos)

            }
        }

        //Check selected Daten un set Headear Statitik
        ModulStatSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                return
            }

            //Generate a Statistik view

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val repo = ModulStatSpinner.selectedItem as RepositoryWithCards
                Statview?.adapter = StatAdapter(this@statisticfragment, repo)
                TabLayoutMediator(Stathead, Statview){
                        Stathead, position -> Statview.setCurrentItem(Stathead.position, true)
                    when (position) {
                        0 -> {
                            Stathead.setText("Übersicht")

                        }
                        1 -> {
                            Stathead.setText("Grafik")
                        }
                        2 -> {
                            Stathead.setText("StatFrag3")
                        }

                    }
                }.attach()
            }

        }

    }
}