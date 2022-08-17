package com.example.pozi_android.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pozi_android.R
import com.example.pozi_android.databinding.ItemPbViewpagerBinding
import com.example.pozi_android.domain.entity.Place
import com.example.pozi_android.domain.entity.ViewPagerItem

class MainPBInfoPagerAdapter :
    ListAdapter<ViewPagerItem, MainPBInfoPagerAdapter.ItemViewHolder>(differ) {

    var findLoadClickListener: ((ViewPagerItem?) -> Unit)? = null

    inner class ItemViewHolder(private val binding: ItemPbViewpagerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.findLoadButton.setOnClickListener {
                findLoadClickListener?.invoke(binding.viewpageritem)
            }
        }

        fun bind(item: ViewPagerItem) {
            binding.viewpageritem = item
            when (item.place.brandName) {
                "포토매틱" -> binding.brandimage.setImageResource(R.drawable.brand_photomatic)
                "하루필름" -> binding.brandimage.setImageResource(R.drawable.brand_harufilm)
                "셀픽스" -> binding.brandimage.setImageResource(R.drawable.brand_selfixselfix)
                "포토드링크" -> binding.brandimage.setImageResource(R.drawable.brand_photodrink)
                "포토그레이" -> binding.brandimage.setImageResource(R.drawable.brand_photogray)
                "포토이즘" -> binding.brandimage.setImageResource(R.drawable.brand_photoism)
                "비룸" -> binding.brandimage.setImageResource(R.drawable.brand_broom)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemPbViewpagerBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<ViewPagerItem>() {
            override fun areItemsTheSame(oldItem: ViewPagerItem, newItem: ViewPagerItem): Boolean {
                return oldItem.place.id == newItem.place.id
            }

            override fun areContentsTheSame(oldItem: ViewPagerItem, newItem: ViewPagerItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}