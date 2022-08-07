package com.example.pozi_android.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pozi_android.databinding.ItemPhotoboothDetailViewpagerBinding
import com.example.pozi_android.domain.entity.PB

class MainPBInfoPagerAdapter :
    ListAdapter<PB, MainPBInfoPagerAdapter.ItemViewHolder>(differ) {

    inner class ItemViewHolder(val binding: ItemPhotoboothDetailViewpagerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pbEntity: PB) {
            binding.pb = pbEntity
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
<<<<<<< HEAD:app/src/main/java/com/example/pozi_android/ui/main/MainPBInfoPagerAdapter.kt
        val binding = ItemPhotoboothDetailViewpagerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
=======
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(
            inflater.inflate(
                R.layout.item_pb_viewpager,
                parent,
                false
            )
>>>>>>> fb4e648 ([25-PBWigetUI]):app/src/main/java/com/example/pozi_android/widget/HouseViewPagerAdapter.kt
        )
        return ItemViewHolder(binding)
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