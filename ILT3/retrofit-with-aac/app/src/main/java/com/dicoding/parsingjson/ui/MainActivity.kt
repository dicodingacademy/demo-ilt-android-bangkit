package com.dicoding.parsingjson.ui

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.parsingjson.R


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = UserAdapter(mutableListOf())

        val recyclerView = findViewById<RecyclerView>(R.id.rv_users)

        // TODO : [8] Don't forget to add a ProgressBar to your layout
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // TODO : [7] Instance ViewModel using MainViewModel Factory.
        val mainViewModel = ViewModelProvider(this, MainViewModel.Factory).get(MainViewModel::class.java)

        // TODO : [9] Call Get User from ViewModel
        mainViewModel.getUser()

        // TODO : [10] Observe data
        mainViewModel.isLoading.observe(this) { loadingState ->
            if (loadingState) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }

        mainViewModel.users.observe(this) { users ->
            for (newUser in users) {
                adapter.addUser(newUser)
            }
        }

        mainViewModel.isError.observe(this) { errorMessage ->
            Toast.makeText(
                this,
                "Terjadi kesalahan (${errorMessage})",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
