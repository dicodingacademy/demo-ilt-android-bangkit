package com.dicoding.parsingjson.ui

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.parsingjson.R
import com.dicoding.parsingjson.network.Result


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = UserAdapter(mutableListOf())

        val recyclerView = findViewById<RecyclerView>(R.id.rv_users)
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // TODO : [5] Instance ViewModel using KTX
        val mainViewModel: MainViewModel by viewModels { MainViewModel.Factory }

        // TODO : [6] Adjust your code using Single Live Event
        mainViewModel.getUser().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        progressBar.visibility = View.GONE
                        for (newUser in result.data) {
                            adapter.addUser(newUser)
                        }
                    }

                    is Result.Error -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Terjadi kesalahan (${result.error})",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
