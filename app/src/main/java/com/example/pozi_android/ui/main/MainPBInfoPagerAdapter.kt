package com.example.pozi_android.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pozi_android.R
import com.example.pozi_android.databinding.ItemPbViewpagerBinding
import com.example.pozi_android.ui.main.model.CustomMarkerModel

class MainPBInfoPagerAdapter :
    ListAdapter<CustomMarkerModel, MainPBInfoPagerAdapter.ItemViewHolder>(differ) {

    var findLoadClickListener: ((CustomMarkerModel?) -> Unit)? = null

    inner class ItemViewHolder(private val binding: ItemPbViewpagerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.findLoadButton.setOnClickListener {
                findLoadClickListener?.invoke(binding.place)
            }
        }

        fun bind(item: CustomMarkerModel) {
            binding.place = item
            when (item.brandName) {
                "인생네컷" -> setImage(R.drawable.brand_lifefourcut)
                "포토매틱" -> setImage(R.drawable.brand_photomatic)
                "하루필름" -> setImage(R.drawable.brand_harufilm)
                "셀픽스" -> setImage(R.drawable.brand_selfix)
                "포토드링크" -> setImage(R.drawable.brand_photodrink)
                "포토그레이" -> setImage(R.drawable.brand_photogray)
                "포토이즘" -> setImage(R.drawable.brand_photoism)
                "비룸스튜디오" -> setImage(R.drawable.brand_broom)
            }
        }

        private fun setImage(drawable: Int) {
            Glide.with(binding.brandimage.context)
                .load(drawable)
                .into(binding.brandimage)
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
        val differ = object : DiffUtil.ItemCallback<CustomMarkerModel>() {
            override fun areItemsTheSame(oldItem: CustomMarkerModel, newItem: CustomMarkerModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: CustomMarkerModel, newItem: CustomMarkerModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}