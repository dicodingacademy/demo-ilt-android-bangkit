package com.dicoding.debugging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.dicoding.debugging.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listOfNames = listOf("Arif", "Gilang", "Ilham", "Ian")
        var index = 0

        binding.btnShowAName.setOnClickListener {
            Toast.makeText(this, "Button clicked!", Toast.LENGTH_SHORT).show()

            binding.tvName.text = listOfNames[index]
            index += 1

//            if (index == listOfNames.size){
//                index = 0
//            }
        }
    }
}