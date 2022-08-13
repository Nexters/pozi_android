package com.example.pozi_android.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pozi_android.R
import com.example.pozi_android.databinding.ItemPbViewpagerBinding
import com.example.pozi_android.domain.entity.PBEntity

class MainPBInfoPagerAdapter :
    ListAdapter<PBEntity, MainPBInfoPagerAdapter.ItemViewHolder>(differ) {

    var findLoadClickListener: ((PBEntity?) -> Unit)? = null

    inner class ItemViewHolder(private val binding: ItemPbViewpagerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.findLoadButton.setOnClickListener {
                findLoadClickListener?.invoke(binding.pb)
            }
        }

        fun bind(pbEntity: PBEntity) {
            binding.pb = pbEntity
            when (pbEntity.brandName) {
                "포토매틱" -> binding.brandimage.setImageResource(R.drawable.brand_photomatic)
                "하루필름" -> binding.brandimage.setImageResource(R.drawable.brand_harufilm)
                "셀픽스" -> binding.brandimage.setImageResource(R.drawable.brand_selfixselfix)
                "포토드링크" -> binding.brandimage.setImageResource(R.drawable.brand_photodrink)
                "포토그레이" -> binding.brandimage.setImageResource(R.drawable.brand_photogray)
                "포토이즘" -> binding.brandimage.setImageResource(R.drawable.brand_photoism)
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