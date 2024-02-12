package com.dicoding.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class FragmentB : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_b, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tietData = view.findViewById<TextInputEditText>(R.id.tietData)
        val btnToFragmentA = view.findViewById<Button>(R.id.btnMoveToFragmentA)

        // TODO: 1. Explain about FragmentManager usage in Activity and Fragment
        val fragmentManager = parentFragmentManager

        btnToFragmentA.setOnClickListener {

            // TODO: 3. Explain about how to send and receive data between Fragments

            val inputtedData = tietData.text.toString()

            val dataToBeSend: String? = if (inputtedData.isNotEmpty()){
                inputtedData
            } else {
                null
            }

            val fragmentA = FragmentA()
            fragmentA.arguments = Bundle().apply {
                putString("DATA_KEY", dataToBeSend)
            }

            fragmentManager
                .beginTransaction()
                .replace(R.id.frameContainer, fragmentA, FragmentA::class.java.simpleName)
                .commit()
        }
    }

}