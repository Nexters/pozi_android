package com.example.pozi_android.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pozi_android.databinding.ItemPhotoboothDetailViewpagerBinding
import com.example.pozi_android.domain.entity.PBEntity

class MainPBInfoPagerAdapter :
    ListAdapter<PBEntity, MainPBInfoPagerAdapter.ItemViewHolder>(differ) {

    inner class ItemViewHolder(val binding: ItemPhotoboothDetailViewpagerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pbEntity: PBEntity) {
            binding.pb = pbEntity
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemPhotoboothDetailViewpagerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        val differ = object : DiffUtil.ItemCallback<PBEntity>() {
            override fun areItemsTheSame(oldItem: PBEntity, newItem: PBEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PBEntity, newItem: PBEntity): Boolean {
                return oldItem == newItem
            }

        }
    }
}