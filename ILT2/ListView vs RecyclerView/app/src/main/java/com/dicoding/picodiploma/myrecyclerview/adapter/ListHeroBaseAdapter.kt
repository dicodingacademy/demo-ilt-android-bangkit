package com.dicoding.picodiploma.myrecyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.dicoding.picodiploma.myrecyclerview.R
import com.dicoding.picodiploma.myrecyclerview.model.Hero

class ListHeroBaseAdapter(private val listHero: ArrayList<Hero>): BaseAdapter() {

    private var counterItemCreated = 0
    private var counterItemBind = 0

    override fun getCount(): Int = listHero.size

    override fun getItem(position: Int): Hero = listHero[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val itemView: View = view ?: LayoutInflater.from(parent.context).inflate(R.layout.item_row_hero, parent, false)
        val viewHolder = ListViewHolder(itemView)

        counterItemCreated += 1
        println("Log - ListView - Item created: $counterItemCreated")

        viewHolder.bind(listHero[position])
        return itemView
    }

    private inner class ListViewHolder(itemView: View) {

        var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        var tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        var tvDescription: TextView = itemView.findViewById(R.id.tv_item_description)

        fun bind(hero: Hero) {
            counterItemBind += 1
            println("Log - ListView - Item bind: $counterItemBind")

            val (name, description, photo) = hero
            imgPhoto.setImageResource(photo)
            tvName.text = name
            tvDescription.text = description
        }
    }
}