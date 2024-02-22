package com.dicoding.navigations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: 4. Introduce about HomeFragment
// Home Fragment is a fragment that show list of Trip - as additional you may show Trip data class
// Moreover, the list data is from TripsData object
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvUserBalance = view.findViewById<TextView>(R.id.tv_userBalance)
        val rvExploreTrips = view.findViewById<RecyclerView>(R.id.rv_exploreTrips)

        val userData = MyApplication.userData

        tvUserBalance.text = "Your current balance: $${userData.balance}"

        rvExploreTrips.layoutManager = GridLayoutManager(requireContext(), 3)
        val adapter = TripAdapter(TripsData.listTripsData)

        adapter.setOnItemClickCallback(object: TripAdapter.OnItemClickCallback{
            override fun onItemClicked(tripData: Trip) {
                // TODO: 5. One of the reason to use Navigation is because it's easier to navigate and pass data compare to Fragment Transaction
                // Highlight about NavController to navigate between Fragment
                val toTripDetailFragment =
                    HomeFragmentDirections.actionHomeFragmentToTripDetailFragment(userData, tripData)
                view.findNavController().navigate(toTripDetailFragment)

                // TODO: 5. Compare it with below code - Same goal but not as convenient and readable as above code
//                val fragmentTripDetailFragment = TripDetailFragment()
//                fragmentTripDetailFragment.arguments = Bundle().apply {
//                    putParcelable("DATA_TRIP", tripData)
//                    putParcelable("DATA_USER", userData)
//                }
//
//                parentFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.nav_host_fragment, fragmentTripDetailFragment, TripDetailFragment::class.java.simpleName)
//                    .commit()
            }

        })

        rvExploreTrips.adapter = adapter

    }

}