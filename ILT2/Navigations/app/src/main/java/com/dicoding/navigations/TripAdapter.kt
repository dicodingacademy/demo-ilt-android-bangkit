package com.dicoding.navigations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TripAdapter(private val tripList: List<Trip>): RecyclerView.Adapter<TripAdapter.ListViewHolder>() {

    interface OnItemClickCallback{
        fun onItemClicked(tripData: Trip)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_tripImage: ImageView = itemView.findViewById(R.id.iv_itemTripImage)
        var tv_tripName: TextView = itemView.findViewById(R.id.tv_itemTripName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.trip_item_layout, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val trip = tripList[position]

        holder.iv_tripImage.setImageResource(trip.image)
        holder.tv_tripName.text = trip.name

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(tripList[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int {
        return tripList.size
    }

}