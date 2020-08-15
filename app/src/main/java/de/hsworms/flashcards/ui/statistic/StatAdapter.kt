package de.hsworms.flashcards.ui.statistic

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import de.hsworms.flashcard.database.entity.RepositoryWithCards

class StatAdapter(fm: Fragment, repo: RepositoryWithCards?): FragmentStateAdapter(fm) {
    val Par= repo!!;
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position){
            0 -> {return StatFrag1(Par)}
            1 -> {return StatFrag2()}
            2 -> {return StatFrag3()}
            else -> {return StatFrag1(Par)}
        }
    }
}