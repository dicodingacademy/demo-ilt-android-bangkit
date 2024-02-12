package com.dicoding.navigations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.recyclerview.widget.RecyclerView

class MyTripAdapter(private val bookedTripList: List<BookedTrip>): RecyclerView.Adapter<MyTripAdapter.ListViewHolder>() {

    interface OnItemClickCallback{
        fun onItemClicked(tripData: Trip)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_itemBookTripImage: ImageView = itemView.findViewById(R.id.iv_itemBookTripImage)
        var tv_itemBookTripName: TextView = itemView.findViewById(R.id.tv_itemBookTripName)
        var tv_itemBookTripDate: TextView = itemView.findViewById(R.id.tv_itemBookTripDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.book_trip_item_layout, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val bookedTrip = bookedTripList[position]
        val tripData = TripsData.getSpecificTrip(bookedTrip.tripID)

        holder.iv_itemBookTripImage.setImageResource(tripData.image)
        holder.tv_itemBookTripName.text = tripData.name
        holder.tv_itemBookTripDate.text = buildSpannedString {
            append("Booked for ")
            bold {
                append(bookedTrip.tripDate)
            }
        }

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(tripData) }
    }

    override fun getItemCount(): Int {
        return bookedTripList.size
    }

}