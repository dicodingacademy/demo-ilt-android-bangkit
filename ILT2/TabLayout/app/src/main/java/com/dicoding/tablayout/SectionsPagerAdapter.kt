package com.dicoding.tablayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

// TODO: 5.
// Show briefly about SectionsPagerAdapter.
// The main point here is that using the createFragment method, we can pass position argument
// to a fragment so that we can change the layout according the position value
class SectionsPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {

    var tripID: Int = 0

    override fun createFragment(position: Int): Fragment {
        val fragment = PeopleFragment()
        fragment.arguments = Bundle().apply {
            putInt(PeopleFragment.ARG_POSITION, position)
            putInt(PeopleFragment.ARG_TRIPID, tripID)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 2
    }

}