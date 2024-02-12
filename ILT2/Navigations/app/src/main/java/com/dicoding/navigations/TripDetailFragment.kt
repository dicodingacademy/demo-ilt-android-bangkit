package com.dicoding.navigations

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

class TripDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: 5.1 Below code using Navigation Component - Compare the way to receive data send to this Fragment
        val userData = TripDetailFragmentArgs.fromBundle(arguments as Bundle).userData
        val tripData = TripDetailFragmentArgs.fromBundle(arguments as Bundle).trip

        // TODO: 5.1 Below code using bundle in Fragment Transaction - Compare the way to receive data send to this Fragment
//        val userData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            arguments?.getParcelable("DATA_USER", UserAccount::class.java)
//        } else {
//            arguments?.getParcelable("DATA_USER")
//        } as UserAccount
//        val tripData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            arguments?.getParcelable("DATA_TRIP", Trip::class.java)
//        } else {
//            arguments?.getParcelable("DATA_TRIP")
//        } as Trip

        val ivTripImage = view.findViewById<ImageView>(R.id.iv_tripImage)
        val tvTripName = view.findViewById<TextView>(R.id.tv_tripName)
        val tvTripDescription = view.findViewById<TextView>(R.id.tv_tripDescription)
        val tvTripPrice = view.findViewById<TextView>(R.id.tv_tripPrice)
        val btnBookOrCancelNow = view.findViewById<Button>(R.id.btn_bookOrCancelNow)

        ivTripImage.setImageResource(tripData.image)
        tvTripName.text = tripData.name
        tvTripDescription.text = tripData.description
        tvTripPrice.text = buildSpannedString {
            append("Price: ")
            bold {
                append("$${tripData.price}")
            }
        }

        val isTripBooked = userData.listOfBookTrip.find { it.tripID == tripData.id }

        if (isTripBooked != null){
            btnBookOrCancelNow.text = "Cancel Trip"
            btnBookOrCancelNow.setBackgroundColor(Color.parseColor("#FF8911"))

            btnBookOrCancelNow.setOnClickListener {
                userData.balance += tripData.price

                userData.listOfBookTrip.removeAt(
                    userData.listOfBookTrip.indexOf(isTripBooked)
                )

                val toMyTripsFragment =
                    TripDetailFragmentDirections.actionTripDetailFragmentToMyTripsFragment()
                view.findNavController().navigate(toMyTripsFragment)
            }
        } else {
            btnBookOrCancelNow.text = "Book Now"
            btnBookOrCancelNow.setBackgroundColor(Color.parseColor("#6750A4"))
            btnBookOrCancelNow.setOnClickListener {
                val toBookTripFragment =
                    TripDetailFragmentDirections.actionTripDetailFragmentToBookTripFragment(userData, tripData)
                view.findNavController().navigate(toBookTripFragment)
            }
        }

    }

}