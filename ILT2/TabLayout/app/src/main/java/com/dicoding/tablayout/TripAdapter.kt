package com.dicoding.tablayout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TripAdapter(private var listOfTrip: ArrayList<Trip>): RecyclerView.Adapter<TripAdapter.ListViewHolder>() {

    private val defaultListOfTrip: ArrayList<Trip> = listOfTrip

    interface OnItemClickCallback {
        fun onItemClicked(tripID: Int)
    }

    interface OnNoItemFoundCallback {
        fun onNoItemFoundCallback()
    }

    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var onNoItemFoundCallback: OnNoItemFoundCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setOnNoItemFoundCallback(onNoItemFoundCallback: OnNoItemFoundCallback) {
        this.onNoItemFoundCallback = onNoItemFoundCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivImage: ImageView = itemView.findViewById(R.id.iv_item_image)
        var tvItemTitle: TextView = itemView.findViewById(R.id.tv_item_title)
        var tvItemSubtitle: TextView = itemView.findViewById(R.id.tv_item_subtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_trip_layout, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.ivImage.setImageResource(listOfTrip[position].image)
        holder.tvItemTitle.text = listOfTrip[position].name
        holder.tvItemSubtitle.text = listOfTrip[position].place

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listOfTrip[position].id)
        }
    }

    override fun getItemCount(): Int {
        return listOfTrip.size
    }

    fun filteredListOfTrips(searchName: String){
        listOfTrip = defaultListOfTrip

        val searchList = arrayListOf<Trip>()
        listOfTrip.forEach {
            if (it.name.lowercase().contains(searchName.lowercase())){
                val trip = Trip(
                    id = it.id,
                    name = it.name,
                    image = it.image,
                    description = it.description,
                    price = it.price,
                    place = it.place
                )
                if (!searchList.contains(trip)){
                    searchList.add(trip)
                }
            }
        }

        listOfTrip = searchList

        if (listOfTrip.size == 0){
            onNoItemFoundCallback.onNoItemFoundCallback()
        }

        notifyDataSetChanged()
    }

}