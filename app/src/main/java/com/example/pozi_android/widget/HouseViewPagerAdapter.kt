package com.example.pozi_android.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pozi_android.R
import com.example.pozi_android.domain.entity.PB

class HouseViewPagerAdapter :
    ListAdapter<PB, HouseViewPagerAdapter.ItemViewHolder>(differ) {

    inner class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(PB: PB) {
            val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
            val priceTextView = view.findViewById<TextView>(R.id.addressTextView)

            nameTextView.text = PB.name
            priceTextView.text = PB.address

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

        val differ = object : DiffUtil.ItemCallback<PB>() {
            override fun areItemsTheSame(oldItem: PB, newItem: PB): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PB, newItem: PB): Boolean {
                return oldItem == newItem
            }

        }
    }
}