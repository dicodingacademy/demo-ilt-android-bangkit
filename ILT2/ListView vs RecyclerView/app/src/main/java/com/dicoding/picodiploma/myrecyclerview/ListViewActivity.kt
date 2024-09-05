package com.dicoding.picodiploma.myrecyclerview

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.myrecyclerview.adapter.ListHeroAdapter
import com.dicoding.picodiploma.myrecyclerview.adapter.ListHeroBaseAdapter
import com.dicoding.picodiploma.myrecyclerview.model.Hero
import java.util.*

class ListViewActivity : AppCompatActivity() {
    private lateinit var lvHeroes: ListView
    private val list = ArrayList<Hero>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)

        list.addAll(getListOfHeroes())
        showListView()
    }

    private fun getListOfHeroes(): ArrayList<Hero>{
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val listHero = ArrayList<Hero>()
        for (x in 1..10) {
            for (i in dataName.indices) {
                val hero = Hero(dataName[i],dataDescription[i], dataPhoto.getResourceId(i, -1))
                listHero.add(hero)
            }
        }
        return listHero
    }

    private fun showListView(){
        lvHeroes = findViewById(R.id.lv_heroes)

        val listHeroAdapter = ListHeroBaseAdapter(list)

        lvHeroes.adapter = listHeroAdapter
    }
}