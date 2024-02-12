package com.dicoding.navigations

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Locale

class BookTripFragment : Fragment() {

    private lateinit var userPickedDate: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_trip, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tilSelectDate = view.findViewById<TextInputLayout>(R.id.til_selectDate)
        val tietSelectDate = view.findViewById<TextInputEditText>(R.id.tiet_selectDate)
        val tietInputPin = view.findViewById<TextInputEditText>(R.id.tiet_inputPin)
        val btnBookTrip = view.findViewById<Button>(R.id.btn_bookTrip)
        val tvError = view.findViewById<TextView>(R.id.tv_error)

        val userData = BookTripFragmentArgs.fromBundle(arguments as Bundle).userData
        val tripData = BookTripFragmentArgs.fromBundle(arguments as Bundle).trip

        tietSelectDate.inputType = InputType.TYPE_NULL
        tietSelectDate.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){

                val currentTime = System.currentTimeMillis()

                val dateConstraints = CalendarConstraints.Builder()
                    .setStart(currentTime)
                    .setOpenAt(currentTime)
                    .setValidator(
                        DateValidatorPointForward.from(currentTime)
                    )
                    .build()

                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setCalendarConstraints(dateConstraints)
                        .build()

                datePicker.addOnPositiveButtonClickListener {
                    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    userPickedDate = sdf.format(it)
                    tietSelectDate.setText(userPickedDate)
                }
                datePicker.addOnDismissListener {
                    tilSelectDate.clearFocus()
                }

                datePicker.show(childFragmentManager, "datePickerTag")
            }
        }

        btnBookTrip.setOnClickListener {
            if (tietSelectDate.text?.isNotEmpty() == true && tietInputPin?.text?.isNotEmpty() == true){
                val toMyTripsFragment =
                    BookTripFragmentDirections.actionBookTripFragmentToMyTripsFragment()

                if (this::userPickedDate.isInitialized){
                    if (userData.balance > tripData.price){
                        userData.balance -= tripData.price
                        userData.listOfBookTrip.add((BookedTrip(tripData.id, userPickedDate)))

                        view.findNavController().navigate(toMyTripsFragment)
                    } else {
                        tvError.text = "Insufficient amount of balance to book this trip!"
                    }
                }
            } else {
                tvError.text = "Please select your trip date and input your PIN number first!"
            }
        }

    }

}