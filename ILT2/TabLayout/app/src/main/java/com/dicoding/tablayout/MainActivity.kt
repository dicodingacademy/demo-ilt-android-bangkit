package com.dicoding.tablayout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBar = findViewById<SearchBar>(R.id.searchBar)
        val searchView = findViewById<SearchView>(R.id.searchView)
        val rvTrip = findViewById<RecyclerView>(R.id.rvTrip)
        val tvNoDataFound = findViewById<TextView>(R.id.tvNoDataFound)

        // TODO: 2. Explain how to integrate searchBar to searchView and the searchView listener setup
        searchView.setupWithSearchBar(searchBar)

        val adapter = TripAdapter(TripsData.listTripsData)

        adapter.setOnItemClickCallback(object: TripAdapter.OnItemClickCallback{
            override fun onItemClicked(tripID: Int) {
                val intent = Intent(this@MainActivity, DetailTripActivity::class.java)
                intent.putExtra(DetailTripActivity.EXTRA_TRIPID, tripID)
                startActivity(intent)
            }

        })

        adapter.setOnNoItemFoundCallback(object: TripAdapter.OnNoItemFoundCallback{
            override fun onNoItemFoundCallback() {
                rvTrip.visibility = View.GONE
                tvNoDataFound.visibility = View.VISIBLE
            }
        })

        rvTrip.layoutManager = LinearLayoutManager(this)
        rvTrip.adapter = adapter

        // TODO: 2. Explain how to integrate searchBar to searchView and the searchView listener setup
        searchView
            .editText
            .setOnEditorActionListener { view, actionId, event ->
                val searchText = searchView.text.toString()
                searchBar.setText(searchView.text)

                if (searchText.isNotBlank()){
                    rvTrip.visibility = View.VISIBLE
                    tvNoDataFound.visibility = View.GONE

                    adapter.filteredListOfTrips(searchView.text.toString())
                }

                searchView.hide()
                false
            }

    }

}