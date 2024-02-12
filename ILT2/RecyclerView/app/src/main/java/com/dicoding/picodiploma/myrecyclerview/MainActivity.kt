package com.dicoding.picodiploma.myrecyclerview

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.myrecyclerview.adapter.ListHeroAdapter
import com.dicoding.picodiploma.myrecyclerview.model.Hero
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var rvHeroes: RecyclerView
    private val list = ArrayList<Hero>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvHeroes = findViewById(R.id.rv_heroes)
        rvHeroes.setHasFixedSize(true)

        // TODO: 1. Data source of the Recycler View
        list.addAll(getListOfHeroes())
        showRecyclerList()
    }

    // TODO: 1. Data source of the Recycler View
    private fun getListOfHeroes(): ArrayList<Hero>{
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val listHero = ArrayList<Hero>()
        for (i in dataName.indices) {
            val hero = Hero(dataName[i],dataDescription[i], dataPhoto.getResourceId(i, -1))
            listHero.add(hero)
        }
        return listHero
    }

    private fun showRecyclerList() {

        // TODO: 2. Show Adapter instantiation and how we pass the Data Source into it
        //using lambda
        val listHeroAdapter = ListHeroAdapter(list){ data ->
            showSelectedHero(data)
        }

        //using interface
        listHeroAdapter.setOnItemClickCallback(object : ListHeroAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Hero) {
                showSelectedHero(data)
            }
        })

        // TODO: 5. Show the Layout Manager of the RecycerView
        rvHeroes.layoutManager = LinearLayoutManager(this)

        // TODO: 6. Show how the adapter that we created be connected with the RecyclerView
        rvHeroes.adapter = listHeroAdapter
    }

    private fun showSelectedHero(hero: Hero) {
        Toast.makeText(this, "You choose: " + hero.name, Toast.LENGTH_SHORT).show()
    }
}