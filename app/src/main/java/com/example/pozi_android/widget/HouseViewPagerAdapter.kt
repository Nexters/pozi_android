package com.example.pozi_android.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pozi_android.R
import com.example.pozi_android.data.remote.spec.PBResponce

class HouseViewPagerAdapter :
    ListAdapter<PBResponce, HouseViewPagerAdapter.ItemViewHolder>(differ) {

    inner class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(PBResponce: PBResponce) {
            val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
            val priceTextView = view.findViewById<TextView>(R.id.addressTextView)

            nameTextView.text = PBResponce.name
            priceTextView.text = PBResponce.address

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

        val differ = object : DiffUtil.ItemCallback<PBResponce>() {
            override fun areItemsTheSame(oldItem: PBResponce, newItem: PBResponce): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PBResponce, newItem: PBResponce): Boolean {
                return oldItem == newItem
            }

        }
    }
}