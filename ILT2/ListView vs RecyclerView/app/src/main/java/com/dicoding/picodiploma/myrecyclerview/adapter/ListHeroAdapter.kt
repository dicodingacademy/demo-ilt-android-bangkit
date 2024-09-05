package com.dicoding.picodiploma.myrecyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.myrecyclerview.R
import com.dicoding.picodiploma.myrecyclerview.model.Hero
import java.util.*

/**
 * Created by sidiqpermana on 10/28/16.
 */

class ListHeroAdapter(
    private val listHero: ArrayList<Hero>,
) : RecyclerView.Adapter<ListHeroAdapter.ListViewHolder>() {

    private var counterItemCreated = 0
    private var counterItemBind = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_hero, parent, false)

        counterItemCreated += 1
        println("Log - RecyclerView - Item created: $counterItemCreated")

        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        counterItemBind += 1
        println("Log - RecyclerView - Item bind: $counterItemBind")

        val (name, description, photo) = listHero[position]
        holder.imgPhoto.setImageResource(photo)
        holder.tvName.text = name
        holder.tvDescription.text = description
    }

    override fun getItemCount(): Int = listHero.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        var tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        var tvDescription: TextView = itemView.findViewById(R.id.tv_item_description)
    }
}
