package com.example.pozi_android.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pozi_android.R
import com.example.pozi_android.data.remote.spec.Locations

class HouseViewPagerAdapter :
    ListAdapter<Locations, HouseViewPagerAdapter.ItemViewHolder>(differ) {

    inner class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(locations: Locations) {
            val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
            val priceTextView = view.findViewById<TextView>(R.id.addressTextView)

            nameTextView.text = locations.name
            priceTextView.text = locations.address

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(
            inflater.inflate(
                R.layout.item_photobooth_detail_viewpager,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        val differ = object : DiffUtil.ItemCallback<Locations>() {
            override fun areItemsTheSame(oldItem: Locations, newItem: Locations): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Locations, newItem: Locations): Boolean {
                return oldItem == newItem
            }

        }
    }
}