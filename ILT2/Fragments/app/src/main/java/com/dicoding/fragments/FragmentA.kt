package com.dicoding.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class FragmentA : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_a, container, false)
    }

    // TODO: 2. Highlight the Fragment differences compare to Activity
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: 2. Highlight the Fragment differences compare to Activity
        val tvDataFromFragmentB = view.findViewById<TextView>(R.id.tvDataFromFragmentB)
        val btnClickMe = view.findViewById<Button>(R.id.btnClickMe)
        val btnToFragmentB = view.findViewById<Button>(R.id.btnMoveToFragmentB)

        // TODO: 1. Explain about FragmentManager usage in Activity and Fragment
        val fragmentManager = parentFragmentManager

        // TODO: 3. Explain about how to send and receive data between Fragments
        val datafromFragmentB = arguments?.getString("DATA_KEY")
        tvDataFromFragmentB.text = datafromFragmentB

        btnClickMe.setOnClickListener {
            // TODO: 2. Highlight the Fragment differences compare to Activity
            Toast.makeText(requireContext(), "Hello from Fragment A", Toast.LENGTH_SHORT).show()
        }

        btnToFragmentB.setOnClickListener {

            // TODO: 1. Explain about FragmentManager usage in Activity and Fragment

            val fragmentB = FragmentB()

            fragmentManager
                .beginTransaction()
                .replace(R.id.frameContainer, fragmentB, FragmentB::class.java.simpleName)
                .addToBackStack(null)
                .commit()
        }
    }

}