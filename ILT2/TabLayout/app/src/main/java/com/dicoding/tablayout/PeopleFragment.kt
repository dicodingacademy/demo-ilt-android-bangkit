package com.dicoding.tablayout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PeopleFragment : Fragment() {

    private var position: Int = 0
    private var tripID: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvPeople = view.findViewById<RecyclerView>(R.id.rv_people)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            tripID = it.getInt(ARG_TRIPID)
        }

        val peopleDataID = arrayListOf<Int>()

        if (position == 0) {
            peopleDataID.addAll(TripsData.tripsTourGuides[tripID])
        } else if (position == 1) {
            peopleDataID.addAll(TripsData.tripsPassengers[tripID])
        }

        val peopleData = arrayListOf<User>()

        for (position in peopleDataID.indices){
            val userID = peopleDataID[position]
            val userData = UserData.getUserFromID(userID)

            peopleData.add(userData)
        }

        val adapter = UserAdapter(peopleData)

        rvPeople.layoutManager = LinearLayoutManager(requireContext())
        rvPeople.adapter = adapter
        rvPeople.setHasFixedSize(true)

    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_TRIPID = "tripID"
    }

}