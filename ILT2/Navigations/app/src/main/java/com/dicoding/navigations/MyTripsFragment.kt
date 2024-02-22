package com.dicoding.navigations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyTripsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_trips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tv_noTripsBooked = view.findViewById<TextView>(R.id.tv_noTripsBooked)
        val btn_noTripsBooked = view.findViewById<TextView>(R.id.btn_noTripsBooked)
        val rv_myTrips = view.findViewById<RecyclerView>(R.id.rv_myTrips)

        val userData = MyApplication.userData

        if (userData.listOfBookTrip.isEmpty()){
            rv_myTrips.visibility = View.GONE

            tv_noTripsBooked.visibility = View.VISIBLE
            btn_noTripsBooked.visibility = View.VISIBLE

            btn_noTripsBooked.setOnClickListener {
                val toHomeFragment =
                    MyTripsFragmentDirections.actionMyTripsFragmentToHomeFragment()
                view.findNavController().navigate(toHomeFragment)
            }
        } else {
            rv_myTrips.visibility = View.VISIBLE

            tv_noTripsBooked.visibility = View.GONE
            btn_noTripsBooked.visibility = View.GONE

            rv_myTrips.layoutManager = LinearLayoutManager(requireContext())

            val adapter = MyTripAdapter(userData.listOfBookTrip)
            adapter.setOnItemClickCallback(object: MyTripAdapter.OnItemClickCallback{
                override fun onItemClicked(tripData: Trip) {
                    val toTripDetailFragment =
                        MyTripsFragmentDirections.actionMyTripsFragmentToTripDetailFragment(userData, tripData)
                    view.findNavController().navigate(toTripDetailFragment)
                }

            })

            rv_myTrips.adapter = adapter
        }

    }

}