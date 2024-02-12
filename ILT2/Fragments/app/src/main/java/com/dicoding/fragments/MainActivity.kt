package com.dicoding.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnToFragmentA = findViewById<Button>(R.id.btnFragmentA)
        val btnToFragmentB = findViewById<Button>(R.id.btnFragmentB)

        // TODO: 1. Explain about FragmentManager usage in Activity and Fragment
        val fragmentManager = supportFragmentManager
        val fragmentA = FragmentA()
        val fragmentB = FragmentB()

        fragmentManager
            .beginTransaction()
            .add(R.id.frameContainer, fragmentA, fragmentA::class.java.simpleName)
            .commit()

        btnToFragmentA.setOnClickListener {

            // TODO: 1. Explain about FragmentManager usage in Activity and Fragment

            val fragment = fragmentManager.findFragmentByTag(FragmentA::class.java.simpleName)

            if (fragment !is FragmentA) {
                fragmentManager
                    .beginTransaction()
                    .replace(R.id.frameContainer, fragmentA, FragmentA::class.java.simpleName)
                    .commit()
            }
        }

        btnToFragmentB.setOnClickListener {

            // TODO: 1. Explain about FragmentManager usage in Activity and Fragment

            val fragment = fragmentManager.findFragmentByTag(FragmentB::class.java.simpleName)

            if (fragment !is FragmentB) {
                fragmentManager
                    .beginTransaction()
                    .replace(R.id.frameContainer, fragmentB, FragmentB::class.java.simpleName)
                    .commit()
            }
        }
    }
}